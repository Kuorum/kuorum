package kuorum.editor

import grails.plugin.springsecurity.annotation.Secured
import kuorum.RegionService
import kuorum.files.FileService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.PersonalData
import kuorum.users.ProfileController
import kuorum.web.commands.editor.EditorAccountCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import org.kuorum.rest.model.kuorumUser.KuorumUserExtraDataRSDTO

@Secured(['ROLE_SUPER_ADMIN'])
class EditorUserController {

    KuorumUserService kuorumUserService
    FileService fileService
    RegionService regionService

    RegisterService registerService

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
        command.setPhone(userExtraDataRSDTO.getPhoneNumber())
        command.setPhonePrefix(userExtraDataRSDTO.getPhoneNumberPrefix())
        [user:user,command:command]
    }

    def updateAdminAccountDetails(EditorAccountCommand command){
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            flash.error=message(code:'admin.createUser.error') +": " + message(error:command.errors.getFieldError())
            render view: 'editAdminAccountDetails', model:[command:command, user:user]
            return
        }
        KuorumUser updatedUser = kuorumUserService.updateAlias(user, command.alias)
        if (!updatedUser){
            flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.aliasError')
            render view: 'editAdminAccountDetails', model:[command:command, user:updatedUser]
            return
        }
        updatedUser.email = command.email
        updatedUser.language = command.language
        updatedUser.name = command.name
        updatedUser.surname = command.surname
        if (!updatedUser.personalData){
            updatedUser.personalData = new  PersonalData()
        }
        updatedUser.personalData.phonePrefix = command.phonePrefix
        updatedUser.personalData.telephone = command.phone
        if (command.homeRegion){
            updatedUser.personalData.provinceCode = command.homeRegion.iso3166
        }
        updatedUser = kuorumUserService.updateUser(updatedUser)

        flash.message =message(code:'kuorum.web.commands.editor.EditorAccountCommand.logic.updateSuccess', args: [updatedUser.name])
        redirect(mapping:'editorKuorumAccountEdit', params:updatedUser.encodeAsLinkProperties())
    }
}
