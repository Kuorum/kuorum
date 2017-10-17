package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import com.ecwid.mailchimp.MailChimpException
import com.ecwid.mailchimp.method.v2_0.lists.Email
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod
import com.mongodb.BasicDBObject
import com.mongodb.DBCursor
import com.mongodb.DBObject
import grails.transaction.Transactional
import kuorum.RegionService
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import org.springframework.beans.factory.annotation.Value

@Transactional
class MailchimpService {

    @Value('${mail.mailChimp.key}')
    String MAILCHIMP_APIKEY

    @Value('${mail.mailChimp.listId}')
    String MAILCHIMP_LIST_ID

    @Value('${mail.mailChimp.listCaseStudyId}')
    String MAILCHIMP_CASE_STUDY_LIST_ID

    private static final MAILCHIMP_DATE_FORMAT = "dd-MM-yyyy"

    KuorumMailService kuorumMailService;

    RegionService regionService

    def updateAllUsers(KuorumUser executorUser){

        Thread.start {
            String politiciansOk = "<h1>Politician OK</h1>"
            String politiciansWrong = "<h1>Politician Wrong</h1><UL>"

            DBObject query = new BasicDBObject('enabled', true)
            DBCursor cursor = KuorumUser.collection.find(query)
            cursor.addOption(com.mongodb.Bytes.QUERYOPTION_NOTIMEOUT)
            Long usersOk= 0L;
            Long usersError= 0L;

            cursor.each {
                KuorumUser user = KuorumUser.get(it._id)
                try{
                    log.info("Updating mailchimp ${user.name}")
                    addSubscriber(user)
                    usersOk++
                } catch(Exception e) {
                    politiciansWrong +=  "<li>${user.name} (${user.id}): <i>${e.getMessage()}</i></li>"
                    usersError++
                }

            }
            log.info("Enviando mail de fin de procesado de email a ${executorUser.name} (${executorUser.email})")
            politiciansOk += "<br/> Counter ${usersOk}"
            politiciansWrong += "<br/> Counter ${usersError}</UL>"
            log.info("Enviando mail de fin de procesado de email a ${executorUser.name} (${executorUser.email})")
            kuorumMailService.sendBatchMail(executorUser, politiciansWrong + politiciansOk, "Mailchimp updated")
        }
    }

    def addSubscriber(KuorumUser user){
// reuse the same MailChimpClient object whenever possible
        if (user.email =~ /^[^\+]*+[^@]*@kuorum.org$/){
            log.info("User not updated on mandrillapp because is a fake user [${user.email}]")
            return;
        }
        try {
            MailChimpClient mailChimpClient = new MailChimpClient();

            Email mailChimpEmail = new Email()
            mailChimpEmail.email = user.email

            // Subscribe a person
            SubscribeMethod subscribeMethod = new SubscribeMethod();
            subscribeMethod.apikey = MAILCHIMP_APIKEY;
            subscribeMethod.id = MAILCHIMP_LIST_ID;
            subscribeMethod.email = mailChimpEmail
            subscribeMethod.double_optin = false;
            subscribeMethod.update_existing = true;
            subscribeMethod.send_welcome = false;
            subscribeMethod.replace_interests = true;
            subscribeMethod.merge_vars = createMergeVars(user)
            def userMailChimpId = mailChimpClient.execute(subscribeMethod);

            log.info(" Se ha añadido correctamente el usuario $user con mail $user.email a MailChimp");
            mailChimpClient.close();
        }catch(MailChimpException mailChimpException){
            log.error("No se ha podido añadir al usuario ${user.email} a mailchimp debido a que MailChimp se ha negado",mailChimpException)
        }catch(Exception e){
            log.error("No se ha podido añadir al usuario ${user.email} a mailchimp debido a una excepcion",e)
        }

    }

    private MailChimpMergeVars createMergeVars(KuorumUser user){
        MailChimpMergeVars mergeVars = new MailChimpMergeVars();
        mergeVars.POSTALCODE = user.personalData.postalCode
        mergeVars.BIRTHDAY = user.personalData.birthday?.format(MAILCHIMP_DATE_FORMAT)
        mergeVars.EMAIL = user.email
        mergeVars.FNAME = user.name
        mergeVars.LNAME = user.name
        mergeVars.GENDER = user.personalData.gender.toString()
        if (user.personalData?.provinceCode){
            mergeVars.LOCATION_C = user.personalData.province?.iso3166_2
            mergeVars.COUNTRY_C = user.personalData.country?.iso3166_2
        }
        if (user.userType != UserType.ORGANIZATION){
            mergeVars.STUDIES = user.personalData.studies
            mergeVars.WORKINGSEC = user.personalData.workingSector
        }
        if (user.userType == UserType.POLITICIAN && user?.professionalDetails?.region?.iso3166_2){
            mergeVars.LOCATION_C = user.professionalDetails.region.iso3166_2
            mergeVars.COUNTRY_C = regionService.findCountry(user?.professionalDetails?.region)
        }
        mergeVars.USERTYPE = user.userType.toString()
        mergeVars.mc_language = mailChimpLang(user.language)
        mergeVars.groupings = createGroups(user)
        mergeVars
    }

    private List<MailChimpGroup> createGroups(KuorumUser user){
        List<String> relevantCommissions = []
        //NO use collect for fucking lazy evaluation and send all empty
        for (CommissionType commissionType : user.relevantCommissions){
            relevantCommissions.add(commissionType.toString())
        }
        [new MailChimpGroup( name:"relevantCommissions", groups:relevantCommissions)]
    }

    public void addCaseStudy(String name, String email, Locale locale){
        addToMailChimpList(name, email, locale,MAILCHIMP_CASE_STUDY_LIST_ID )
    }

    private void addToMailChimpList(String name, String email, Locale locale, String listId){
        try {
            MailChimpClient mailChimpClient = new MailChimpClient();

            Email mailChimpEmail = new Email()
            mailChimpEmail.email = email

            // Subscribe a person
            SubscribeMethod subscribeMethod = new SubscribeMethod();
            subscribeMethod.apikey = MAILCHIMP_APIKEY;
            subscribeMethod.id = listId;
            subscribeMethod.email = mailChimpEmail
            subscribeMethod.double_optin = false;
            subscribeMethod.update_existing = true;
            subscribeMethod.send_welcome = false;
            subscribeMethod.replace_interests = true;

            MailChimpMergeVars mergeVars = new MailChimpMergeVars();
            mergeVars.mc_language = mailChimpLang(locale)
            mergeVars.FNAME = name
            subscribeMethod.merge_vars = mergeVars
            def userMailChimpId = mailChimpClient.execute(subscribeMethod);

            log.info(" Se ha añadido correctamente el usuario email $email a MailChimp");
            mailChimpClient.close();
        }catch(MailChimpException mailChimpException){
            log.error("No se ha podido añadir al usuario ${email} a mailchimp debido a que MailChimp se ha negado",mailChimpException)
        }catch(Exception e){
            log.error("No se ha podido añadir al usuario ${email} a mailchimp debido a una excepcion",e)
        }
    }

    private String mailChimpLang(AvailableLanguage lang){
        mailChimpLang(lang.locale)
    }
    private String mailChimpLang(Locale locale){
        if (locale.getLanguage().equals("es")) {
            return "es_ES" // It is necesary to add the countre as "es_ES" because only "es" is spanish from Mexico
        } else {
            return locale.language.toString()
        }
    }
}
