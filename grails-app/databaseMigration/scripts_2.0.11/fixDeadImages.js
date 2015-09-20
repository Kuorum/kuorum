
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var imageProfile = "https://kuorum.org/uploadedImages/lostImages/profile.png"
var imageAvatar = "https://kuorum.org/uploadedImages/lostImages/avatar.png"
var imagePost = "https://kuorum.org/uploadedImages/lostImages/post.png"
var imageProject = "https://kuorum.org/uploadedImages/lostImages/project.png"

dbDest.kuorumUser.find().forEach(function(user) {
    if (user.avatar != undefined && user.avatar.url.indexOf("https://kuorum.org")>=0){
        URL = imageAvatar;
        print(URL);
        dbDest.kuorumUser.update({_id:user._id},{$set:{'avatar.url':URL}})
    }
})

dbDest.kuorumUser.find({imageProfile:{$exists:1}}).forEach(function(user) {
    if (user.imageProfile != undefined && user.imageProfile.url.indexOf("https://kuorum.org")>=0){
        URL = imageProfile;
        print(URL);
        dbDest.kuorumUser.update({_id:user._id},{$set:{'imageProfile.url':URL}})
    }
})

dbDest.project.find().forEach(function(project) {
    if (project.image != undefined && project.image.url.indexOf("https://kuorum.org")>=0){
        URL = imageProject;
        project.image.url = URL
        dbDest.project.update({_id:project._id},{$set:{'image.url':URL}})
    }

    if (project.updates != undefined && project.updates.length > 0){
        for (i = 0; i < project.updates.length; i++) {
            if (project.updates[i].image != undefined && project.updates[i].image.url.indexOf("https://kuorum.org")>=0){
                project.updates[i].image.url = imageProject
            }
        }
        dbDest.project.save(project);
    }
})

dbDest.post.find({multimedia:{$exists:1}}).forEach(function(post) {
    if (post.multimedia != undefined && post.multimedia.url.indexOf("https://kuorum.org")>=0){
        var URL = post.multimedia.url.substring(7);
        URL = "https://"+URL;
        print(URL);
        dbDest.post.update({_id:post._id},{$set:{'multimedia.url':URL}})
    }
})

dbDest.kuorumFile.find({url:/^http:/}).forEach(function(file) {
    if (file.url != undefined && file.url.indexOf("http://")>=0){
        URL =imagePost;
        print(URL);
        dbDest.kuorumFile.update({_id:file._id},{$set:{'url':URL}})
    }
})

dbDest.project.update({hashtag:"#losDatosCuentan",'updates.image.fileName':'558d4ac2e4b06135431f9783.png'},{$set:{"updates.$.image.url":"https://kuorum.org/uploadedImages/lostImages/losDatosCuentanPublicado.png"}})
