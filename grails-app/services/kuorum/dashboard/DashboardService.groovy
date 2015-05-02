package kuorum.dashboard

import kuorum.core.model.UserType
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.KuorumUser
import org.springframework.context.MessageSource
import groovy.time.*

class DashboardService {

    MessageSource messageSource

    /**
     * Method to show notices if the user's data profile is incomplete
     *
     * @param user The user who starts its session
     * @param locale The current language
     * @return resultMessage the notice to show depending of the priority
     */
    Map showNotice(KuorumUser user, Locale locale){
        Map resultMessage
        NoticeType noticeType = getNoticesByKuorumUser(user)
        use(TimeCategory){
            if(!user.notice || new Date().after(user.notice.firstDashboard + 1.month) || user.notice.noticeType != noticeType){
                user.notice = new Notice(noticeType: noticeType)
            }
        }

        switch (noticeType){
            case {noticeType in [NoticeType.FOLLOWPEOPLE, NoticeType.NOPOLITICIANPHONE]}:
                resultMessage = [noticeType:noticeType,notice: messageSource.getMessage("dashboard.warningsUserProfile.$noticeType", null, locale), errors:false]
                break
            case {noticeType in [NoticeType.NOPOLITICIANINYOURCOUNTRY, NoticeType.NOPROVINCE, NoticeType.NOAGEANDGENDER]}:
                resultMessage = [noticeType:noticeType, notice: showNoticeAccordingToReloads(user, noticeType, locale), errors: false]
                break
            default:
                break
        }
        if(resultMessage?.notice){
            ++user.notice.timesInMonth
        }
        ++user.notice?.reloadDashboard

        if (user.validate()){
            user.save()
        }else{
            resultMessage = [notice:messageSource.getMessage("dashboard.warningsUserProfile.errorSaving", null, locale), errors:true]
        }
        resultMessage
    }


    /**
     * Method to get the type of the current notice
     *
     * @param user The user who starts its session
     * @return UserType The type of the user notice
     */
    NoticeType getNoticesByKuorumUser(KuorumUser user){
        if (user.userType == UserType.POLITICIAN && !user.personalData.telephone){
            NoticeType.NOPOLITICIANPHONE
        }else if (!usersFollowingPolitician(user)){
            NoticeType.FOLLOWPEOPLE
        } else if (!user.personalData.province){
            NoticeType.NOPROVINCE
        } else if (!user.personalData.gender || !user.personalData.year){
            NoticeType.NOAGEANDGENDER
        } else if(!politiciansInProvince(user)){
            NoticeType.NOPOLITICIANINYOURCOUNTRY
        }
    }


    /**
     * Method to evaluate if the current message must be shown in the dashboard
     *
     * @param user The user who starts its session
     * @param noticeType one of NoticeType.NOPOLITICIANINYOURCOUNTRY, NoticeType.NOPROVINCE, NoticeType.NOAGEANDGENDER
     * @param locale The current language
     * @return A String message
     */
    private String showNoticeAccordingToReloads(KuorumUser user, NoticeType noticeType, Locale locale){
        if (!(user.notice.reloadDashboard % 20) && (user.notice.timesInMonth < 2)){
            messageSource.getMessage("dashboard.warningsUserProfile.$noticeType", null, locale)
        }
    }


    /**
     * Method to find id a user is following politicians
     *
     * @param user The user who starts its session
     * @return users who are following politicians
     */
    private usersFollowingPolitician(KuorumUser user){
        KuorumUser.createCriteria().list(){
            'in'("id", user.following)
        }
    }


    /**
     * Method to find politicians in the user's province
     *
     * @param user The user who starts its session
     * @return Politicians who are in the same province as user
     */
    private politiciansInProvince(KuorumUser user){
        List<KuorumUser> kuorumUsers = KuorumUser.createCriteria().list(){
            and{
                eq("userType", UserType.POLITICIAN)
                isNotNull("politicianOnRegion")
            }
        }
        kuorumUsers.findAll{ user.personalData?.provinceCode?.startsWith(it.politicianOnRegion.iso3166_2) }
    }
}

