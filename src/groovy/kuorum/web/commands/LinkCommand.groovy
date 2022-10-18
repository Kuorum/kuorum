package kuorum.web.commands

import grails.validation.Validateable

@Validateable
class LinkCommand {
    String title;
    String url;

    static constraints = {
        title nullable:false
        url nullable: true
    }
}
