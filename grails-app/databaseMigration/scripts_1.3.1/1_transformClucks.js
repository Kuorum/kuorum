
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.cluck.find().forEach(function(cluck){

    var law = dbDest.law.find({_id:cluck.law})[0]
    var newCluck = {
        cluckAction:cluck.isFirstCluck?"CREATE":"CLUCK",
        isFirstCluck:cluck.isFirstCluck,
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
                isFirstCluck:false,
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

//DELETING REPETEATED CLUCKS

dbDest.cluck.aggregate(
    [
        {$group:{
            _id:{post:'$post',owner:'$owner'},
               lastUpdated:{$max:"$lastUpdated"},
            total:{$sum:1}}
        },
        {$match:{
            total:{$gt:1}}
        }
    ]).forEach(function(group){
        var cluckToSave = dbDest.cluck.find({
            post:group._id.post,
            owner: group._id.owner,
            lastUpdated:group.lastUpdated
        })[0]
        dbDest.cluck.remove({
            post:group._id.post,
            owner: group._id.owner
        })
        cluckToSave.cluckAction = "DEBATE"
        dbDest.cluck.save(cluckToSave);
    })