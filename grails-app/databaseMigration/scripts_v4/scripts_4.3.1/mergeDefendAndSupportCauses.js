var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.find({ideology:{$exists:1}},{ideology:1}).forEach(function(user){
    dbDest.kuorumUser.update(
        {_id:user._id},
        {
            $addToSet:{'ideology.supportedCauses':{$each:user.ideology.defendedCauses}},
            $unset:{'ideology.defendedCauses':0}
        })
});


dbDest.cause.find().forEach(function(cause){
    var numSupporting = dbDest.kuorumUser.find({'ideology.supportedCauses':cause.name}).count()
    dbDest.cause.update(
        {_id:cause._id},
        {
            $set:{citizenSupports:numSupporting},
            $unset:{politicianDefends:0}
        })
});