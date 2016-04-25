
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var userId = ObjectId("5703e603e4b0de7680e8f993")


removeUser = function (dbDest, userId){
    dbDest.kuorumUser.remove({_id:userId});

    dbDest.kuorumUser.update({followers:userId},{$pull:{followers:userId}},{multi:true})
    dbDest.kuorumUser.update({following:userId},{$pull:{following:userId}},{multi:true})

    dbDest.cluck.remove({owner:userId})
    dbDest.cluck.remove({defendedBy:userId})

    //TODO SI TIENE POST ES UNA MIERDA QUE NO ESTA HECHA
    dbDest.post.remove({owner:userId})
    dbDest.post.update({'comments.kuorumUserId':userId},{$pull:{'comments':{kuorumUserId:userId}}}, {multi:true})

}


dbDest.kuorumUser.aggregate([{$match:{userType:"POLITICIAN"}},{$group:{_id:"$name",count:{$sum:1}}},{$match:{count:{$gt:1}}} ]).forEach(function(repe){
    var politicianRepe = dbDest.kuorumUser.find({name:repe._id}).next()
    removeUser(dbDest, politicianRepe._id)
});

