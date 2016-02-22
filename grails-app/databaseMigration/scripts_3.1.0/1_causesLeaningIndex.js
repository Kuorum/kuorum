
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.aggregate(
    [
        {$match:{userType:"POLITICIAN"}},
        {$unwind:"$tags"},
        {$group:{
            _id:"$tags",
            avg:{$avg:"$politicianLeaning.liberalIndex"},
            total:{$sum:1}
        }}
    ]
).forEach(function(causeLeaning){
    dbDest.cause.update({name:causeLeaning._id},{
        $set:{
            'leaningIndex.liberalIndex':causeLeaning.avg
        }
    });
})
