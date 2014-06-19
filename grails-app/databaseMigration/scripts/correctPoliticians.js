var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumDev");

load("../transformFirstDB/htmlDecoder.js")

dbDest.kuorumUser.update({userType:"POLITICIAN"},{$set:{'personalData.userType':"POLITICIAN", verified:true}}, {multi:true})

dbDest.kuorumUser.find({userType:"POLITICIAN"}).forEach(function(politician){
   politician.email = removeDiacritics(politician.email.replace(/info../g, "info+"))
//    print(politician.email)
    dbDest.kuorumUser.update({_id:politician._id},{$set:{'email':politician.email}})
});