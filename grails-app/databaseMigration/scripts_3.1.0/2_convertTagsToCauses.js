
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({tags:{$exists:1}}).forEach(function(kuorumUser){

    var defendedCauses = []
    if (kuorumUser.userType == "POLITICIAN"){
        defendedCauses = kuorumUser.tags
    }
    var ideology = {
        supportedCauses:kuorumUser.tags,
        discardedCauses:[],
        defendedCauses:defendedCauses
    }
    dbDest.kuorumUser.update({_id:kuorumUser._id},{$set:{ideology:ideology}})
    dbDest.kuorumUser.update({_id:kuorumUser._id},{$unset:{tags:''}})
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
