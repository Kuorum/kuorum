package kuorum.mail

import com.ecwid.mailchimp.MailChimpClient
import com.ecwid.mailchimp.MailChimpException
import com.ecwid.mailchimp.method.v2_0.lists.Email
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod
import grails.transaction.Transactional
import kuorum.users.KuorumUser
import org.springframework.beans.factory.annotation.Value

@Transactional
class MailchimpService {

    @Value('${mail.mailChimp.key}')
    String MAILCHIMP_APIKEY

    @Value('${mail.mailChimp.listId}')
    String MAILCHIMP_LIST_ID

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
            subscribeMethod.merge_vars = new MailChimpMergeVars(user.email, user.name, user?.surname);
            mailChimpClient.execute(subscribeMethod);

            log.info(" Se ha añadido correctamente el usuario $user con mail $user.email a MailChimp");
            mailChimpClient.close();
        }catch(MailChimpException mailChimpException){
            log.error("No se ha podido añadir al usuario ${user.email} a mailchimp debido a que MailChimp se ha negado",mailChimpException)
        }catch(Exception e){
            log.error("No se ha podido añadir al usuario ${user.email} a mailchimp debido a una excepcion",e)
        }

    }
}
