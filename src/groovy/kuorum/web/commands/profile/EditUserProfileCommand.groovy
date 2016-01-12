package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.RegionService
import kuorum.core.model.*
import kuorum.postalCodeHandlers.PostalCodeHandler
import kuorum.web.binder.RegionBinder
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing
import org.grails.databinding.BindingFormat

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand{
    @BindUsing({obj,  org.grails.databinding.DataBindingSource source ->
        RegionBinder.bindRegion(obj, "homeRegion", source)
    })
    Region homeRegion
    @BindingFormat("dd/MM/yyyy")
    Date birthday
    String bio
    Gender gender
//    Region country
//    Region province

    WorkingSector workingSector
    Studies studies
    EnterpriseSector enterpriseSector

    String photoId
    String imageProfile
    static constraints = {
        gender nullable: true
        homeRegion nullable: true
        birthday nullable:true
        //Step2
        photoId nullable: true
        workingSector nullable: true
        studies nullable: true
        enterpriseSector nullable:true
        bio nullable: true, maxSize: 1000
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
}
