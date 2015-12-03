package kuorum.campaign

import kuorum.users.KuorumUser

class PollCampaignVote {

    PollCampaign campaign;
    KuorumUser politician;
    List<String> values;
    String userEmail;

    static constraints = {
        userEmail email:true;
    }
}
