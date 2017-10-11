var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.find({'avatar.url':/^http:/},{avatar:1}).forEach(function(user){
    dbDest.kuorumUser.update({_id:user._id},{$unset:{'avatar':''}})
    dbDest.kuorumFile.remove({_id:user.avatar._id})
});
dbDest.kuorumUser.find({'imageProfile.url':/^http:/},{imageProfile:1}).forEach(function(user){
    dbDest.kuorumUser.update({_id:user._id},{$unset:{'imageProfile':''}})
    dbDest.kuorumFile.remove({_id:user.imageProfile._id})
});