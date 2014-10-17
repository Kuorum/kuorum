
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var userId = ObjectId("540f282de4b0c738693f13d8")

dbDest.kuorumUser.remove({_id:userId});

dbDest.kuorumUser.update({followers:userId},{$pull:{followers:userId}},{multi:true})
dbDest.kuorumUser.update({following:userId},{$pull:{following:userId}},{multi:true})

dbDest.cluck.remove({owner:userId})
dbDest.cluck.remove({defendedBy:userId})

//TODO SI TIENE POST ES UNA MIERDA QUE NO ESTA HECHA