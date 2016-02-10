

var dbDest = dbDest || connect("localhost:27017/Kuorum");



dbDest.kuorumUser.aggregate([
        {$match:{tags:{$exists:1}}},
        {$unwind:"$tags"},
        {$group:{_id:{tag:"$tags"}, count:{$sum:1}}}
    ]).forEach(function(rawCause) {

    var orgCauseName =rawCause._id.tag
    var causeName =orgCauseName
    if (causeName.substring(0, 1) == "#"){
        causeName = orgCauseName.substring(1)
        print("Chnging  "+orgCauseName +" to "+causeName)
        dbDest.kuorumUser.update({tags:orgCauseName},{$addToSet:{tags:causeName}},{multi:true})
        dbDest.kuorumUser.update({tags:orgCauseName},{$pull:{tags:orgCauseName}},{multi:true})
    }

    var cause = {
        name: causeName,
        citizenVotes: 0,
        politicianVotes : rawCause.count
    }
    dbDest.cause.save(cause);
})