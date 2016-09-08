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

    def editExternalActivity(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        [command:new ExternalPoliticianActivityCommand(politician:politician, externalPoliticianActivities: politician.externalPoliticianActivities)]
    }

    def updateExternalActivity(ExternalPoliticianActivityCommand command){
        command.externalPoliticianActivities = command.externalPoliticianActivities.grep()
        command.validate()
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editExternalActivity", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExternalActivity(politician, command.externalPoliticianActivities)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianExternalActivity', params: politician.encodeAsLinkProperties()
    }

    def editRelevantEvents(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        [command:new RelevantEventsCommand(politician:politician, politicianRelevantEvents: politician.relevantEvents?.reverse()?:[])]
    }

    def updateRelevantEvents(RelevantEventsCommand command){
        command.politicianRelevantEvents = command.politicianRelevantEvents.grep()
        command.validate()
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(politician, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianRelevantEvents', params: politician.encodeAsLinkProperties()
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
        List<CauseRSDTO> causes = causesService.findDefendedCauses(politician)
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


    def editPoliticalExperience(){
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        PoliticalExperienceCommand command = new PoliticalExperienceCommand(politician)
        [command:command]
    }

    def updatePoliticalExperience(PoliticalExperienceCommand command){
        command.timeLine = command.timeLine.grep()
        command.validate()
        KuorumUser politician = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editPoliticalExperience", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExperience(politician, command.timeLine)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'editorEditPoliticianExperience', params: command.politician.encodeAsLinkProperties()
    }

}
