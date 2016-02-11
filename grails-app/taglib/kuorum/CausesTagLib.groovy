package kuorum

import kuorum.core.model.UserType
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
        if (springSecurityService.isLoggedIn()){
            SupportedCauseRSDTO supportedCauseRSDTO = causesService.statusCause(springSecurityService.currentUser, cause.name)
            if (supportedCauseRSDTO.supported){
                causeSupportClass = "active"
            }
        }else{
            causeSupportClass = "noLogged"
        }
        String searchLink = g.createLink(mapping:"searcherSearch", params:[type:UserType.POLITICIAN, word:cause.name])
        String supportCause = g.createLink(mapping:"causeSupport", params:[causeName:cause.name], absolute: true)
        out << """

                <li class="cause link-wrapper ${causeSupportClass}">
                    <a href='${searchLink}' class="hidden"> Search cause ${cause.name}</a>
                    <div class="cause-name">
                        <span class="fa fa-tag"></span>
                        ${cause.name}
                    </div>
                    <div class="cause-support">
                        <a href='${supportCause}'>
                            <span class="fa fa-heart"></span>
                            <span class="fa fa-heart-o"></span>
                            <span class="cause-counter">${cause.citizenVotes}</span>
                        </a>
                    </div>
                </li>

"""
    }
}
