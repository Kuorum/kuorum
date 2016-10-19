package payment.social

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.*
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.people.v1.People
import com.google.api.services.people.v1.PeopleScopes
import com.google.api.services.people.v1.model.ListConnectionsResponse
import com.google.api.services.people.v1.model.Person
import com.google.gdata.client.contacts.ContactsService
import com.google.gdata.data.contacts.ContactEntry
import com.google.gdata.data.contacts.ContactFeed
import grails.converters.JSON

class GoogleContactsController {

    public String CLIENT_ID = "426876445725-06qsr9vdj5r10a7k9h7tlcb04oomfmub.apps.googleusercontent.com"
    public String SECRET = "yNf8b4wSJ0pEFq_-ed6RHa0b"
    public String REDIRECT_URL = "http://localhost:8080/kuorum/googleContacts/auth"

    def index(){
        AuthorizationCodeFlow flow = googleAuthorizationFlow();
        GoogleAuthorizationCodeRequestUrl authUrl = flow.newAuthorizationUrl();
        authUrl.setRedirectUri(REDIRECT_URL)
        String url = authUrl.toURL();
        // https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=426876445725-06qsr9vdj5r10a7k9h7tlcb04oomfmub.apps.googleusercontent.com&redirect_uri=http://localhost:51724/Callback&response_type=code&scope=https://www.googleapis.com/auth/contacts.readonly
//        url = "https://accounts.google.com/o/oauth2/auth?access_type=offline&client_id=426876445725-06qsr9vdj5r10a7k9h7tlcb04oomfmub.apps.googleusercontent.com&redirect_uri=http://127.0.0.1:8080/kuorum/googleContacts/auth&response_type=code&scope=${googleAuthorizationFlow.getScopesAsString()}"
//        redirect(url: url+"?"+googleAuthorizationFlow.getScopesAsString())
//        url = new GoogleBrowserClientRequestUrl(CLIENT_ID,
//                REDIRECT_URL,
//                SCOPES)
//                .setResponseTypes(Arrays.asList("code"))
//                .build()

        redirect(url: url)
    }

    def auth(){
        log.info("AUTH")
        String code = params.code
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, SECRET, code, REDIRECT_URL).execute();
//        Credential credential =new Credential(BearerToken.authorizationHeaderAccessMethod()).setFromTokenResponse(tokenResponse);
        Credential credential = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(new NetHttpTransport())
                .setClientSecrets(getClientSecrets()).build()
                .setFromTokenResponse(tokenResponse);

        ContactsService contactsService = new ContactsService(APPLICATION_NAME)
        contactsService.setOAuth2Credentials(credential);
        URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
        ContactFeed resultFeed = contactsService.getFeed(feedUrl, ContactFeed.class);
        // Print the results
        System.out.println(resultFeed.getTitle().getPlainText());
        for (ContactEntry entry : resultFeed.getEntries()) {
            log.info("${entry.title.plainText}=> ${entry.emailAddresses.address}")
        }

        People peopleService =new People.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        Person person = peopleService.people().get("people/me").execute()
        ListConnectionsResponse response = peopleService.people().connections().list("people/me").execute();
        List<Person> connections = response.getConnections();

//        connections.each{Person p ->
//            if (p.getEmailAddresses()){
//                log.info("===>Contact: ${p.getNames().get(0).givenName} |||  ${p.getEmailAddresses()?.get(0)?.getValue()}")
//            }else{
//                log.info("Contact: ${p.getNames().get(0).givenName} || ${p.getResourceName()}")
//            }
//        }
        render (connections as JSON)

    }

    /** Application name. */
    private static final String APPLICATION_NAME = "Kuorum";

    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/people.googleapis.com-java-quickstart");
    private static final java.io.File DATA_STORE_DIR = new java.io.File("/tmp");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/people.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(PeopleScopes.CONTACTS_READONLY,PeopleScopes.USERINFO_PROFILE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public AuthorizationCodeFlow googleAuthorizationFlow() throws IOException {


        // Build googleAuthorizationFlow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY,getClientSecrets() , SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
//                        .setAccessType("offline")
                        .build();
        return flow;
    }


    private GoogleClientSecrets getClientSecrets(){
        GoogleClientSecrets.Details webDetails = new GoogleClientSecrets.Details();
        webDetails.setAuthUri("https://accounts.google.com/o/oauth2/auth")
        webDetails.setClientId(CLIENT_ID)
        webDetails.setClientSecret(SECRET)
        webDetails.setRedirectUris([REDIRECT_URL])
        webDetails.set("auth_provider_x509_cert_url","https://www.googleapis.com/oauth2/v1/certs")
        webDetails.set("project_id","kuorum-social-1")
        webDetails.setTokenUri("https://accounts.google.com/o/oauth2/token")
//        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(webDetails)

        File file = new File ("/home/iduetxe/Descargas/client_secret_426876445725-06qsr9vdj5r10a7k9h7tlcb04oomfmub.apps.googleusercontent.com.json")

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new FileReader(file));
        return clientSecrets;
    }
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public Credential authorize() throws IOException {

        AuthorizationCodeFlow flow = googleAuthorizationFlow();
        LocalServerReceiver localServerReceiver = (new LocalServerReceiver.Builder())
                .setHost("127.0.0.1")
                .setPort("8080")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, localServerReceiver).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());

        return credential;
    }

    /**
     * Build and return an authorized People client service.
     * @return an authorized People client service
     * @throws IOException
     */
    public People getPeopleService() throws IOException {
        Credential credential = authorize();
        return new People.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
