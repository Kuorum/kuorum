package kuorum

import kuorum.core.model.search.Pagination
import kuorum.dashboard.DashboardService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.web.commands.profile.PersonalDataCommand

class DataProfileIncompleteTagLib {
    def springSecurityService
    DashboardService dashboardService
    KuorumUserService kuorumUserService

    static defaultEncodeAs = 'raw'

    static namespace = "showNoticesDataIncomplete"

    def showWarningsDataProfileIncomplete = {attrs ->
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Map orderedNotice = dashboardService.showNotice(user, request.locale)
        PersonalDataCommand personalDataCommand = new PersonalDataCommand()
        personalDataCommand.country = user.personalData?.country
        personalDataCommand.gender = user.personalData?.gender
        personalDataCommand.postalCode = user.personalData?.postalCode
        personalDataCommand.telephone = user.personalData?.telephone
        personalDataCommand.year = user.personalData?.year
        if(orderedNotice)
            out << g.render(template:'/profile/userIncompleteData', model:[user:user, noticeType: orderedNotice.noticeType, orderedNotice:orderedNotice.notice, errors: orderedNotice.errors, personalDataCommand: personalDataCommand])
    }
}
