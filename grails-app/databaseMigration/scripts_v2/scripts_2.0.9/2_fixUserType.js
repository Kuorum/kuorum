
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.find().forEach(function(user){
   dbDest.kuorumUser.update({_id:user._id},{$set:{'personalData.userType':user.userType}})
});
