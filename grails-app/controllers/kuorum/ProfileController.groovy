package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ProfileController {

    def springSecurityService

    def editUser() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }
    def changePassword() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def changeEmail() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def socialNetworks() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def configurationEmails() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def showFavoritesPosts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def showUserPosts() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def kuorumStore() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def userNotifications() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

    def userMessages() {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        [user:user]
    }

}
