package kuorum.editor

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.RegionService
import kuorum.admin.AdminController
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailAccountService
import kuorum.users.*
import kuorum.web.commands.admin.AdminUserCommand
import kuorum.web.commands.admin.KuorumAccountCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO

@Secured(['ROLE_EDITOR'])
class EditorUserController {

    KuorumUserService kuorumUserService
    def springSecurityUiService
    FileService fileService
    RegionService regionService
    KuorumMailAccountService kuorumMailAccountService;

    def editUser(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        EditUserProfileCommand command = new EditUserProfileCommand(user)
        [user:user, command:command]
    }

    def updateUser(EditUserProfileCommand command){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (command.hasErrors()){
            render view:"editUser", model: [command:command,user:user]
            return
        }

        ProfileController.prepareUserEditProfile(user,command) //Chapu for generic process
        ProfileController.prepareUserImages(user,command, fileService)//Chapu for generic process
        kuorumUserService.updateUser(user)
        flash.message =message(code:'admin.editUser.success', args: [user.name])
        redirect(mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    def editAdminAccountDetails(){
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        KuorumMailAccountDetailsRSDTO account = kuorumMailAccountService.getAccountDetails(user)
        KuorumAccountCommand command = new KuorumAccountCommand(user, account?.active?:false);
        [user:user,command:command]
    }

    def updateAdminAccountDetails(KuorumAccountCommand command){
        if (command.hasErrors()){
            flash.error=message(code:'admin.createUser.error')
            render view: 'editAdminAccountDetails', model:[command:command, user:command.user]
            return
        }
        KuorumUser updatedUser = kuorumUserService.updateAlias(command.user, command.alias)
        if (!updatedUser){
            flash.error = g.message(code:'kuorum.web.commands.profile.AccountDetailsCommand.logic.aliasError')
            render view: 'editAdminAccountDetails', model:[command:command, user:command.user]
            return
        }
        if (command.emailAccountActive){
            kuorumMailAccountService.activateAccount(updatedUser)
        }else{
            kuorumMailAccountService.deleteAccount(updatedUser)
        }
        updatedUser.email = command.email
        updatedUser.language = command.language
        updatedUser.name = command.name
        if (!updatedUser.personalData){
            updatedUser.personalData = new  PersonData();
        }
        updatedUser.personalData.phonePrefix = command.phonePrefix
        updatedUser.personalData.telephone = command.phone
        updatedUser.userType = command.userType
        updatedUser.personalData.userType = command.userType
        updatedUser.enabled = command.active?:false
        updatedUser = kuorumUserService.updateUser(updatedUser);

        flash.message =message(code:'admin.editUser.success', args: [updatedUser.name])
        redirect(mapping:'editorKuorumAccountEdit', params:updatedUser.encodeAsLinkProperties())
    }
}
