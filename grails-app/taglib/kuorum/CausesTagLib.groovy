package kuorum

import kuorum.core.model.UserType
import kuorum.core.model.search.Pagination
import kuorum.core.model.search.SearchType
import kuorum.users.KuorumUser
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO
import org.kuorum.rest.model.tag.UsersSupportingCauseRSDTO

class CausesTagLib {
    static defaultEncodeAs = [taglib: 'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    def causesService
    def springSecurityService

    static namespace = "cause"

    def show = {attrs ->
        CauseRSDTO cause = attrs.cause

        String causeSupportClass = ""
        String ariaPressed = "false"
        if (springSecurityService.isLoggedIn()){
            KuorumUser userLogged = KuorumUser.get(springSecurityService.principal.id)
            SupportedCauseRSDTO supportedCauseRSDTO = causesService.statusCause(userLogged, cause.name)
            if (supportedCauseRSDTO.supported){
                causeSupportClass = "active"
                ariaPressed = "true"
            }
        }else{
            causeSupportClass = "noLogged"
        }
        String searchLink = g.createLink(mapping:"searcherSearch", params:[type:UserType.POLITICIAN, word:cause.name, searchType:SearchType.CAUSE])
        String supportCause = g.createLink(mapping:"causeSupport", params:[causeName:cause.name], absolute: true)

        String showCounter = ""
        if (springSecurityService.isLoggedIn()){
            showCounter = """
                            <span class="sr-only">Cause support counter:</span>
                            <span class="cause-counter">${cause.citizenSupports}</span>
                          """
        }

        out << """
                <li class="cause link-wrapper ${causeSupportClass}" id="cause-${cause.name.encodeAsKuorumUrl()}">
                    <a href='${searchLink}' class="sr-only hidden"> Search cause ${cause.name}</a>
                    <div class="cause-name" aria-hidden="true" tabindex="104">
                        <span class="fa fa-tag"></span>
                        <span>${cause.name}</span>
                    </div>
                    <div class="cause-support">
                        <a href='${supportCause}' role="button" tabindex="105" aria-pressed="${ariaPressed}" aria-labelledby="cause-support cause-counter">
                            <span class="fa fa-heart"></span>
                            <span class="fa fa-heart-o"></span>
                            ${showCounter}
                        </a>
                    </div>
                </li>

"""
    }

    def card = {attrs ->
        CauseRSDTO cause = attrs.cause

        UsersSupportingCauseRSDTO politiciansPage = causesService.mostRelevantDefenders(cause.name, new Pagination(max:4))
        UsersSupportingCauseRSDTO citizensPage = causesService.mostRelevantSupporters(cause.name, new Pagination(max:4))

        List<KuorumUser> politicians = politiciansPage.data.collect{KuorumUser.get(it.id)}
        List<KuorumUser> citizens = citizensPage.data.collect{KuorumUser.get(it.id)}

        out << g.render(
                template:"/dashboard/dashboardModules/causeCard",
                model:[
                        cause:cause,
                        mainPolitician:politicians?politicians.get(0):null,
                        politcians:politicians,
                        citizens:citizens,
                        total:politiciansPage.total
                ])

    }
}
