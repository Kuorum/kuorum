
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var spain = dbDest.region.findOne({iso3166_2:"EU-ES"})

dbDest.kuorumUser.find({userType:'POLITICIAN'}).forEach(function(politician) {

    dbDest.kuorumUser.update({_id:politician._id},{
        $set:{
            'politicianExtraInfo.completeName':politician.name,
            'professionalDetails.region':politician.politicianOnRegion,
            'personalData.country':spain._id,
            'politicianExtraInfo.webSite':politician.socialLinks.blog
        }
    })
})