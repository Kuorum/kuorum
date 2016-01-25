package kuorum

import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.Gender
import kuorum.dashboard.DashboardService
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.KuorumUser
import kuorum.users.ProfileController
import kuorum.web.commands.profile.PersonalDataCommand
import kuorum.web.commands.profile.UserRegionCommand

class CustomRegisterController extends  ProfileController{

    DashboardService dashboardService

    def afterInterceptor = {}

//    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED'])
    def countryAndPostalCode(UserRegionCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        if (command.hasErrors()){
            flash.error = message(code:'customRegister.countryAndPostalCode.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.country = regionService.findCountry(command.province)
            Region province = command.province
            user.personalData.province = province
            user.personalData.provinceCode = province.iso3166_2
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            kuorumUserService.updateUser(user)
        }
        flash.message = message(code:'customRegister.countryAndPostalCode.success')
        redirect mapping: 'home'
    }

    @Secured(['ROLE_INCOMPLETE_USER', 'ROLE_PASSWORDCHANGED', 'ROLE_USER'])
    def ageAndGender(PersonalDataCommand personalDataCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        personalDataCommand.validate()

        if (personalDataCommand.hasErrors()){
            flash.error = message(code:'customRegister.ageAndGender.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.gender = personalDataCommand.gender
            if (user.personalData.gender != Gender.ORGANIZATION){
                user.personalData.year = personalDataCommand.year
            }
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            kuorumUserService.updateUser(user)
        }
        flash.message = message(code:'customRegister.ageAndGender.success')
        redirect mapping: 'home'
    }

    @Secured(['ROLE_POLITICIAN'])
    def telephone(PersonalDataCommand personalDataCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        personalDataCommand.validate()

        if (personalDataCommand.hasErrors()){
            flash.error = message(code:'customRegister.telephone.fail')
            redirect mapping: 'home'
            return
        }

        if(user){
            user.personalData.telephone = personalDataCommand.telephone
            user.personalData.phonePrefix = personalDataCommand.phonePrefix
            NoticeType noticeType = dashboardService.getNoticesByKuorumUser(user)
            user.notice = new Notice(noticeType: noticeType)
            kuorumUserService.updateUser(user)
        }
        flash.message = message(code:'customRegister.telephone.success')
        redirect mapping: 'home'
    }


}
