package kuorum.campaign

import kuorum.users.KuorumUser

class PollCampaign {

    KuorumUser politician;
    List<String> values;
    String userEmail;

    static constraints = {
        userEmail email:true;
    }
}
