package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.ParliamentaryGroup
import kuorum.core.model.UserType
import kuorum.users.*
import kuorum.web.commands.admin.AdminUserCommand
import org.bson.types.ObjectId

@Secured(['ROLE_ADMIN'])
class AdminUserController extends AdminController {

    def kuorumUserService
    def springSecurityUiService
    def fileService

    def createUser() {
        [command:new AdminUserCommand(enabled:true), institutions:Institution.findAll(), parliamentaryGroups:ParliamentaryGroup.findAll()]
    }

    def saveUser(AdminUserCommand command){
        if(KuorumUser.findByEmail(command.email)){
            command.errors.rejectValue("email","kuorum.web.commands.admin.AdminUserCommand.email.notUnique")
        }
        if (command.hasErrors()){
            render view: 'createUser', model:[command:command, institutions:Institution.findAll(), parliamentaryGroups:ParliamentaryGroup.findAll()]
            flash.error=message(code:'admin.createUser.error')
            return
        }
        RoleUser roleUser = RoleUser.findByAuthority("ROLE_USER")
        KuorumUser user = new KuorumUser()
        user.authorities = [roleUser]
        user = prepareUser(user, command)
        user = kuorumUserService.createUser(user)
        flash.message =message(code:'admin.createUser.success', args: [user.name])
        redirect(mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    def editUser(String id){
        KuorumUser user = KuorumUser.get(new ObjectId(id))
        AdminUserCommand command = prepareCommand(user)
        [user:user, command:command, institutions:Institution.findAll(), parliamentaryGroups:ParliamentaryGroup.findAll()]
    }

    def updateUser(AdminUserCommand command){
        command.imageProfile = params['imageProfile'] // no se por que no lo esta mapeando
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        if (command.hasErrors()){
            render view: 'editUser', model:[user:user,command:command, institutions:Institution.findAll(), parliamentaryGroups:ParliamentaryGroup.findAll()]
            flash.error=message(code:'admin.createUser.error')
            return
        }
        user = prepareUser(user, command)
        kuorumUserService.updateUser(user)

        flash.message =message(code:'admin.editUser.success', args: [user.name])
        redirect(mapping:'userShow', params:user.encodeAsLinkProperties())
    }

    private KuorumUser prepareUser(KuorumUser user, AdminUserCommand command){
        //TODO: Pensar como unificar con el ProfileController
        PersonalData personalData = null
        if (UserType.ORGANIZATION.equals(command.userType)){
            personalData = new OrganizationData()
            personalData.isPoliticalParty = false
            personalData.country = command.country
            personalData.enterpriseSector =  command.enterpriseSector
            kuorumUserService.convertAsOrganization(user)
        }else{
            personalData = new PersonData()
            if (command.userType==UserType.POLITICIAN){
                kuorumUserService.convertAsPolitician(user, command.institution, command.parliamentaryGroup)
            }else{
                kuorumUserService.convertAsNormalUser(user)
            }
            personalData.studies = command.studies
            personalData.workingSector = command.workingSector
        }
        personalData.birthday = command.date
        personalData.gender = command.gender
        personalData.postalCode = command.postalCode
        personalData.provinceCode = command.province.iso3166_2
        personalData.province = command.province
        personalData.userType = command.userType
        user.personalData = personalData
        user.email = command.email
        user.verified = command.verified?:false
        user.enabled = command.enabled?:false
        user.userType = command.userType
        user.bio = command.bio
        user.name = command.name
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
            command.country = user.personalData.country
            command.enterpriseSector = user.personalData.enterpriseSector
        }else{

            command.studies = user.personalData.studies
            command.workingSector= user.personalData.workingSector
        }
        command.year =  user.personalData?.birthday?user.personalData?.birthday[Calendar.YEAR]:null
        command.month = user.personalData?.birthday?user.personalData?.birthday[Calendar.MONTH] +1:null
        command.day =   user.personalData?.birthday?user.personalData?.birthday[Calendar.DAY_OF_MONTH]:null

        command.gender = user.personalData.gender
        command.postalCode = user.personalData.postalCode
        command.province = user.personalData.province
        command.email = user.email
        command.verified = user.verified
        command.enabled = user.enabled
        command.userType = user.userType
        command.bio = user.bio
        command.name = user.name
        command.password = user.password
        command.photoId = user.avatar?.id
        command.imageProfile = user.imageProfile?.id

        command
    }
}
