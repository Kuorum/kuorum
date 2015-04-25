package kuorum.project

import kuorum.Region
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class ProjectEvent {

    ObjectId id
    Project project
    KuorumUser owner
    ProjectAction projectAction
    Integer projectUpdatePos
    Date dateCreated
    Region region

    static embedded = ['region' ]

    static constraints = {
        projectUpdatePos nullable: true, validator: {val, obj ->
            if (obj.projectAction && obj.projectAction == ProjectAction.PROJECT_UPDATE && !val){
                return "projectUpdateWithoutPos"
            }
        }
    }
}

public enum ProjectAction{
    PROJECT_CREATED, PROJECT_UPDATE
}
