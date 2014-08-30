var dbDest = dbDest || connect("localhost:27017/KuorumDev");

dbDest.kuorumUser.find().forEach(function(user){

    dbDest.post.find({owner:user._id}).forEach(function(post){
        var firstCluck = dbDest.cluck.find({post:post._id, owner:post.owner})[0]
        if (firstCluck == undefined){
            print("Post sin firstCluck: "+post._id+"=> post published="+post.published)
        }else{
            dbDest.post.update({_id:post._id}, {$set:{firstCluck:firstCluck._id}})
            print("Post actualizado:"+post._id)
        }
    });
})