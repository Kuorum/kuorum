package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.users.extendedPoliticianData.ExternalPoliticianActivity
import kuorum.users.extendedPoliticianData.PoliticianTimeLine

/**
 * Created by iduetxe on 15/12/15.
 */
@Validateable
class PoliticalExperienceCommand {
    KuorumUser politician
    List<PoliticianTimeLine> timeLine
    static constraints = {
        politician nullable: false;
        timeLine maxSize: 20
    }

    public PoliticalExperienceCommand(){}
    public PoliticalExperienceCommand(KuorumUser politician){
        this.politician = politician
        this.timeLine = politician.timeLine?:[]
    }
}
