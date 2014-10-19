var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.post.find({published:true, "_id" : ObjectId("543e68c9e4b0fe769015ceb7")}).forEach(function(post){
    dbDest.cluck.update({post:post._id},{$set:{debateMembers:[], firstCluck:false}}, {multi:true})
   var firstCluck = dbDest.cluck.find({post:post._id, owner:post.owner})[0]
    if (firstCluck == undefined){
        print("No firstCluck for post "+post._id);
        firstCluck  ={
            "owner" : post.owner,
            "postOwner" : post.owner,
            "defendedBy" : null,
            "sponsors" : [ ],
            "isFirstCluck" : true,
            "law" : post.law,
            "post" : post.id,
            "dateCreated" : post.dateCreated,
            "lastUpdated" : post.dateCreated,
            "debateMembers" : [ ]
        }
    }
    var debaters = []
    var lastUpdated = firstCluck.lastUpdated
    if (post.debates != undefined){
        print("Post con debates: " + post.debates.length)
        post.debates.forEach(function(debate){
            debaters.push(debate.kuorumUserId)
            lastUpdated = debate.dateCreated
        });
    }
    firstCluck.debateMembers = debaters
    firstCluck.lastUpdated = lastUpdated
    firstCluck.isFirstCluck = true
    dbDest.cluck.save(firstCluck)

    post.firstCluck = firstCluck._id
    dbDest.post.save(post)
});