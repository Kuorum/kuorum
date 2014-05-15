package kuorum.web.commands

import grails.validation.Validateable
import kuorum.Institution
import kuorum.core.model.CommissionType
import kuorum.law.Law

/**
 * Created by iduetxe on 3/03/14.
 */

@Validateable
class LawCommand {
    String hashtag
    String shortName
    String realName
    String description
    String introduction
    URL urlPdf
    List<CommissionType> commissions  = new ArrayList<CommissionType>()
//    Region region
    Institution institution
    String photoId

    static constraints = {
        importFrom Law
        commissions nullable: false, minSize: 1
        photoId nullable: false
        urlPdf nullable: false, url:true

//        hashtag validator: {val, obj ->
//            if (Law.findByHashtag(val)){
//                return "notUnique"
//            }
//        }

        //Validator is not imported
//        region  nullable:false, validator: { val, obj ->
//            if (obj.institution && val != obj.institution.region) {
//                return ['notSameRegionAsInstitution']
//            }
//        }
    }
}
