package kuorum.causes

import grails.plugin.springsecurity.SpringSecurityService

class CausesController {

    CausesService causeService

    SpringSecurityService springSecurityService;

    def supportCause(String causeName) {
        render "3"
    }
}
