package kuorum.web.commands

import grails.validation.Validateable
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.project.Project
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 3/03/14.
 */

@Validateable
class ProjectCommand {
    String hashtag
    String shortName
    String description
    Boolean availableStats
    List<CommissionType> commissions  = new ArrayList<CommissionType>()
    Region region
    Institution institution
    String photoId
    ProjectStatusType status = ProjectStatusType.OPEN

    //New fields for Project
    Date deadline
    String urlYoutubeId
    String pdfFileId
    KuorumUser owner

    static constraints = {
        importFrom Project
        commissions nullable: false, minSize: 1
        photoId nullable: false
        status nullable:false
        pdfFileId nullable: true
        hashtag nullable: false, minSize: 1, maxSize: 7
        shortName nullable:false, minSize: 1, maxSize: 124
        description nullable: false, minSize: 1, maxSize: 1024

    }
}
