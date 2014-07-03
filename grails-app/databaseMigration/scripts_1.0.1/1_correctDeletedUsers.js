var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumDev");

load("../transformFirstDB/htmlDecoder.js")

dbDest.kuorumUser.find({enabled:false, userType:{$ne:"POLITICIAN"}},{email:1,name:1}).forEach(function(user){
    var nombreEmail = user.email.split("@")[0]
    var domain = user.email.split("@")[1]
    var deletedEmail = "BORRADO_"+nombreEmail+"@NO_EMAIL_"+domain;
    dbDest.kuorumUser.update({_id:user._id},{$set:{'email':deletedEmail}})
});