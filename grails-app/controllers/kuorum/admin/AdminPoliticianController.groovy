package kuorum.admin

import kuorum.users.KuorumUser
import kuorum.users.PoliticianProfileController
import kuorum.users.PoliticianService
import kuorum.users.extendedPoliticianData.CareerDetails
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.politician.ExternalPoliticianActivityCommand
import kuorum.web.commands.profile.politician.PoliticalExperienceCommand
import kuorum.web.commands.profile.politician.PoliticianCausesCommand
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.QuickNotesCommand
import kuorum.web.commands.profile.politician.RelevantEventsCommand

class AdminPoliticianController extends AdminController{

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
        redirect mapping:'adminEditPoliticianExternalActivity', params: command.politician.encodeAsLinkProperties()
    }

    def editRelevantEvents(){
        KuorumUser politician = KuorumUser.get(params.id)
        [command:new RelevantEventsCommand(politician:politician, politicianRelevantEvents: politician.relevantEvents)]
    }

    def updateRelevantEvents(RelevantEventsCommand command){
        if (command.hasErrors()){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(command.politician, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'adminEditPoliticianRelevantEvents', params: command.politician.encodeAsLinkProperties()
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
        redirect mapping:'adminEditPoliticianProfessionalDetails', params: command.politician.encodeAsLinkProperties()
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
        redirect mapping:'adminEditPoliticianQuickNotes', params: command.politician.encodeAsLinkProperties()
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
        redirect mapping:'adminEditPoliticianCauses', params: command.politician.encodeAsLinkProperties()
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
        redirect mapping:'adminEditPoliticianExperience', params: command.politician.encodeAsLinkProperties()
    }

}
