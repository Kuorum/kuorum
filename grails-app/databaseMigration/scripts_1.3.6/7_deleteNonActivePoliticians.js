
var dbDest = dbDest || connect("localhost:27017/Kuorum");

db.kuorumUser.find({userType:"POLITICIAN", enabled:false}).forEach(function (politician){
    db.kuorumUser.update({},{$pull:{following:politician._id}}, {multi:true})
    db.kuorumUser.remove({_id:politician._id})
})