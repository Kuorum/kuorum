
load("htmlDecoder.js")
dbOrigin = connect("localhost:27017/KuorumWeb");
dbDest = connect("localhost:27017/KuorumDev");


dbOrigin.facebookUser.find().forEach(function(facebookUser){
    var user = dbOrigin.secUser.find({_id:facebookUser.user})[0]
    if (user.enabled && !user.accountLocked){
        var kuorumUser = createKuorumUserFromOldUser(user)
        print("usuario de facebook creado:"+kuorumUser.email)
        dbDest.facebookUser.insert(facebookUser)
        dbDest.kuorumUser.insert(kuorumUser)
    }else{
        print("usuario de facebook no activo:"+user.username)
    }

})


dbOrigin.secUser.find({enabled:true, accountLocked:false}).forEach(function(user){
    var kuorumUser = createKuorumUserFromOldUser(user)
    var numUsersByEmail = dbDest.kuorumUser.find({email:kuorumUser.email}).count()
    if (numUsersByEmail==0){
//        print("usuario creado:"+kuorumUser.email)
        dbDest.kuorumUser.insert(kuorumUser)
    }else{
        print("El usuario ya existia por facebook ("+numUsersByEmail+"):"+kuorumUser.email+"[ID:"+kuorumUser._id+"]")
    }

});


function createKuorumUserFromOldUser(user){

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
        "userType":"PERSON",
        "enabled" : true,
        "followers" : user.friends,
        "following" : user.friends,
        "favorites" : [],
        "numFollowers":user.friends==undefined?0:user.friends.length,
        "language" : "es_ES",
        "lastUpdated" : user.lastUpdated,
        "name" : HtmlDecode(user.name),
//        "password" : user.password,
        "password" : "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", //test
        "passwordExpired" : false,
        "personalData" : {
            _class:"PersonData",
            "birthday" : user.personalData.birthday,
            "gender" : user.personalData.gender,
            "postalCode" : null,
            "regionCode" : null,
            "provinceCode":null,
            "province":null,
            "studies":null,
            "workingSector":null
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
        gamification: {
            numEggs: 0,
            numPlumes:0,
            numCorns: 0,
            activeRole:"ROLE_DEFAULT",
            boughtAwards: ["ROLE_DEFAULT"]
        },
        "subscribers" : [ ],
        "version" : NumberLong(4)
    }
    return kuorumUser
}