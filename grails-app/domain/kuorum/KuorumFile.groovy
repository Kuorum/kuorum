package kuorum

import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class KuorumFile {

    ObjectId id
    KuorumUser user
    /**
     * Define if the image is stored on kuorumServer or is externarl
     */
    Boolean local = Boolean.TRUE
    Boolean temporal
    String storagePath
    String fileName
    String url
    FileGroup fileGroup
    FileType fileType

    static constraints = {
        storagePath nullable: true
        fileName nullable:true
        url url: true
        local validator: {val, obj-> // If local , storagePath and fileName can not be null
            (val && obj.storagePath && obj.fileName) || (!val)
        }
    }
}
