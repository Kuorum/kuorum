package kuorum.editor

import grails.plugin.springsecurity.annotation.Secured
import kuorum.RegionService
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.register.RegisterService
import kuorum.users.*
import kuorum.web.commands.editor.EditorAccountCommand
import kuorum.web.commands.editor.EditorCreateUserCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import kuorum.web.commands.profile.SocialNetworkCommand

@Secured(['ROLE_EDITOR'])
class EditorUserController {

    KuorumUserService kuorumUserService
    FileService fileService
    RegionService regionService

    RegisterService registerService;

    def createPolitician(){
        [command:new EditorCreateUserCommand()]
    }

    def saveCreatePolitician(EditorCreateUserCommand command){
        if (command.hasErrors()){
            render view: "createPolitician", model:[command:command]
            return;
        }
        String pass = registerService.generateNotSetUserPassword("EDITOR")
        KuorumUser newPolitician = registerService.createUser(command.name, pass, command.email,command.alias, command.language)
        newPolitician.personalData.phonePrefix = command.phonePrefix
        newPolitician.personalData.telephone = command.phone
        if (command.homeRegion){
            newPolitician.personalData.province = command.homeRegion
            newPolitician.personalData.provinceCode = command.homeRegion.iso3166_2
        }
        newPolitician.authorities.remove(RoleUser.findByAuthority("ROLE_INCOMPLETE_USER"))
        newPolitician = kuorumUserService.updateUser(newPolitician);
        redirect(mapping:'userShow', params:newPolitician.encodeAsLinkProperties())

    }

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
        EditorAccountCommand command = new EditorAccountCommand(user);
        [user:user,command:command]
    }

    def updateAdminAccountDetails(EditorAccountCommand command){
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            flash.error=message(code:'admin.createUser.error')
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
            updatedUser.personalData = new  PersonData();
        }
        updatedUser.personalData.phonePrefix = command.phonePrefix
        updatedUser.personalData.telephone = command.phone
        if (command.homeRegion){
            updatedUser.personalData.province = command.homeRegion
            updatedUser.personalData.provinceCode = command.homeRegion.iso3166_2
        }
        updatedUser = kuorumUserService.updateUser(updatedUser);

        flash.message =message(code:'admin.editUser.success', args: [updatedUser.name])
        redirect(mapping:'editorKuorumAccountEdit', params:updatedUser.encodeAsLinkProperties())
    }

    def editUserSocialNetwork(){
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        SocialNetworkCommand command = new SocialNetworkCommand(user);
        [user:user,command:command]
    }

    def updateUserSocialNetwork(SocialNetworkCommand command){
        KuorumUser user = kuorumUserService.findEditableUser(params.userAlias)
        if (command.hasErrors()){
            render (view:'socialNetworks', model:[user:user, command: command])
            return
        }
        command.properties.each {
            if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                user.socialLinks."${it.key}" = it.value
        }
        user.socialLinks.twitter =user.socialLinks.twitter?.decodeTwitter()
        kuorumUserService.updateUser(user)
        flash.message = g.message(code: 'kuorum.web.commands.profile.SocialNetworkCommand.save.success')
        redirect mapping:'editorEditSocialNetwork', params: user.encodeAsLinkProperties()
    }
}
