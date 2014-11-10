
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({userType:"POLITICIAN"}).forEach(function(politician){
    var debates = dbDest.post.count({'debates.kuorumUserId':politician._id});
    var victories = dbDest.post.count({'defender':politician._id, victory:true});
    var defenders = dbDest.post.count({'defender':politician._id});
    var politicianActivity={
        numVictories:victories,
        numDefends:defenders,
        numDebates:debates
    }
    dbDest.kuorumUser.update({_id:politician._id},{$set:{politicianActivity:politicianActivity}});
});