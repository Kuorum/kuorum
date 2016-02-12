package kuorum.editor

import grails.plugin.springsecurity.annotation.Secured
import kuorum.RegionService
import kuorum.core.model.UserType
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.PoliticianService
import kuorum.web.commands.editor.EditorAccountCommand
import kuorum.web.commands.editor.EditorCreateUserCommand
import kuorum.web.commands.profile.politician.*

@Secured(['ROLE_EDITOR'])
class EditorPoliticianController {
    PoliticianService politicianService

    def editExternalActivity(){
        KuorumUser politician = KuorumUser.get(params.id)
        [command:new ExternalPoliticianActivityCommand(politician:politician, externalPoliticianActivities: politician.externalPoliticianActivities)]
    }

    def updateExternalActivity(ExternalPoliticianActivityCommand command){
        if (command.hasErrors()){
            render view:"editExternalActivity", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExternalActivity(command.politician, command.externalPoliticianActivities)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianExternalActivity', params: command.politician.encodeAsLinkProperties()
    }

    def editRelevantEvents(){
        KuorumUser politician = KuorumUser.get(params.id)
        [command:new RelevantEventsCommand(politician:politician, politicianRelevantEvents: politician.relevantEvents?.reverse()?:[])]
    }

    def updateRelevantEvents(RelevantEventsCommand command){
        if (command.hasErrors()){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(command.politician, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianRelevantEvents', params: command.politician.encodeAsLinkProperties()
    }

    def editProfessionalDetails(){
        KuorumUser politician = KuorumUser.get(params.id)
        ProfessionalDetailsCommand command = new ProfessionalDetailsCommand(politician)
        [command:command]
    }

    def updateProfessionalDetails(ProfessionalDetailsCommand command){
        if (command.hasErrors()){
            render view:"editProfessionalDetails", model:[command:command]
            return;
        }
        politicianService.updatePoliticianProfessionalDetails(command.politician, command)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianProfessionalDetails', params: command.politician.encodeAsLinkProperties()
    }


    def editQuickNotes(){
        KuorumUser politician = KuorumUser.get(params.id)
        QuickNotesCommand command = new QuickNotesCommand(politician)
        [command:command]
    }

    def updateQuickNotes(QuickNotesCommand command){
        if (command.hasErrors()){
            render view:"editQuickNotes", model:[command:command]
            return;
        }
        politicianService.updatePoliticianQuickNotes(command.politician, command.politicianExtraInfo, command.institutionalOffice, command.politicalOffice)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianQuickNotes', params: command.politician.encodeAsLinkProperties()
    }


    def editCauses(){
        KuorumUser politician = KuorumUser.get(params.id)
        PoliticianCausesCommand command = new PoliticianCausesCommand(politician)
        [command:command]
    }

    def updateCauses(PoliticianCausesCommand command){
        if (command.hasErrors()){
            render view:"editCauses", model:[command:command]
            return;
        }
        politicianService.updatePoliticianCauses(command.politician, command.causes)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianCauses', params: command.politician.encodeAsLinkProperties()
    }


    def editPoliticalExperience(){
        KuorumUser politician = KuorumUser.get(params.id)
        PoliticalExperienceCommand command = new PoliticalExperienceCommand(politician)
        [command:command]
    }

    def updatePoliticalExperience(PoliticalExperienceCommand command){
        if (command.hasErrors()){
            render view:"editPoliticalExperience", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExperience(command.politician, command.timeLine)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianExperience', params: command.politician.encodeAsLinkProperties()
    }

}
