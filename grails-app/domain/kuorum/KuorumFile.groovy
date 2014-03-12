package kuorum

import kuorum.core.FileGroup
import kuorum.users.KuorumUser
import org.bson.types.ObjectId

class KuorumFile {

    ObjectId id
    KuorumUser user
    Boolean temporal
    String storagePath
    String fileName
    String url
    FileGroup fileGroup

    static constraints = {
    }
}
