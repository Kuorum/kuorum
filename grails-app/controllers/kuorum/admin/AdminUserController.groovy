package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.PoliticalParty
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.UserType
import kuorum.files.FileService
import kuorum.mail.KuorumMailAccountService
import kuorum.users.*
import kuorum.web.commands.admin.AdminUserCommand
import kuorum.web.commands.admin.KuorumAccountCommand
import kuorum.web.commands.profile.EditUserProfileCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.notification.KuorumMailAccountDetailsRSDTO

@Secured(['ROLE_ADMIN'])
class AdminUserController extends AdminController {

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

    private KuorumUser prepareUser(KuorumUser user, AdminUserCommand command){
        //TODO: Pensar como unificar con el ProfileController
        PersonalData personalData = null
        if (UserType.ORGANIZATION.equals(command.userType)){
            personalData = new OrganizationData()
            personalData.enterpriseSector =  command.enterpriseSector
            kuorumUserService.convertAsOrganization(user)
        }else{
            personalData = new PersonData()
            if (command.userType==UserType.POLITICIAN){
                kuorumUserService.convertAsPolitician(user, command.politicianOnRegion,command.constituency, command.politicalParty)
            }else{
                kuorumUserService.convertAsNormalUser(user)
            }
            personalData.studies = command.studies
            personalData.workingSector = command.workingSector
            personalData.year = command.year
        }
        personalData.gender = command.gender
        personalData.provinceCode = command.homeRegion.iso3166_2
        personalData.country = regionService.findCountry(command.homeRegion)
        personalData.province = command.homeRegion
        personalData.userType = command.userType
        user.personalData = personalData
        user.email = command.email
        user.alias = command.alias
        user.verified = command.verified?:false
        user.enabled = command.enabled?:false
        user.userType = command.userType
        user.bio = command.bio
        user.name = command.name
        user.relevantCommissions = command.commissions
        if (user.password != command.password){
            user.password = springSecurityUiService.encodePassword(command.password, null)
        }


        if (command.photoId){
            KuorumFile avatar = KuorumFile.get(new ObjectId(command.photoId))
            avatar.alt = user.name
            avatar.save()
            user.avatar = avatar
            fileService.convertTemporalToFinalFile(avatar)
            fileService.deleteTemporalFiles(user)
        }
        if (command.imageProfile){
            KuorumFile imageProfile = KuorumFile.get(new ObjectId(command.imageProfile))
            imageProfile.alt = user.name
            imageProfile.save()
            user.imageProfile = imageProfile
            fileService.convertTemporalToFinalFile(imageProfile)
            fileService.deleteTemporalFiles(user)
        }
        user
    }

    private AdminUserCommand prepareCommand(KuorumUser user){
        //TODO: Pensar como unificar con el ProfileController
        AdminUserCommand command = new AdminUserCommand()
        if (UserType.ORGANIZATION.equals(user.userType)){
            command.enterpriseSector = user.personalData.enterpriseSector
        }else{

            command.studies = user.personalData.studies
            command.workingSector= user.personalData.workingSector
        }

        if (UserType.POLITICIAN.equals(user.userType)){
            command.politicalParty = user?.professionalDetails?.politicalParty?:"";
            command.politicianOnRegion = user?.professionalDetails?.region?:null;
            command.constituency = user?.professionalDetails?.constituency?:null;
        }
        command.gender = user.personalData.gender
        command.email = user.email
        command.verified = user.verified
        command.enabled = user.enabled
        command.userType = user.userType
        command.bio = user.bio
        command.password = user.password
        command.photoId = user.avatar?.id
        command.imageProfile = user.imageProfile?.id
        command.commissions = user.relevantCommissions

        command
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
