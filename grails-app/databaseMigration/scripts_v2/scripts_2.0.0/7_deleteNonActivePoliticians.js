
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({userType:"POLITICIAN", enabled:false}).forEach(function (politician){
    dbDest.kuorumUser.update({},{$pull:{following:politician._id}}, {multi:true})
    dbDest.kuorumUser.remove({_id:politician._id})
})