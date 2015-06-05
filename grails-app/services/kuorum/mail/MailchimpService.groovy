package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import com.ecwid.mailchimp.MailChimpException
import com.ecwid.mailchimp.method.v2_0.lists.Email
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod
import com.mongodb.BasicDBObject
import com.mongodb.DBCursor
import com.mongodb.DBObject
import grails.transaction.Transactional
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

    private static final MAILCHIMP_DATE_FORMAT = "yyyy-MM-dd"

    def updateAllUsers(){
        DBObject query = new BasicDBObject('enabled', true)
        DBCursor cursor = KuorumUser.collection.find(query)
        cursor.each {
            KuorumUser user = KuorumUser.get(it._id)
            addSubscriber(user)
        }
    }

    def addSubscriber(KuorumUser user){
// reuse the same MailChimpClient object whenever possible
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
        mergeVars.PROVINCE = user.personalData.provinceCode
        if (user.userType != UserType.ORGANIZATION){
            mergeVars.STUDIES = user.personalData.studies
            mergeVars.WORKINGSEC = user.personalData.workingSector
        }
        mergeVars.USERTYPE = user.userType.toString()
        mergeVars.mc_language = user.language.locale.toString()
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
}
