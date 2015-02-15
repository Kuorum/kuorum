
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.find().forEach(function (project){
    var numPost = dbDest.post.count({project:project._id, published:true})
    var description = project.introduction + "\r\n"+project.description
    var imageName = project.image.fileName
    dbDest.project.update(
        {_id:project._id},
        {$set:{
            'peopleVotes.numPosts':numPost,
            description:description,
            'image.originalName':imageName
            }
        }
    )
})