package kuorum

import kuorum.dashboard.DashboardService
import kuorum.users.KuorumUser

class DataProfileIncompleteTagLib {
    def springSecurityService
    DashboardService dashboardService

    static defaultEncodeAs = 'raw'

    static namespace = "showNoticesDataIncomplete"

    def showWarningsDataProfileIncomplete = {attrs ->
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map orderedNotice = dashboardService.showNotice(user, request.locale)
        out << g.render(template:'/profile/userIncompleteData', model:[orderedNotice:orderedNotice.notice, errors: orderedNotice.errors])
    }
}
