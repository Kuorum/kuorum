package kuorum.editor

import grails.plugin.springsecurity.annotation.Secured
import kuorum.causes.CausesService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PoliticianService
import kuorum.web.commands.profile.politician.*
import org.kuorum.rest.model.tag.CauseRSDTO

@Secured(['ROLE_EDITOR'])
class EditorPoliticianController {
    PoliticianService politicianService

    KuorumUserService kuorumUserService
    CausesService causesService

    def editNews(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        [command:new RelevantEventsCommand(politician:politician, politicianRelevantEvents: politician.relevantEvents?.reverse()?:[])]
    }

    def updateNews(RelevantEventsCommand command){
        command.politicianRelevantEvents = command.politicianRelevantEvents.grep()
        command.validate()
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editNews", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(politician, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditNews', params: politician.encodeAsLinkProperties()
    }

    def editProfessionalDetails(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        ProfessionalDetailsCommand command = new ProfessionalDetailsCommand(politician)
        [command:command]
    }

    def updateProfessionalDetails(ProfessionalDetailsCommand command){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editProfessionalDetails", model:[command:command]
            return;
        }
        politicianService.updatePoliticianProfessionalDetails(politician, command)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianProfessionalDetails', params: command.politician.encodeAsLinkProperties()
    }


    def editQuickNotes(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        QuickNotesCommand command = new QuickNotesCommand(politician)
        [command:command]
    }

    def updateQuickNotes(QuickNotesCommand command){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editQuickNotes", model:[command:command]
            return;
        }
        politicianService.updatePoliticianQuickNotes(politician, command.politicianExtraInfo, command.institutionalOffice, command.politicalOffice)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianQuickNotes', params: command.politician.encodeAsLinkProperties()
    }


    def editCauses(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        List<CauseRSDTO> causes = causesService.findSupportedCauses(politician)
        PoliticianCausesCommand command = new PoliticianCausesCommand(politician, causes.collect{it.name})
        [command:command]
    }

    def updateCauses(PoliticianCausesCommand command){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editCauses", model:[command:command]
            return;
        }
        politicianService.updatePoliticianCauses(politician, command.causes)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianCauses', params: command.politician.encodeAsLinkProperties()
    }

}
