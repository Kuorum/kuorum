package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.*
import kuorum.postalCodeHandlers.PostalCodeHandler
import kuorum.users.KuorumUser
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindingFormat

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand{

    public EditUserProfileCommand(){}
    public EditUserProfileCommand(KuorumUser user){
        this.gender = user.personalData?.gender
        this.bio = user.bio
        if (user.userType == UserType.ORGANIZATION){
            this.enterpriseSector = user.personalData?.enterpriseSector
        }else{
            this.position = user.professionalDetails?.position
            this.birthday= user.personalData?.birthday
            this.workingSector = user.personalData?.workingSector
            this.studies = user.personalData?.studies
        }
    }


    String bio
    Gender gender

    //Citizen
    @BindingFormat("dd/MM/yyyy")
    Date birthday
    Studies studies
    EnterpriseSector enterpriseSector
    WorkingSector workingSector

    //Politician
    String position
    //String politicalParty

    static constraints = {
        gender nullable: true
        birthday nullable:true
        workingSector nullable: true
        studies nullable: true
        enterpriseSector nullable:true
        bio nullable: true, maxSize: 1000
        //politicalParty nullable:true
        position nullable:true
    }

    @Deprecated
    public static Region bindingPostalCode(obj,  org.grails.databinding.DataBindingSource source){
        if (source['homeRegionId']){
            Region country = Region.get(new ObjectId(source['country']))
            obj.country = country
            Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
            RegionService regionService = (RegionService)appContext.regionService
            PostalCodeHandler postalCodeHandler = regionService.getPostalCodeHandler(country)
            String postalCode = source["postalCode"]
            if (postalCode){
                postalCode = postalCodeHandler.standardizePostalCode(postalCode)
                obj.province = regionService.findMostSpecificRegionByPostalCode(country, postalCode)
                postalCode = postalCode?:source["postalCode"]
            }
            return new Region(name:"inventada", iso3166_2: "EU-ES-IN")
        }
    }
}
