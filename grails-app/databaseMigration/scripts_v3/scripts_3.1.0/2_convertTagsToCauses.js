
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.find({tags:{$exists:1}}).forEach(function(kuorumUser){

    var causes =[]
    kuorumUser.tags.forEach(function(causeName){
        causes.push(causeName.trim())
    })

    var defendedCauses = []
    if (kuorumUser.userType == "POLITICIAN"){
        defendedCauses = causes
    }
    var ideology = {
        supportedCauses:causes,
        discardedCauses:[],
        defendedCauses:defendedCauses
    }
    dbDest.kuorumUser.update({_id:kuorumUser._id},{$set:{ideology:ideology}})
    dbDest.kuorumUser.update({_id:kuorumUser._id},{$unset:{tags:''}})
})

dbDest.cause.find().forEach(function(cause){
    var correctName = cause.name.trim()
    dbDest.cause.update({name:cause.name},{$set:{name:correctName}})
})

dbDest.kuorumUser.aggregate(
        [
            {$match:{userType:"POLITICIAN"}},
            {$unwind:"$ideology.defendedCauses"},
            {$group:{
                _id:"$ideology.defendedCauses",
                total:{$sum:1}
            }}
        ]
    ).forEach(function(totalCause){
        dbDest.cause.update({name:totalCause._id},{
            $set:{
                'politicianDefends':totalCause.total,
                'citizenSupports':totalCause.total
            }
        });
    })

dbDest.cause.update({},{$unset:{citizenVotes:'', politicianVotes:''}},{multi:true})
