
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.post.find({published:false}).forEach(function(post){

    var law = dbDest.law.find({_id:post.law})[0]
    var newCluck = {
        cluckAction:"CREATE",
        isFirstCluck:true,
        owner:post.owner,
        postOwner:post.postOwner,
        law:law._id,
        post:post._id,
        region:law.region,
        dateCreated:post.dateCreated,
        lastUpdated:post.lastUpdated
    }
    dbDest.cluck.save(newCluck);
    dbDest.post.update({_id:post._id},{$set:{published:true}});
    print("Published : "+post._id)
    printjson(newCluck);

})