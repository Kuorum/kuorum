package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.PoliticalParty
import kuorum.Region
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
        [command:new AdminUserCommand(enabled:true), regions:Region.findAll(), politicalParties:PoliticalParty.findAll()]
    }

    def saveUser(AdminUserCommand command){
        if(KuorumUser.findByEmail(command.email)){
            command.errors.rejectValue("email","kuorum.web.commands.admin.AdminUserCommand.email.notUnique")
        }
        if(KuorumUser.findByAlias(command.alias)){
            command.errors.rejectValue("alias","kuorum.web.commands.admin.AdminUserCommand.alias.notUnique")
        }
        if (command.hasErrors()){
            render view: 'createUser', model:[command:command, regions:Region.findAll(), politicalParties:PoliticalParty.findAll()]
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
        [user:user, command:command, regions:Region.findAll(), politicalParties: PoliticalParty.findAll()]
    }

    def updateUser(AdminUserCommand command){
        command.imageProfile = params['imageProfile'] // no se por que no lo esta mapeando
        KuorumUser user = KuorumUser.get(new ObjectId(params.id))
        KuorumUser testUser = KuorumUser.findByEmail(command.email);
        if(testUser && testUser != user){
            command.errors.rejectValue("email","kuorum.web.commands.admin.AdminUserCommand.email.notUnique")
        }
        if (command.alias){
            testUser = KuorumUser.findByAlias(command.alias)
            if(testUser && testUser != user){
                command.errors.rejectValue("alias","kuorum.web.commands.admin.AdminUserCommand.alias.notUnique", [testUser.alias, testUser.name].toArray(),'Alias not unique')
            }
        }
        if (command.hasErrors()){
            render view: 'editUser', model:[user:user,command:command, regions:Region.findAll(), politicalParties: PoliticalParty.findAll()]
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
            personalData.enterpriseSector =  command.enterpriseSector
            kuorumUserService.convertAsOrganization(user)
        }else{
            personalData = new PersonData()
            if (command.userType==UserType.POLITICIAN){
                kuorumUserService.convertAsPolitician(user, command.politicianOnRegion, command.politicalParty)
            }else{
                kuorumUserService.convertAsNormalUser(user)
            }
            personalData.studies = command.studies
            personalData.workingSector = command.workingSector
            personalData.year = command.year
        }
        personalData.gender = command.gender
        personalData.postalCode = command.postalCode
        personalData.provinceCode = command.province.iso3166_2
        personalData.country = command.country
        personalData.province = command.province
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
            command.politicalParty = user.politicalParty
            command.politicianOnRegion = user.politicianOnRegion
        }
        command.year =  user.personalData?.birthday?user.personalData?.birthday[Calendar.YEAR]:null
//        command.month = user.personalData?.birthday?user.personalData?.birthday[Calendar.MONTH] +1:null
//        command.day =   user.personalData?.birthday?user.personalData?.birthday[Calendar.DAY_OF_MONTH]:null

        command.gender = user.personalData.gender
        command.country = user.personalData.country
        command.postalCode = user.personalData.postalCode
        command.province = user.personalData.province
        command.email = user.email
        command.alias = user.alias
        command.verified = user.verified
        command.enabled = user.enabled
        command.userType = user.userType
        command.bio = user.bio
        command.name = user.name
        command.password = user.password
        command.photoId = user.avatar?.id
        command.imageProfile = user.imageProfile?.id
        command.commissions = user.relevantCommissions

        command
    }
}
