package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.kuorumUser.LanguageRSDTO

@Validateable
class DomainValidationCommand {

    PrivateContentType privateContent
    Boolean validationCensus
    Boolean validationPhone
    Boolean validationCode
    String smsDomainName
    String defaultPhonePrefix

    static constraints = {
        privateContent nullable: false
        validationCensus nullable: true
        validationPhone nullable: true
        validationCode nullable: true
        smsDomainName nullable: true, matches: '[a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*', maxSize: 11
        defaultPhonePrefix nullable: true
    }

    public static final enum PrivateContentType{
        PUBLIC(false), PRIVATE(true)

        Boolean isPrivate;
        public PrivateContentType(Boolean isPrivate){
            this.isPrivate = isPrivate;
        }
        public static getPrivateContent(Boolean privateContent){
            return privateContent?PRIVATE:PUBLIC
        }

        Boolean getIsPrivate() {
            return isPrivate
        }
    }
}
