
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.find().forEach(function (project){
    var numPost = dbDest.post.count({project:project._id, published:true})
    dbDest.project.update({_id:project._id},{$set:{'peopleVotes.numPosts':numPost}})
})