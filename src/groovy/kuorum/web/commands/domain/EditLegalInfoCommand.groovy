package kuorum.web.admin.domain

import grails.validation.Validateable

@Validateable
class EditLegalInfoCommand {

    String address;
    String city;
    String country;
    String domainOwner;
    String fileName;
    String filePurpose;
    String fileResponsibleEmail;
    String fileResponsibleName;

        static constraints = {
            address nullable: true
            city nullable: true
            country nullable: true
            domainOwner nullable:true
            fileName nullable:true
            filePurpose nullable: true
            fileResponsibleEmail nullable: true
            fileResponsibleName nullable: true
    }
}
