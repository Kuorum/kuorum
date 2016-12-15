
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.post.find().forEach(function(post){
    var user = dbDest.kuorumUser.find({_id:post.owner})[0]
    print("Post: "+post.title)
    post.ownerPersonalData = user.personalData
    dbDest.post.save(post);
})