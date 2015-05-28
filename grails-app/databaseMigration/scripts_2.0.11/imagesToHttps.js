
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find().forEach(function(user) {
    if (user.avatar != undefined && user.avatar.url.indexOf("http://kuorum.org")>=0){
        var URL = user.avatar.url.substring(7);
        URL = "https://"+URL;
        print(URL);
        dbDest.kuorumUser.update({_id:user._id},{$set:{'avatar.url':URL}})
    }
})

dbDest.kuorumUser.find({imageProfile:{$exists:1}}).forEach(function(user) {
    if (user.imageProfile != undefined && user.imageProfile.url.indexOf("http://kuorum.org")>=0){
        var URL = user.imageProfile.url.substring(7);
        URL = "https://"+URL;
        print(URL);
        dbDest.kuorumUser.update({_id:user._id},{$set:{'imageProfile.url':URL}})
    }
})

dbDest.project.find({image:{$exists:1}}).forEach(function(project) {
    if (project.image != undefined && project.image.url.indexOf("http://kuorum.org")>=0){
        var URL = project.image.url.substring(7);
        URL = "https://"+URL;
        print(URL);
        dbDest.project.update({_id:project._id},{$set:{'image.url':URL}})
    }
})