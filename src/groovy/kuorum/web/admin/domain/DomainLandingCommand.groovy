package kuorum.web.admin.domain

import grails.validation.Validateable

@Validateable
class DomainLandingCommand {

    String slogan;
    String subtitle;
    String domainDescription;

    static constraints = {
        slogan nullable:false
        subtitle nullable:false
        domainDescription nullable:true
    }
}
