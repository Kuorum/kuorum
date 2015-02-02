package kuorum.web.commands

import grails.validation.Validateable
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.project.Project

/**
 * Created by iduetxe on 3/03/14.
 */

@Validateable
class ProjectCommand {
    String hashtag
    String shortName
//    String realName
    String description
//    String introduction
    Boolean availableStats
    List<CommissionType> commissions  = new ArrayList<CommissionType>()
    Region region
//    Institution institution
    String photoId
    ProjectStatusType status = ProjectStatusType.OPEN

    //New fields for Project
    Date deadline
    String urlYoutubeId
    String pdfFileId
//    PoliticalParty politicalParty

    static constraints = {
        importFrom Project
        commissions nullable: false, minSize: 1
        photoId nullable: false
        status nullable:false
        pdfFileId nullable: true

//        hashtag validator: {val, obj ->
//            if (Project.findByHashtag(val)){
//                return "notUnique"
//            }
//        }

        //Validator is not imported
//        region nullable:false
//        institution nullable:false, validator: { val, obj ->
//            if (obj.region && obj.region != val.region) {
//                return ['notSameRegionAsInstitution']
//            }
//        }
//        region  nullable:false, validator: { val, obj ->
//            if (obj.institution && val != obj.institution.region) {
//                return ['notSameRegionAsInstitution']
//            }
//        }
    }
}
