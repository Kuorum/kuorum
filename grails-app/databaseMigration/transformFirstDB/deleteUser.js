
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var userId = ObjectId("56cec414c830fd901e011fc0")

dbDest.kuorumUser.remove({_id:userId});

dbDest.kuorumUser.update({followers:userId},{$pull:{followers:userId}},{multi:true})
dbDest.kuorumUser.update({following:userId},{$pull:{following:userId}},{multi:true})

dbDest.cluck.remove({owner:userId})
dbDest.cluck.remove({defendedBy:userId})

//TODO SI TIENE POST ES UNA MIERDA QUE NO ESTA HECHA
dbDest.post.remove({owner:userId})
dbDest.post.update({'comments.kuorumUserId':userId},{$pull:{'comments':{kuorumUserId:userId}}}, {multi:true})
