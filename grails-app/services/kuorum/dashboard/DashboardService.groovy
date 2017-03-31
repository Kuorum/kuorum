package kuorum.dashboard

import com.fasterxml.jackson.core.type.TypeReference
import groovy.time.TimeCategory
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.post.PostRSDTO
import org.springframework.context.MessageSource

class DashboardService {

    MessageSource messageSource

    RegionService regionService

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
        } else if (!user.personalData.gender || (!user.personalData.year && user.personalData.gender!= Gender.ORGANIZATION)){
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
    private boolean politiciansInProvince(KuorumUser user){
        Region country = regionService.findCountry(user.personalData.province)
        Region state = regionService.findState(user.personalData.province)
        long numPoliticiansForUser = KuorumUser.createCriteria().count(){
            or{
                eq("professionalDetails.region.iso3166_2", country.iso3166_2)
                eq("professionalDetails.region.iso3166_2", state.iso3166_2)
            }
        }
        numPoliticiansForUser > 0
    }

    List<PostRSDTO> findAllContactsPosts (KuorumUser user, String viewerUid = null){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [:]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = RestKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACTS_POSTS_ALL,
                params,
                query,
                new TypeReference<List<PostRSDTO>>() {}
        );

        response.data
    }
}

