package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.EnterpriseSector
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector
import kuorum.postalCodeHandlers.PostalCodeHandler
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand{
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        EditUserProfileCommand.bindingRegion(obj, source, "homeRegion")
    })
    Region homeRegion
    @Deprecated
    String postalCode
    Gender gender
    String name
//    Region country
//    Region province

    //Step2
    String photoId
    WorkingSector workingSector
    Studies studies
    EnterpriseSector enterpriseSector
    AvailableLanguage language
    String bio

    String phonePrefix
    String telephone

    String alias
    Integer year

    String imageProfile
    static constraints = {
        importFrom BirthdayCommad
        //Step1
        gender nullable: true
        name nullable: false, maxSize: 17
        alias nullable: true
//        country nullable: true
//        province nullable:true
        postalCode nullable: true
        homeRegion nullable: false

        //Step2
        photoId nullable: true
        workingSector nullable: true
        studies nullable: true
        enterpriseSector nullable:true
        bio nullable: true, maxSize: 270
        year nullable:true

        telephone nullable:true

        imageProfile nullable: true
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

    public static Region bindingRegion(obj,  org.grails.databinding.DataBindingSource source, String field){
        String fieldId = field+".id"
        if (source[fieldId]){
            Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
            RegionService regionService = (RegionService)appContext.regionService
            Region region = regionService.findRegionBySuggestedId(source[fieldId])
            return region;
        }
    }
}
