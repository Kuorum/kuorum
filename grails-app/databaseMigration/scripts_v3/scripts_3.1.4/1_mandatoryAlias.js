
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({alias:{$exists:0}},{alias:1,name:1, _id:1}).forEach(function(kuorumUser){
    var id = kuorumUser._id +""
    var alias = id.substr(id.length - 15);
    dbDest.kuorumUser.update({_id:kuorumUser._id},{$set:{alias:alias}})
})
