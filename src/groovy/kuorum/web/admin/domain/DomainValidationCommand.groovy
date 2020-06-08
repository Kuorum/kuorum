package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.kuorumUser.LanguageRSDTO

@Validateable
class DomainValidationCommand {

    Boolean validationCensus
    Boolean validationPhone
    Boolean validationCode
    String smsDomainName;

    static constraints = {
        validationCensus nullable: true
        validationPhone nullable: true
        validationCode nullable: true
        smsDomainName nullable: true, matches: '[a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*', maxSize: 11
    }
}
