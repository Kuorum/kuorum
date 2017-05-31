var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({}).forEach(function(user) {
    var existsRegion = dbDest.kuorumRegion.find({_id:user.personalData.province._id}).hasNext()
    if (!existsRegion){
        print("Has no province ")
        //dbDest.kuorumUser.update({_id:user._id},{$unset:{'personalData.province':''}})
    }

    var existsRegion = dbDest.kuorumRegion.find({_id:user.personalData.country._id}).hasNext()
    if (!existsRegion){
        print("Has no province ")
        //dbDest.kuorumUser.update({_id:user._id},{$unset:{'personalData.country':''}})
    }
})