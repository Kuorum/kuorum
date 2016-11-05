package kuorum.editor

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.EditorRules
import kuorum.users.EditorService
import kuorum.users.KuorumUser

class EditorRecruitmentController {

    SpringSecurityService springSecurityService
    EditorService editorService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def requestEditor() {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        KuorumUser user = editorService.requestEditorRights(loggedUser)

        if (!user || user.hasErrors()){
            flash.error=g.message(code: 'modules.ipdbRecruitment.buttons.yes.error')
        }else{
            flash.message=g.message(code: 'modules.ipdbRecruitment.buttons.yes.success')
        }
        redirect mapping:"dashboard"
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def discardEditor() {
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        KuorumUser user = editorService.discardEditorWarns(loggedUser)

        if (!user || user.hasErrors()){
            flash.error=g.message(code: 'modules.ipdbRecruitment.buttons.neverAsk.error')
        }else{
            flash.message=g.message(code: 'modules.ipdbRecruitment.buttons.neverAsk.success')
        }
        redirect mapping:"dashboard"
    }
}
