package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.kuorum.rest.model.contact.ContactIssueTypeDTO

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class ContactIssueCommand {
    String resolver;
    String note;
    ContactIssueTypeDTO issueType;

    static constraints = {
        resolver nullable: false
        note nullable: false
        issueType nullable: false
    }
}
