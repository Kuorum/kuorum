
function createAvatar(objectId, fileGroup, photo){
    var id = new ObjectId();
    if (photo != undefined && photo != null && photo != ""){
        var kuorumFile = {
            "_class":"KuorumFile",
            "_id":id,
            "userId":objectId,
            "temporal":false,
            "local":photo.indexOf("http://") < 0,
            "storagePath":storagePath(photo).storagePath,
            "fileName":storagePath(photo).fileName,
            "url":absoluteUrl(photo),
            "fileGroup":fileGroup,
            "fileType":"IMAGE"
        }

        dbDest.kuorumFile.insert(kuorumFile)
        return kuorumFile
    }else{
        return null
    }
}

function storagePath(pathAvatar){
    var absoluteRootPath = "/home/tomcat7/uploadedImages/"
    if (pathAvatar.indexOf("http://") == 0){
        //External file (FACEBOOK)
        return {
            "storagePath":null,
            "fileName": null
        }
    }else if (pathAvatar){
        return {
            "storagePath":absoluteRootPath+pathAvatar.substring(0,pathAvatar.lastIndexOf("/")),
            "fileName": pathAvatar.substring(pathAvatar.lastIndexOf("/")+1)
        }
    }else{
        return {
            "storagePath":null,
            "fileName": null
        }
    }
}

function absoluteUrl(pathAvatar){
    var absoluteRootUrl = "http://kuorum.org/uploadedImages/"
    if (pathAvatar.indexOf("http://") == 0){
        //External file (FACEBOOK)
        return pathAvatar
    }else if (pathAvatar){
        return absoluteRootUrl+pathAvatar
    }else{
        return null
    }
}