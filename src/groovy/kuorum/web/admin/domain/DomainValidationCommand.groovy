package kuorum.web.admin.domain

import grails.validation.Validateable
import org.kuorum.rest.model.domain.DomainPrivacyRDTO
import org.kuorum.rest.model.domain.DomainValidationRDTO

@Validateable
class DomainValidationCommand {

    DomainPrivacyRDTO domainPrivacy
    DomainValidationRDTO firstFactorValidation
    String externalIdName
    Boolean validationCensus
    Boolean validationPhone
    Boolean validationCode
    Boolean validationTokenMail
    String smsDomainName
    String defaultPhonePrefix

    Boolean isSocialNetwork
    Boolean isUserProfileExtended
    Boolean tourEnabled
    Boolean showRegisterButton

    Boolean providerBasicEmailForm
    Boolean providerGoogle
    Boolean providerFacebook
    Boolean providerAoc

    static constraints = {
        domainPrivacy nullable: true
        firstFactorValidation nullable: true
        externalIdName nullable: true
        validationCensus nullable: true
        validationPhone nullable: true
        validationCode nullable: true
        validationTokenMail nullable: true
        isSocialNetwork nullable: true
        isUserProfileExtended nullable: true
        tourEnabled nullable: true
        showRegisterButton nullable: true
        smsDomainName nullable: true, matches: '[a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*', maxSize: 11
        defaultPhonePrefix nullable: true
        providerBasicEmailForm nullable: true
        providerGoogle nullable: true
        providerFacebook nullable: true
        providerAoc nullable: true
    }
}
