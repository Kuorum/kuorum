package kuorum.campaign

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.notifications.NotificationService

@Transactional
class CampaignService {

    NotificationService notificationService;

    def savePollResponse(PollCampaign pollCampaing) {

        if (pollCampaing.save()){
            notificationService.sendPollCampaingNotification(pollCampaing);
        }else{
            throw new KuorumException("Error saving poll");
        }

    }
}
