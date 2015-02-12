package kuorum.web.commands

import grails.validation.Validateable
import kuorum.Institution
import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileType
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.project.Project
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 3/03/14.
 */

@Validateable
class ProjectCommand {

    private static final YOUTUBE_REGEX = ~/http[s]{0,1}:\/\/(w{3}.){0,1}youtube\.com\/watch\?v=[a-zA-Z0-9_]*/

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
    String videoPost
    String pdfFileId
    KuorumUser owner

    static constraints = {
        importFrom Project
        commissions nullable: false, minSize: 1
        photoId nullable: true, validator: { val, obj ->
            if (!val && !obj.videoPost) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        videoPost nullable: true, url:true, validator: { val, obj ->
            if (val && !YOUTUBE_REGEX.matcher(val).matches()) {
                return ['notYoutubeFormat']
            }
            if (!val && !obj.photoId) {
                return ['imageOrUrlYoutubeRequired']
            }
        }
        pdfFileId nullable: true
    }
}
