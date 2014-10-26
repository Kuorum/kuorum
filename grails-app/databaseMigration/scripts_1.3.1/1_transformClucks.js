
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.cluck.find().forEach(function(cluck){

    var law = dbDest.law.find({_id:cluck.law})[0]
    var newCluck = {
        cluckAction:cluck.isFirstCluck?"CREATE":"CLUCK",
        owner:cluck.owner,
        postOwner:cluck.postOwner,
        law:cluck.law,
        post:cluck.post,
        region:law.region,
        dateCreated:cluck.dateCreated,
        lastUpdated:cluck.lastUpdated
    }
    dbDest.cluck.save(newCluck);

    if (cluck.debateMembers!= undefined && cluck.debateMembers.length>0){
        print("DEbate memebers"+cluck.debateMembers.length)
        cluck.debateMembers.forEach(function(userId){
            var debateCluck = {
                cluckAction:"DEBATE",
                owner:userId,
                postOwner:cluck.postOwner,
                law:cluck.law,
                post:cluck.post,
                region:law.region,
                dateCreated:cluck.dateCreated,
                lastUpdated:cluck.lastUpdated
            }
            dbDest.cluck.save(debateCluck)
        })
    }
    dbDest.cluck.remove({_id:cluck._id})
});