
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumFile.find({}).forEach(function(file) {
    dbDest.kuorumFile.update({_id:file._id},{$set:{'urlThumb':file.url}})
})

dbDest.kuorumFile.find({fileType:"YOUTUBE"}).forEach(function(file) {
    dbDest.kuorumFile.update({_id:file._id},{$set:{'urlThumb':"https://img.youtube.com/vi/"+file.fileName+"/maxresdefault.jpg"}})
})

dbDest.post.find({multimedia:{$exists:1}}).forEach(function(post) {
    var multimedia = dbDest.kuorumFile.find({_id:post.multimedia._id}).next()
    dbDest.post.update({_id:post._id},{$set:{'multimedia':multimedia}})
})

dbDest.kuorumUser.find({avatar:{$exists:1}}).forEach(function(user) {
    if (user.avatar != undefined && user.avatar != null){
        var avatar = dbDest.kuorumFile.find({_id:user.avatar._id}).next()
        dbDest.kuorumUser.update({_id:user._id},{$set:{'avatar':avatar}})
    }
})

dbDest.kuorumUser.find({imageProfile:{$exists:1}}).forEach(function(user) {
    if (user.imageProfile != undefined && user.imageProfile != null){
        var imageProfile = dbDest.kuorumFile.find({_id:user.imageProfile._id}).next()
        dbDest.kuorumUser.update({_id:user._id},{$set:{'imageProfile':imageProfile}})
    }
})

