package kuorum.editor

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.RegionService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.files.FileService
import kuorum.register.KuorumUserSession
import kuorum.register.RegisterService
import kuorum.security.evidences.Evidences
import kuorum.security.evidences.HttpRequestRecoverEvidences
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PersonalData
import kuorum.users.ProfileController
import kuorum.web.commands.editor.EditorAccountCommand
import kuorum.web.commands.profile.EditProfilePicturesCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import org.kuorum.rest.model.kuorumUser.KuorumUserExtraDataRSDTO

@Secured(['ROLE_ADMIN'])
class EditorUserController {

    KuorumUserService kuorumUserService
    FileService fileService
    RegionService regionService

    RegisterService registerService
    SpringSecurityService springSecurityService

    def editUser(String userAlias){
        KuorumUser user = kuorumUserService.findEditableUser(userAlias)
        EditUserProfileCommand command = new EditUserProfileCommand(user)
        [user:user, command:command]
    }

    def updateUser(EditUserProfileCommand command){
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }

        ProfileController.prepareUserEditProfile(user,command) //Chapu for generic process
        ProfileController.prepareUserImages(user,command, fileService)//Chapu for generic process
        kuorumUserService.updateUser(user)
        flash.message =message(code:'admin.editUser.success', args: [user.name])
        redirect(mapping:'editorEditUserProfile', params:user.encodeAsLinkProperties())
    }

    def editAdminAccountDetails(){
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        KuorumUserExtraDataRSDTO userExtraDataRSDTO = kuorumUserService.findUserExtendedDataRSDTO(user.getId().toString())
        EditorAccountCommand command = new EditorAccountCommand(user)
        [user:user,command:command]
    }

    def updateAdminAccountDetails(EditorAccountCommand command) {
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        validateUniqueEmail(user, command)
        if (command.hasErrors()) {
            flash.error = message(code: 'admin.createUser.error') + ": " + message(error: command.errors.getFieldError())
            render view: 'editAdminAccountDetails', model: [command: command, user: user]
            return
        }

        user.email = command.email
        user.language = command.language
        user.name = command.name
        user.surname = command.surname
        user.bio = command.bio
        kuorumUserService.updateUser(user)

        flash.message = message(code: 'kuorum.web.commands.editor.EditorAccountCommand.logic.updateSuccess', args: [user.name])
        redirect(mapping: 'editorKuorumAccountEdit', params: user.encodeAsLinkProperties())
    }


    def editAdminAccountPictures() {
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        EditProfilePicturesCommand command = new EditProfilePicturesCommand(user)
        [user: user, command: command]
    }

    def updateAdminAccountPictures(EditProfilePicturesCommand command) {
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()) {
            render view: "editPictures", model: [command: command, user: user]
            return
        }
        ProfileController.prepareUserImages(user, command, fileService)
        kuorumUserService.updateUser(user)
        flash.message = message(code: 'profile.editUser.success')
        redirect mapping: "editorKuorumAccountPicturesEdit", params: user.encodeAsLinkProperties()
    }

    def invalidateUser() {
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias, true)
        if (user) {
            kuorumUserService.invalidateUser(user)
            flash.message = "User invalidated"
        } else {
            flash.error = "User not found"
        }
        redirect(mapping: 'editorAdminUserRights', params: user.encodeAsLinkProperties())
    }

    def validateUser(){
        BasicDataKuorumUserRSDTO user = kuorumUserService.findBasicUserRSDTO(params.userAlias, true)
        KuorumUserSession kuorumUserSession = springSecurityService.principal
        Long campaignId
        try{
            campaignId = params.campaignId?Long.parseLong(params.campaignId):null


            if (user && campaignId){
                Evidences evidences = new HttpRequestRecoverEvidences(request, params.browserId);
                kuorumUserService.adminValidation(kuorumUserSession, evidences, user, campaignId)
                flash.message = "User validated"
            } else {
                flash.error = "Campaign not found"
            }
        } catch (Exception e) {
            flash.error("Error validating user");
        }
        redirect(mapping: 'editorAdminUserRights', params: user.encodeAsLinkProperties())
    }

    void validateUniqueEmail(KuorumUser user, EditorAccountCommand command) {
        if (command.email && command.email != user.email && KuorumUser.findByEmailAndDomain(command.email, CustomDomainResolver.domain)) {
            command.errors.rejectValue("email", "unique")
        }
    }
}
