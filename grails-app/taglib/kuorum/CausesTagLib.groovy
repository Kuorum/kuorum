package kuorum

import kuorum.users.KuorumUser
import org.kuorum.rest.model.tag.CauseRSDTO
import org.kuorum.rest.model.tag.SupportedCauseRSDTO

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

        String searchLink = g.createLink(mapping:"searcherSearchByCAUSE", params:[word:cause.name])
        String supportCause = g.createLink(mapping:"causeSupport", params:[causeName:cause.name], absolute: true)

        String showCounter = ""
        if (springSecurityService.isLoggedIn()) {
            String counterString = cause.citizenSupports
            if (cause.citizenSupports >= 1000) {
                counterString = Math.round((cause.citizenSupports/1000) * 10) / 10 + "k"
            }
            showCounter = """
                            <span class="sr-only">Cause support counter:</span>
                            <span class="cause-counter">${counterString}</span>
                          """
        }

        out << """
                <li class="cause link-wrapper ${causeSupportClass}" id="cause-${cause.name.encodeAsKuorumUrl()}">
                    <a href='${searchLink}' class="sr-only hidden"> Search cause ${cause.name}</a>
                    <div class="cause-name" aria-hidden="true">
                        <span class="fal fa-hashtag"></span>
                        <span>${cause.name}</span>
                    </div>
                    <div class="cause-support">
                        <a href='${supportCause}' role="button" tabindex="105" aria-pressed="${ariaPressed}" aria-labelledby="cause-support cause-counter">
                            <span class="fas fa-heart"></span>
                            <span class="fal fa-heart"></span>
                            ${showCounter}
                        </a>
                    </div>
                </li>

"""
    }
}
