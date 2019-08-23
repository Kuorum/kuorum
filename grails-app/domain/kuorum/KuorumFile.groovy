package kuorum

import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class KuorumFile {

    ObjectId id
/**
 * It is a trick for embedded this object
 */
    transient KuorumUser user
    ObjectId userId
    void setUser(KuorumUser user){
        userId = user.id
        this.user = user
    }

    KuorumUser getUser(){
        if (!user)
            user = KuorumUser.get(userId)
        user
    }
    /**
     * Define if the image is stored on kuorumServer or is externarl
     */
    Boolean local = Boolean.TRUE
    Boolean temporal
    String storagePath
    String alt
    String fileName
    String originalName
    String relativePath
    String url
    String urlThumb
    FileGroup fileGroup
    FileType fileType = FileType.IMAGE
    Long campaignId // If not null, image associated to a campaign

    static constraints = {
        storagePath nullable: true
        relativePath nullable: true // If null the relativePath will be root "/"
        fileName nullable:true
        originalName nullable: false
        url url: true
        alt nullable:true
        campaignId nullable:true
        local validator: {val, obj-> // If local , storagePath and fileName can not be null
            (val && obj.storagePath && obj.fileName) || (!val)
        }
    }
}
