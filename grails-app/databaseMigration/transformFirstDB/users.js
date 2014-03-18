

dbOrigin = connect("localhost:27017/KuorumWeb");
dbDest = connect("localhost:27017/KuorumDev");

//db.notification.update({},{$set:{dismiss:false}},{multi:true})
//db.message.update({_class: "NormalDebate"},{$set:{convertAsLeader:false}},{multi:true})
//db.secUser.remove({"_id": ObjectId("52927047e4b02ef3bc0ed83f")})

dbOrigin.secUser.find({enabled:true, accountLocked:false}).forEach(function(user){
    var kuorumUser = {
        "_class" : "KuorumUser",
        "_id" : user._id,
        "accountExpired" : false,
        "accountLocked" : false,
        "authorities" : [
            {
                "_id" : NumberLong(1),
                "authority" : "ROLE_USER",
                "version" : NumberLong(0)
            }
        ],
        "dateCreated" : user.dateCreated,
        "email" : user.username,
        "enabled" : true,
        "followers" : user.friends,
        "following_$$manyToManyIds" : user.friends,
        "language" : "es_ES",
        "lastUpdated" : user.lastUpdated,
        "name" : user.name,
        "password" : user.password,
        "passwordExpired" : false,
        "personalData" : {
            "birthday" : user.personalData.birthday,
            "gender" : user.personalData.gender,
//            "postalCode" : "28001",
//            "regionCode" : "EU-SP-MD",
//            "version" : NumberLong(0)
        },
        "relevantCommissions" : [
            "JUSTICE",
            "CONSTITUTIONAL",
            "AGRICULTURE",
            "NUTRITION_AND_ENVIRONMENT",
            "FOREIGN_AFFAIRS",
            "RESEARCH_DEVELOP",
            "CULTURE",
            "DEFENSE",
            "ECONOMY",
            "EDUCATION_SPORTS",
            "EMPLOY_AND_HEALTH_SERVICE",
            "PUBLIC_WORKS",
            "TAXES",
            "INDUSTRY",
            "DOMESTIC_POLICY",
            "BUDGETS",
            "HEALTH_CARE",
            "EUROPE_UNION",
            "DISABILITY",
            "ROAD_SAFETY",
            "SUSTAINABLE_MOBILITY",
            "OTHERS"
        ],
        "subscribers" : [ ],
        "version" : NumberLong(4)
    }
    print("usuario creado:"+kuorumUser.email)
    dbDest.kuorumUser.insert(kuorumUser)
});