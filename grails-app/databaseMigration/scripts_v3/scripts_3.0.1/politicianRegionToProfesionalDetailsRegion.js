var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({politicianOnRegion:{$exists:1}}).forEach(function(politician) {
    var region = politician.politicianOnRegion
    if (region != undefined){
        dbDest.kuorumUser.update(
            {_id:politician._id},
            {$set:{
                'professionalDetails.region':region,
                'professionalDetails.constituency':region
            },
            $unset:{
                politicianOnRegion:""
            }}
        )
    }
})