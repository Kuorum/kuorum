package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.Region
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.EnterpriseSector
import kuorum.core.model.Gender
import kuorum.core.model.Studies
import kuorum.core.model.WorkingSector
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.grails.databinding.BindUsing

/**
 * Created by iduetxe on 13/02/14.
 */
@Validateable
class EditUserProfileCommand{


    @BindUsing({obj, source ->
        EditUserProfileCommand.bindingPostalCode(obj, source)
        //Returns gender because it assigns return value to gender. WHY??
        source['gender']
    })
    Gender gender
    String name
    String postalCode
    Region country
    Region province

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
        gender nullable: false
        name nullable: false, maxSize: 17
        alias nullable: true
        country nullable: false
        province nullable:true
        postalCode nullable: false, maxSize: 5, matches:"[0-9]+", validator: {val, command ->
            if (command.postalCode && !command.province){
                return "notExists"
            }
        }

        //Step2
        photoId nullable: false
        workingSector nullable: true
        studies nullable: true
        enterpriseSector nullable:true
        bio nullable: true, maxSize: 270
        year nullable:true

        telephone nullable:true

        imageProfile nullable: true
    }

    public static void bindingPostalCode(obj, source){
        if (source['country']){
            Region country = Region.get(new ObjectId(source['country']))
            obj.country = country
            if (country.iso3166_2 == "EU-ES"){
                Object appContext = ServletContextHolder.servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
                def regionService = appContext.regionService
                if (obj.postalCode){
                    obj.postalCode = source['postalCode'].padLeft( 5, '0' )
                    obj.province = regionService.findRegionOrProvinceByPostalCode(country, obj.postalCode)
                }
            }
            else{
                //CHAPU PARA QUE LOS PAISES QUE NO SON ESPA�OLES PONGAN LO QUE LES DE LA GANA
                obj.postalCode = source['postalCode']
                obj.province = country
            }
        }
    }
}
