package payment.contact

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.*
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.people.v1.PeopleScopes
import com.google.api.services.plusDomains.PlusDomains
import com.google.api.services.plusDomains.PlusDomainsScopes
import com.google.api.services.plusDomains.model.Circle
import com.google.api.services.plusDomains.model.CircleFeed
import com.google.api.services.plusDomains.model.PeopleFeed
import com.google.api.services.plusDomains.model.Person
import com.google.common.io.Files
import com.google.gdata.client.contacts.ContactsService
import com.google.gdata.data.contacts.ContactEntry
import com.google.gdata.data.contacts.ContactFeed
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRDTO

import javax.annotation.PreDestroy
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Transactional
class ContactFromGoogleService {

    ContactService contactService
    def grailsApplication

    private static final Integer MAX_BULK_CONTACTS = 10000;

    private static final String URL_LOAD_ALL_CONTACTS= "https://www.google.com/m8/feeds/contacts/default/full"

    /** Application name. */
    private static final String APPLICATION_NAME = "Kuorum";

    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/people.googleapis.com-java-quickstart");
    private static final java.io.File DATA_STORE_DIR = Files.createTempDir();

    /** Global instance of the {@link com.google.api.client.util.store.FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY=new FileDataStoreFactory(DATA_STORE_DIR);
    private static final JsonFactory JSON_FACTORY =JacksonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/people.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(PeopleScopes.CONTACTS_READONLY, PlusDomainsScopes.PLUS_CIRCLES_READ);

    ExecutorService executor = Executors.newSingleThreadExecutor()
    @PreDestroy
    void shutdown() {
        executor.shutdownNow()
    }


    public String getUrlForAuthorization(String urlCallback){
        AuthorizationCodeFlow flow = googleAuthorizationFlow(urlCallback);
        GoogleAuthorizationCodeRequestUrl authUrl = flow.newAuthorizationUrl();
        authUrl.setRedirectUri(urlCallback)
        return authUrl.toURL();
    }


    public void loadContacts(KuorumUser user, String authorizationCode, String urlCallback){
        log.info("Loading ${user.alias}'s gmail contacs")
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        HTTP_TRANSPORT,
                        JSON_FACTORY,
                        grailsApplication.config.oauth.providers.google.key,
                        grailsApplication.config.oauth.providers.google.secret,
                        authorizationCode,
                        urlCallback
                ).execute();
        Credential credential = createCredentialFromToken(tokenResponse, urlCallback)
        loadContactUsingGData(user, credential);

    }

    private Credential createCredentialFromToken(TokenResponse token, String urlCallback){

        new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(new NetHttpTransport())
                .setClientSecrets(getGoogleClientSecrets(urlCallback)).build()
                .setFromTokenResponse(token);
    }

    private Map loadCirclesMappedByName(Credential credential){

        Map<String, Set<String>> mapCircles = [:];
        try {
            PlusDomains plusDomains = new PlusDomains.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            PlusDomains.Circles.List listCircles = plusDomains.circles().list("me");
            listCircles.setMaxResults(5L);
            CircleFeed circleFeed = listCircles.execute();
            List<Circle> circles = circleFeed.getItems();

            // Loop until no additional pages of results are available.
            while (circles != null) {
                for (Circle circle : circles) {
                    PlusDomains.People.ListByCircle listPeople = plusDomains.people().listByCircle(circle.getId());
                    log.info("Circle ${circle.getDisplayName()}[${circle.getPeople().getTotalItems()} contacts]")
                    PeopleFeed peopleFeed = listPeople.execute();
                    while (peopleFeed != null) {
                        if (peopleFeed.getItems() != null && peopleFeed.getItems().size() > 0) {
                            for (Person person : peopleFeed.getItems()) {
                                log.debug("${circle.getDisplayName()} :: ${person.getId()} (${person.getDisplayName()})")
                                String mapName = person.getDisplayName();
                                Set<String> userCircles = mapCircles.get(mapName)
                                userCircles = userCircles ?: [] as Set<String>
                                userCircles.add(circle.getDisplayName())
                                mapCircles.put(mapName, userCircles)
                            }
                        } else {
                            log.debug("Circle ${circle.getDisplayName()} with no contacts")
                        }
                        if (peopleFeed.getNextPageToken() != null) {
                            listPeople.setPageToken(peopleFeed.getNextPageToken())
                            peopleFeed = listPeople.execute();
                        } else {
                            log.debug("No more contacts on ${circle.getDisplayName()}")
                            peopleFeed = null;
                        }
                    }

                }

                // When the next page token is null, there are no additional pages of
                // results. If this is the case, break.
                if (circleFeed.getNextPageToken() != null) {
                    // Prepare the next page of results
                    listCircles.setPageToken(circleFeed.getNextPageToken());

                    // Execute and process the next page request
                    circleFeed = listCircles.execute();
                    circles = circleFeed.getItems();
                } else {
                    circles = null;
                }
            }
        }catch (com.google.api.client.googleapis.json.GoogleJsonResponseException e){
            log.info("The user has no google plus. [Google excpt: ${e.getMessage()}]" )
        }catch (Throwable t){
            log.info("The user has not circles or was a problem loading his circles. ")
        }
        return mapCircles;
    }

    private void loadContactUsingGData(KuorumUser user, Credential credential){

//        Map circleMap = [:]
        Map circleMap = loadCirclesMappedByName(credential)
        credential = refreshCredential(credential)
        executor.execute{
            ContactsService contactsService = new ContactsService(APPLICATION_NAME)
            contactsService.setOAuth2Credentials(credential);
            URL feedUrl = new URL(URL_LOAD_ALL_CONTACTS);
            Long numContacts = 0;
            List<ContactRDTO> contactRDTOs = []
            while (true){
                ContactFeed resultFeed = contactsService.getFeed(feedUrl, ContactFeed.class);
                contactRDTOs =  processContactsFeed(resultFeed,circleMap);
                if (contactRDTOs.size() > MAX_BULK_CONTACTS){
                    contactService.addBulkContacts(user, contactRDTOs);
                    contactRDTOs.clear()
                    log.info("Imported ${numContacts} [${user.alias}]")
                }
                numContacts += contactRDTOs?.size()?:0
                try{
                    if (resultFeed.nextLink?.href){
                        feedUrl = new URL(resultFeed.nextLink.href)
                    }else{
                        log.info("End contacts: "+numContacts)
                        break
                    }
                }catch (Exception e){
                    log.info("End contacts due to exception: "+numContacts +" => Excp:"+e.getLocalizedMessage())
                    break;
                }
                if (credential.refreshToken()){
                    credential = refreshCredential(credential)
                }
            }
            contactService.addBulkContacts(user, contactRDTOs);
            contactRDTOs.clear()
            log.info("Imported ${numContacts} [${user.alias}]")
        }
    }

    private Credential refreshCredential(Credential credential){
        try {
            String refreshToken = credential.getRefreshToken()?:credential.accessToken
            TokenResponse tokenResponse =
                    new GoogleRefreshTokenRequest(
                            HTTP_TRANSPORT,
                            JSON_FACTORY,
                            refreshToken,
                            grailsApplication.config.oauth.providers.google.key,
                            grailsApplication.config.oauth.providers.google.secret).execute();
            return createCredentialFromToken(tokenResponse, "")
        } catch (TokenResponseException e) {
            log.warn("No refreshed token due to exception: ${e.getMessage()}", e)
            return null;
        }
    }

    private List<ContactRDTO> processContactsFeed(ContactFeed resultFeed, Map<String, List<String>> circleMap){
        List<ContactRDTO> contactRDTOs = new ArrayList<>();
        for (ContactEntry entry : resultFeed.getEntries()) {
            contactRDTOs.addAll(createContactRDTO(entry, circleMap))
        }
        contactRDTOs;
    }

    // A gmail user has more than one email
    private List<ContactRDTO> createContactRDTO(ContactEntry contactEntry, Map<String, Set<String>> circleMap){
        List<ContactRDTO> contacts = new ArrayList<>()
        if (contactEntry.hasEmailAddresses()){
            contactEntry.emailAddresses.address.each {email ->
                ContactRDTO contactRDTO = null;
                String name = contactEntry.title.plainText
                contactRDTO = new ContactRDTO(name:name, email:email)
                contactRDTO.tags = []
                if (contactEntry.hasGender()){
                    contactRDTO.tags.add(contactEntry.gender.toString())
                }
                if (contactEntry.hasLanguages()){
                    contactEntry.languages.findAll{it.label}.each {lang ->
                        contactRDTO.tags.add(lang.label)
                    }
                }
                if (contactEntry.hasHobbies()){
                    contactEntry.hobbies.findAll{it.value}.each {hobby->
                        contactRDTO.tags.add(hobby.getValue())
                    }
                }
                if (contactEntry.hasOrganizations()){
                    contactEntry.organizations.findAll{it.orgName}.each {organization->
                        contactRDTO.tags.add(organization.getOrgName().value)
                    }
                }
                String mapName = contactEntry.getName()?.fullName?.value
                if (circleMap.containsKey(mapName)){
                    contactRDTO.tags.addAll(circleMap.get(mapName))
                }
                contacts.add(contactRDTO)
            }
        }

        return contacts;
    }

    private AuthorizationCodeFlow googleAuthorizationFlow(String urlCallback) throws IOException {


        // Build googleAuthorizationFlow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY,getGoogleClientSecrets(urlCallback) , SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        return flow;
    }


    private GoogleClientSecrets getGoogleClientSecrets(String urlCallBack){
        GoogleClientSecrets.Details webDetails = new GoogleClientSecrets.Details();
        webDetails.setAuthUri("https://accounts.google.com/o/oauth2/auth")
        webDetails.setClientId(grailsApplication.config.oauth.providers.google.key)
        webDetails.setClientSecret(grailsApplication.config.oauth.providers.google.secret)
        webDetails.setRedirectUris([urlCallBack])
        webDetails.set("auth_provider_x509_cert_url","https://www.googleapis.com/oauth2/v1/certs")
        webDetails.set("project_id","kuorum-social-1")
        webDetails.setTokenUri("https://accounts.google.com/o/oauth2/token")
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(webDetails)

//        File file = new File ("/home/iduetxe/Descargas/client_secret_426876445725-06qsr9vdj5r10a7k9h7tlcb04oomfmub.apps.googleusercontent.com.json")
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new FileReader(file));
        return clientSecrets;
    }

}
