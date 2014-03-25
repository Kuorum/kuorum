package kuorum

import grails.plugin.springsecurity.annotation.Secured

class NotificationController {

    def index() {}

    def springSecurityService
    def notificationService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def userAlert(){

    }
}
