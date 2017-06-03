var dbDest = dbDest || connect("localhost:27017/Kuorum");


var count = 0;
dbDest.kuorumUser.find({}).forEach(function(user) {

    if (user.personalData != undefined){
        if (user.personalData.province !=undefined){
            var existsRegion = dbDest.kuorumRegion.find({_id:user.personalData.province}).hasNext();
            if (!existsRegion){
                print("Has no province ");
                dbDest.kuorumUser.update({_id:user._id},{$unset:{'personalData.province':''}});
            }
            count ++;
        }

        if (user.personalData.country != undefined){
            var existsRegion = dbDest.kuorumRegion.find({_id:user.personalData.country}).hasNext();
            if (!existsRegion){
                print("Has no Country ");
                dbDest.kuorumUser.update({_id:user._id},{$unset:{'personalData.country':''}})
            }
            count ++;
        }
    }
})
print(count)