package kuorum.users

import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.extendedPoliticianData.CareerDetails
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.politician.ExternalPoliticianActivityCommand
import kuorum.web.commands.profile.politician.PoliticalExperienceCommand
import kuorum.web.commands.profile.politician.PoliticianCausesCommand
import kuorum.web.commands.profile.politician.ProfessionalDetailsCommand
import kuorum.web.commands.profile.politician.QuickNotesCommand
import kuorum.web.commands.profile.politician.RelevantEventsCommand

@Secured(['ROLE_ADMIN', 'ROLE_POLITICIAN'])
class PoliticianProfileController extends ProfileController{

    PoliticianService politicianService

    def editExternalActivity() {
        KuorumUser user = params.user

        [command:new ExternalPoliticianActivityCommand(politician:user, externalPoliticianActivities: user.externalPoliticianActivities)]
    }

    def updateExternalActivity(ExternalPoliticianActivityCommand command){
        KuorumUser user = params.user
        command.externalPoliticianActivities = command.externalPoliticianActivities.findAll{it}.reverse()
        if (!command.validate()|| !user ){
            render view:"editExternalActivity", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExternalActivity(params.user, command.externalPoliticianActivities)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianExternalActivity'
    }

    def editRelevantEvents(){
        KuorumUser user = params.user
        [command:new RelevantEventsCommand(politician:user, politicianRelevantEvents: user.relevantEvents?.reverse()?:[])]
    }

    def updateRelevantEvents(RelevantEventsCommand command){
        command.politicianRelevantEvents = command.politicianRelevantEvents.findAll{it}
        KuorumUser user = params.user
        if (!command.validate() || !user ){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianRelevantEvents(params.user, command.politicianRelevantEvents)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianRelevantEvents'
    }

    def editProfessionalDetails(){
        KuorumUser politician = params.user
        ProfessionalDetailsCommand command = new ProfessionalDetailsCommand(politician)
        [command:command]
    }

    def updateProfessionalDetails(ProfessionalDetailsCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editRelevantEvents", model:[command:command]
            return;
        }
        politicianService.updatePoliticianProfessionalDetails(user, command)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianProfessionalDetails', params: user.encodeAsLinkProperties()
    }

    def editQuickNotes(){
        KuorumUser politician = params.user
        QuickNotesCommand command = new QuickNotesCommand(politician)
        [command:command]
    }

    def updateQuickNotes(QuickNotesCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editQuickNotes", model:[command:command]
            return;
        }
        politicianService.updatePoliticianQuickNotes(user, command.politicianExtraInfo, command.institutionalOffice, command.politicalOffice)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianQuickNotes', params: user.encodeAsLinkProperties()
    }

    def editCauses(){
        KuorumUser politician = params.user
        PoliticianCausesCommand command = new PoliticianCausesCommand(politician)
        [command:command]
    }

    def updateCauses(PoliticianCausesCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editCauses", model:[command:command]
            return;
        }
        politicianService.updatePoliticianCauses(user, command.causes)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianCauses', params: user.encodeAsLinkProperties()
    }

    def editPoliticalExperience(){
        KuorumUser politician = params.user
        PoliticalExperienceCommand command = new PoliticalExperienceCommand(politician)
        [command:command]
    }

    def updatePoliticalExperience(PoliticalExperienceCommand command){
        KuorumUser user = params.user
        if (command.hasErrors() || !user ){
            render view:"editPoliticalExperience", model:[command:command]
            return;
        }
        politicianService.updatePoliticianExperience(user, command.timeLine)
        flash.message=message(code:'profile.editUser.success')
        redirect mapping:'profilePoliticianExperience', params: user.encodeAsLinkProperties()
    }

}
