package kuorum.web.admin.domain

import grails.validation.Validateable

@Validateable
class EditLegalInfoCommand {

    String address;
    String city;
    String country;
    String domainName;
    String domainOwner;
    String fileName;
    String filePurpose;
    String fileResponsibleEmail;
    String fileResponsibleName;

        static constraints = {
            address nullable: false
            city nullable: false
            country nullable: false
            domainName nullable:false
            domainOwner nullable:false
            fileName nullable:false
            filePurpose nullable: false
            fileResponsibleEmail nullable: false
            fileResponsibleName nullable: false
    }
}
