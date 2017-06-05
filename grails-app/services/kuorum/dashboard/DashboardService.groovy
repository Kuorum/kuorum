package kuorum.dashboard

import com.fasterxml.jackson.core.type.TypeReference
import com.google.gdata.client.contacts.ContactsService
import grails.plugin.springsecurity.SpringSecurityService
import groovy.time.TimeCategory
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.KuorumUser
import kuorum.util.rest.RestKuorumApiService
import org.kuorum.rest.model.communication.PageCampaignRSDTO
import org.kuorum.rest.model.communication.debate.PageDebateRSDTO
import org.kuorum.rest.model.communication.post.PagePostRSDTO
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.springframework.context.MessageSource
import payment.contact.ContactService

class DashboardService {

    MessageSource messageSource

    RegionService regionService

    RestKuorumApiService restKuorumApiService

    SpringSecurityService springSecurityService;
    ContactService contactService;

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

    boolean forceUploadContacts(){
        KuorumUser user = springSecurityService.currentUser
        ContactPageRSDTO contacts = contactService.getUsers(user)
        return contacts.total==0 && !user.skipUploadContacts
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

    PagePostRSDTO findAllContactsPosts (KuorumUser user, String viewerUid = null, Integer page = 0){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [page:page]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACTS_POSTS_ALL,
                params,
                query,
                new TypeReference<PagePostRSDTO>() {}
        );

        response.data
    }

    PageCampaignRSDTO findAllContactsCampaigns (KuorumUser user, String viewerUid = null, Integer page = 0){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [page:page]
        if (viewerUid){
            query.put("viewerUid",viewerUid)
        }
        def response = restKuorumApiService.get(
                RestKuorumApiService.ApiMethod.USER_CONTACTS_CAMPAIGNS_ALL,
                params,
                query,
                new TypeReference<PageCampaignRSDTO>() {}
        );

        response.data
    }

    PageDebateRSDTO findAllContactsDebates (KuorumUser user, Integer page = 0){
        Map<String, String> params = [userId: user.id.toString()]
        Map<String, String> query = [page:page]

        def response = restKuorumApiService.get(
            RestKuorumApiService.ApiMethod.USER_CONTACTS_DEBATES_ALL,
            params,
            query,
            new TypeReference<PageDebateRSDTO>(){}
        );

        response.data
    }
}

