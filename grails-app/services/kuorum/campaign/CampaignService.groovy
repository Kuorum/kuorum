package kuorum.campaign

import com.mongodb.DBCursor
import com.mongodb.DBObject
import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.exception.KuorumExceptionData
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.UserType
import kuorum.notifications.NotificationService
import kuorum.register.RegisterService
import kuorum.users.KuorumUser

@Transactional
class CampaignService {

    NotificationService notificationService;
    RegisterService registerService

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

        if (!registerService.checkValidEmail(pollCampaing.userEmail)){
            throw new KuorumException("Not valid email: ${pollCampaing.userEmail}", new KuorumExceptionData(code: 'campaignService.email.notValid', field:"email"))
        }

        PollCampaignVote alreadyVoted = PollCampaignVote.findByCampaignAndUserEmail(pollCampaing.campaign, pollCampaing.userEmail);
        if (alreadyVoted){
            Map<String, Long> decremental = alreadyVoted.values.collectEntries{["results.$it", -1]}
            Campaign.collection.update([_id:alreadyVoted.campaign.id],['$inc':decremental])
            alreadyVoted.values = pollCampaing.values
            pollCampaing = alreadyVoted
        }
        if (pollCampaing.save()){
            Map<String, Long> incremental = pollCampaing.values.collectEntries{["results.$it", 1]}
            Campaign.collection.update([_id:pollCampaing.campaign.id],['$inc':incremental])
            notificationService.sendPollCampaignNotification(pollCampaing);
        }else{
            KuorumExceptionUtil.createExceptionFromValidatable(pollCampaing)
        }

    }
}
