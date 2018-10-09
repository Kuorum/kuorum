package kuorum.web.admin.domain

import grails.validation.Validateable
import kuorum.web.commands.LinkCommand

@Validateable
class DomainLandingCommand {

    String slogan;
    String subtitle;
    String domainDescription;

    List<LinkCommand> footerLinks

    static constraints = {
        slogan nullable:false
        subtitle nullable:false
        domainDescription nullable:true
        footerLinks minSize: 0, maxSize: 10
    }
}
