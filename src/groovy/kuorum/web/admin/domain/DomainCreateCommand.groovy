package kuorum.web.admin.domain

import grails.validation.Validateable

@Validateable
class DomainCreateCommand {

    String name;

    static constraints = {
        name nullable: true
    }
}
