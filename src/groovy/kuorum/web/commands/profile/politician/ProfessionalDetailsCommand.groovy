package kuorum.web.commands.profile.politician

import grails.validation.Validateable
import kuorum.Region
import kuorum.users.KuorumUser
import kuorum.users.extendedPoliticianData.CareerDetails
import kuorum.users.extendedPoliticianData.ProfessionalDetails
import kuorum.web.binder.RegionBinder
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 28/12/15.
 */
@Validateable
class ProfessionalDetailsCommand {
    KuorumUser politician
    CareerDetails careerDetails

    String institution;

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "region", source)
    })
    Region region

    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "constituency", source)
    })
    Region constituency

    static constraints = {
        politician nullable: false;
        region nullable: true
        constituency nullable: true
        institution nullable:true
        careerDetails nullable: true
    }

    public ProfessionalDetailsCommand(){}
    public ProfessionalDetailsCommand(KuorumUser politician){
        this.politician = politician
        this.careerDetails = politician.careerDetails?:new CareerDetails()
        this.region = politician?.professionalDetails?.region
        this.constituency = politician?.professionalDetails?.constituency
        this.institution = politician?.professionalDetails?.institution
    }
}
