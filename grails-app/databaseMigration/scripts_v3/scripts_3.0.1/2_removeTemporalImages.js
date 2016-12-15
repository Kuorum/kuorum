
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.find({'avatar.url':/^https:\/\/kuorum.org\/uploadedImages\/lostImages\/avatar.png$/}).forEach(function(user) {
    dbDest.kuorumUser.update({_id:user._id},{$unset:{'avatar':''}})
    dbDest.kuorumFile.remove({_id:user.avatar._id})
})

dbDest.kuorumUser.find({'imageProfile.url':/^https:\/\/kuorum.org\/uploadedImages\/lostImages/}).forEach(function(user) {
    dbDest.kuorumUser.update({_id:user._id},{$unset:{'imageProfile':''}})
    dbDest.kuorumFile.remove({_id:user.imageProfile._id})
})

dbDest.project.update({hashtag:"#seguridadCiudadana"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"seguridadCiudadana.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"seguridadCiudadana.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/seguridadCiudadana.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/seguridadCiudadana.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f7475e4b04e8641eafa3f")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"seguridadCiudadana.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"seguridadCiudadana.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/seguridadCiudadana.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/seguridadCiudadana.jpg"
    }})


dbDest.project.update({hashtag:"#reformaEducativa"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"reformaEducativa.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"reformaEducativa.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaEducativa.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaEducativa.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f5ebbe4b0423f96766992")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"reformaEducativa.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"reformaEducativa.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaEducativa.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaEducativa.jpg"
    }})

dbDest.project.update({hashtag:"#recualificacionProf"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"recualificacionProf.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"reformaEducativa.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/recualificacionProf.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/recualificacionProf.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f9b69e4b04e8641eafa67")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"recualificacionProf.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"recualificacionProf.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/recualificacionProf.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/recualificacionProf.jpg"
    }})


dbDest.project.update({hashtag:"#presupuestos2014"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"presupuestos2014.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"presupuestos2014.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/presupuestos2014.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/presupuestos2014.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f98d7e4b04e8641eafa5f")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"presupuestos2014.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"presupuestos2014.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/presupuestos2014.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/presupuestos2014.jpg"
    }})

dbDest.project.update({hashtag:"#pobrezaEnergetica"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"pobrezaEnergetica.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"pobrezaEnergetica.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/pobrezaEnergetica.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/pobrezaEnergetica.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f9a84e4b04e8641eafa65")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"pobrezaEnergetica.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"pobrezaEnergetica.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/pobrezaEnergetica.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/pobrezaEnergetica.jpg"
    }})

dbDest.project.update({hashtag:"#parquesNacionales"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"parquesNacionales.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"parquesNacionales.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/parquesNacionales.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/parquesNacionales.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f93e1e4b04e8641eafa55")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"parquesNacionales.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"parquesNacionales.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/parquesNacionales.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/parquesNacionales.jpg"
    }})

dbDest.project.update({hashtag:"#medidasAntiCrisis"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"medidasAntiCrisis.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"medidasAntiCrisis.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/medidasAntiCrisis.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/medidasAntiCrisis.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f9f65e4b04e8641eafa6e")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"medidasAntiCrisis.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"medidasAntiCrisis.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/medidasAntiCrisis.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/medidasAntiCrisis.jpg"
    }})

dbDest.project.update({hashtag:"#justiciaUniversal"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"justiciaUniversal.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"justiciaUniversal.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/justiciaUniversal.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/justiciaUniversal.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("538f3c55e4b0dca2e4fb1753")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"justiciaUniversal.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"justiciaUniversal.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/justiciaUniversal.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/justiciaUniversal.jpg"
    }})

dbDest.project.update({hashtag:"#propiedadIntelectual"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"propiedadIntelectual.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"propiedadIntelectual.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/propiedadIntelectual.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/propiedadIntelectual.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("545268d3e4b05873c20ef7c0")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"propiedadIntelectual.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"propiedadIntelectual.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/propiedadIntelectual.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/propiedadIntelectual.jpg"
    }})


dbDest.project.update({hashtag:"#reformaElectoral"},
    {$set:{
        'image.fileGroup':"PROJECT_IMAGE",
        'image.fileName':"reformaElectoral.jpg",
        'image.fileType':"AMAZON_IMAGE",
        "image.local" : false,
        'image.originalName':"reformaElectoral.jpg",
        "image.storagePath" : "",
        "image.temporal" : false,
        "image.url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaElectoral.jpg",
        "image.urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaElectoral.jpg"
    }})

dbDest.kuorumFile.update({_id: ObjectId("54049cbde4b099d72696c17f")},
    {$set:{
        'fileGroup':"PROJECT_IMAGE",
        'fileName':"reformaElectoral.jpg",
        "fileType":"AMAZON_IMAGE",
        "local" : false,
        'originalName':"reformaElectoral.jpg",
        "storagePath" : "",
        "temporal" : false,
        "url" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaElectoral.jpg",
        "urlThumb" : "https://kuorumorg.s3.amazonaws.com/ProjectsFiles/reformaElectoral.jpg"
    }})

//FIX David Burrowes
dbDest.kuorumUser.update({_id:ObjectId("55feeb04e4b0849cb0cc6117")},{$unset:{'personalData.province':""}})
dbDest.kuorumUser.update({_id:ObjectId("55feeb04e4b0849cb0cc6117")},{$unset:{'personalData.provinceCode':""}})