

var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.find().forEach(function(project) {
    var projectOwner = dbDest.kuorumUser.find({_id:project.owner}).next()
    dbDest.project.update(
        {_id:project._id},
        {$set:{
            'indexMetaData.ownerName':projectOwner.name
        }})
})

dbDest.post.find().forEach(function(post) {
    var owner = dbDest.kuorumUser.find({_id:post.owner}).next()
    var project = dbDest.project.find({_id:post.project}).next()
    dbDest.post.update(
        {_id:post._id},
        {$set:{
            'indexMetaData.ownerName':owner.name,
            'indexMetaData.hashtag':project.hashtag,
            'indexMetaData.ownerName':owner.name,
            'indexMetaData.commissions':project.commissions,
            'indexMetaData.regionName':project.region.name,
            'indexMetaData.regionIso3166_2': project.region.iso3166_2
        }})
})
