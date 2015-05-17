package kuorum.web.commands

import grails.validation.Validateable
import kuorum.core.FileType
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
    String description
//    Boolean availableStats
    List<CommissionType> commissions  = new ArrayList<CommissionType>()
//    Region region
    String photoId
    ProjectStatusType status = ProjectStatusType.OPEN
    FileType fileType

    //New fields for Project
    Date deadline
    String videoPost
    String pdfFileId

    static constraints = {
        importFrom Project
        commissions nullable: false, minSize: 1
        photoId nullable: true, validator: { val, obj ->
            if (!val && !obj.videoPost) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        videoPost nullable: true, url:true, validator: { val, obj ->
            if (val && !val.decodeYoutubeName()) {
                return ['notYoutubeFormat']
            }
            if (!val && !obj.photoId) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        pdfFileId nullable: true
        deadline nullable: false, validator: { val, obj ->
            if (val < new Date().clearTime()) {
                return ['deadlineLessThanToday']
            } else if (val.clearTime() > new Date().clearTime() + 120) {
                return ['deadlineGreaterThan120Days']
            }
        }
    }
}
