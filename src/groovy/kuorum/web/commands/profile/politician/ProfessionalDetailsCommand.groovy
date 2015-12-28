package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.Region
import kuorum.users.KuorumUser
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.commands.profile.EditUserProfileCommand
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 28/12/15.
 */
@Validateable
class ProfessionalDetailsCommand {
    KuorumUser politician
    ProfessionalDetails professionalDetails

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        EditUserProfileCommand.bindingRegion(obj, source, "region")
    })
    Region region

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        EditUserProfileCommand.bindingRegion(obj, source, "constituency")
    })
    Region constituency

    static constraints = {
        politician nullable: false;
        region nullable: true
        constituency nullable: true
    }

    public void setRegion(Region region){
        this.region = region
        if (!this.professionalDetails){
            this.professionalDetails = new ProfessionalDetails()
        }
        this.professionalDetails.region = region
    }
    public void setConstituency(Region constituency){
        this.constituency = constituency
        if (!this.professionalDetails){
            this.professionalDetails = new ProfessionalDetails()
        }
        this.professionalDetails.constituency = constituency
    }

    public Region getRegion(){
        this?.professionalDetails?.region
    }

    public Region getConstituency(){
        this?.professionalDetails?.constituency
    }
}
