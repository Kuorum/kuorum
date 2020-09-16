package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat
import org.kuorum.rest.model.contact.ContactLanguageRDTO
import org.kuorum.rest.model.contact.GenderRDTO

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class ContactCommand {
    Long contactId;
    String name;
    String surname;
    String externalId;
    String email;
    String phonePrefix;
    String phone;
    Date birthDate;
    GenderRDTO gender;

    @BindingFormat(".")
    Double surveyVoteWeight;
    @BindUsing({obj, org.grails.databinding.DataBindingSource source->
        ContactLanguageRDTO.getContactLanguage(source["language"])
    })
    ContactLanguageRDTO language;

    static constraints = {
        contactId nullable: false
        name nullable: false
        surname nullable: true
        externalId nullable: true
        language nullable: true
        phonePrefix nullable: true
        phone nullable: true
        email nullable: true, email: true // Is nullable because when the contact is a follower we don't have the email
        surveyVoteWeight nullable: false, min: new Double(0), scale: 2
        birthDate nullable: true
        gender nullable: true

    }
}
