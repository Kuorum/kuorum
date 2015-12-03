package kuorum.campaign

import com.mongodb.DBCursor
import com.mongodb.DBObject
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.model.UserType
import kuorum.notifications.NotificationService
import kuorum.users.KuorumUser

@Transactional
class CampaignService {

    NotificationService notificationService;

    PollCampaign createPollCampaign(String name, List<String> values, List<KuorumUser> politicians, startDate, endDate ){
        PollCampaign pollCampaign = new PollCampaign(
                startDate: startDate,
                endDate: endDate,
                name : name,
                values: values,
                results: values.collectEntries{[it, 0]}
        );
        pollCampaign.politicianIds = politicians.collect{it.id}
        pollCampaign.save();
        pollCampaign
    }


    Campaign findActiveCampaign(KuorumUser politician){
        Date now = new Date()
        DBCursor cursor = Campaign.collection.find(['politicianIds':politician.id, 'startDate':['$lte': now], 'endDate':['$gte':now]])
        if (cursor.hasNext()){
            DBObject campaign = cursor.next();
            return Campaign.get(campaign._id)
        }else{
            return null;
        }
    }

    Campaign createPSOEFakeCampaign(){
        log.warn("Creando campaña fake para el psoe")
        DBCursor cursor = KuorumUser.collection.find(['userType':UserType.POLITICIAN.toString(), 'professionalDetails.politicalParty':'PSOE'])
        List<KuorumUser> politicians = []
        while (cursor.hasNext()){
            DBObject politicianDB = cursor.next()
            politicians << KuorumUser.get(politicianDB._id)
        }
        createPollCampaign(
                "PSOE - Pedro Sanchez",
                ['recovery','education','democracy','equalty','constitution','foreign'],
                politicians,
                new Date() -10,
                new Date() + 100
        )

    }

    def savePollResponse(PollCampaignVote pollCampaing) {

        if (pollCampaing.save()){
            notificationService.sendPollCampaignNotification(pollCampaing);
        }else{
            throw new KuorumException("Error saving poll");
        }

    }
}
