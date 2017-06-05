package kuorum.campaign

import kuorum.users.KuorumUser

@Deprecated
class PollCampaignVote extends CampaignVote{

    PollCampaign campaign;
    KuorumUser politician;
    List<String> values;
    String userEmail;

    static constraints = {
        userEmail email:true;
    }
}
