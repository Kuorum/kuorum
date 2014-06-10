
var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumDev");

var equoId =ObjectId("526f8d65e4b0fdd7d1894997")
var ecologistasId = ObjectId("52ea1b22e4b0fd3b24dc9dc8")
var pacmaId = ObjectId("52f82043e4b0093e42230637");

var enterpriseIds = [equoId, ecologistasId, pacmaId]
enterpriseIds.forEach(function (enterpriseId){
        dbOrigin.secUser.find({followingEnterprises: enterpriseId}, {name: 1, email: 1}).forEach(function (user) {
            var exists = dbDest.kuorumUser.find({_id: user._id}).count()
            if (exists == 0) {
                print("no existe el usuario " + user.email)
            } else {
                print(user._id+ " <- "+ enterpriseId)
                dbDest.kuorumUser.update({_id:user._id},{$addToSet:{following:enterpriseId}})
                var err = dbDest.runCommand( { getLastError: 1, j: "true" }).err
                if (err){
                    print(err + " => "+user.name)
                }
            }
        });
    });
