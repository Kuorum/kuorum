package kuorum

import kuorum.core.FileGroup
import org.bson.types.ObjectId

class FormTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']

    static namespace = "formUtil"

    def editImage ={attrs ->
        def command = attrs.command
        def field = attrs.field
        def kuorumImageId = command."$field"
        KuorumFile kuorumFile = null
        FileGroup fileGroup = null
        def value = ""
        def imageUrl = ""
        if (kuorumImageId)
            kuorumFile = KuorumFile.get(new ObjectId(kuorumImageId))

        if (!kuorumFile){
            kuorumImageId = "__NEW__"
            fileGroup = attrs.fileGroup
        }else{
            value = kuorumImageId
            fileGroup =kuorumFile.fileGroup
            imageUrl = kuorumFile.url
        }
        def model = [
                imageId: kuorumImageId,
                value:value,
                fileGroup:fileGroup,
                imageUrl:imageUrl,
                name:field
        ]
        out << g.render(template:'/layouts/form/uploadImage', model:model)
    }
}
