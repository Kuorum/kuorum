package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.users.KuorumUser
import kuorum.users.extendedPoliticianData.OfficeDetails
import kuorum.users.extendedPoliticianData.PoliticianExtraInfo

/**
 * Created by iduetxe on 28/12/15.
 */
@Validateable
class QuickNotesCommand {
    KuorumUser politician
    PoliticianExtraInfo politicianExtraInfo
    OfficeDetails institutionalOffice
    OfficeDetails politicalOffice


    static constraints = {
        politician nullable: false;
        politicianExtraInfo nullable: true
        institutionalOffice nullable: true
        politicalOffice nullable: true
    }

    public QuickNotesCommand(){}
    public QuickNotesCommand(KuorumUser politician){
        this.politician = politician
        this.politicianExtraInfo = politician.politicianExtraInfo?:new PoliticianExtraInfo()
        this.institutionalOffice = politician.institutionalOffice?:new OfficeDetails()
        this.politicalOffice = politician.politicalOffice?:new OfficeDetails()
    }
}
