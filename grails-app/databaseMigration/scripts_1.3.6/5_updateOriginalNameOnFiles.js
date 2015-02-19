
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumFile.update({},{$set:{originalName:'XX'}},{multi:true});

dbDest.kuorumUser.find().forEach(function (user){
    if (user.avatar != undefined){
        dbDest.kuorumUser.update({_id:user._id},{$set:{'avatar.originalName':'XX'}})
    }

    if (user.imageProfile != undefined){
        dbDest.kuorumUser.update({_id:user._id},{$set:{'avatar.imageProfile':'XX'}})
    }
})