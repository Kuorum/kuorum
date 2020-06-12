package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.kuorum.rest.model.contact.ContactLanguageRDTO

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class ContactCommand {
    Long contactId;
    String name;
    String surname;
    String email;
    String phonePrefix;
    String phone;
    @BindUsing({obj, org.grails.databinding.DataBindingSource source->
        ContactLanguageRDTO.getContactLanguage(source["language"])
    })
    ContactLanguageRDTO language;

    static constraints = {
        contactId nullable: false
        name nullable: false
        surname nullable: true
        language nullable: true
        phonePrefix nullable: true
        phone nullable: true
        email nullable: true, email: true // Is nullable because when the contact is a follower we don't have the email
    }
}
