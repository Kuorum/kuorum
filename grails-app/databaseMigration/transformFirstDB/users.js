
load("htmlDecoder.js")
load("imageHelper.js")
var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumDev");


var numFacebook = 0
var numFacebookError = 0
dbOrigin.facebookUser.find().forEach(function(facebookUser){
    var user = dbOrigin.secUser.find({_id:facebookUser.user})[0]
    if (user.enabled && !user.accountLocked){
        var kuorumUser = createKuorumUserFromOldUser(user)
//        print("usuario de facebook creado:"+kuorumUser.email)
        dbDest.facebookUser.insert(facebookUser)
        dbDest.kuorumUser.insert(kuorumUser)
        var err = dbDest.runCommand( { getLastError: 1, j: "true" }).err
        if (err){
            print(err + " => "+user.username)
            numFacebookError++
        }else{
            numFacebook++
        }
    }else{
        print("usuario de facebook no activo:"+user.username)
    }

})

var numSkipped = 0
var numLoaded = 0
var numLocked = 0
var numDisabled = 0
//dbOrigin.secUser.find({enabled:true, accountLocked:false}).forEach(function(user){
dbOrigin.secUser.find().forEach(function(user){

    if (!user.enabled){
        print("usuario desactivado:"+user.username)
        numDisabled++
        return
    }
    if (user.accountLocked){
        print("usuario bloqueado:"+user.username)
        numLocked++
        return
    }
    var kuorumUser = createKuorumUserFromOldUser(user)
    var numUsersByEmail = dbDest.kuorumUser.find({email:kuorumUser.email}).count()
    if (numUsersByEmail==0){
//        print("usuario creado:"+kuorumUser.email)
        dbDest.kuorumUser.insert(kuorumUser)
        numLoaded ++
    }else{
        numSkipped ++
//        print(numSkipped+":El usuario ya existia por facebook ("+numUsersByEmail+"):"+kuorumUser.email+"[ID:"+kuorumUser._id+"]")
    }

});

print("#### LIMIPIANDO AMIGOS QUE NO EXISTEN ######")
var deletedFriends = 0
dbDest.kuorumUser.find({email:/^(?!.*@example.com$)/}).forEach(function(user){
    user.followers.forEach(function(userId){
        var exists = dbDest.kuorumUser.find({_id:userId}).count()
        if (exists == 0){
            dbDest.kuorumUser.update({followers:userId},{$pull:{followers:userId}}, {multi:true})
            dbDest.kuorumUser.update({following:userId},{$pull:{following:userId}}, {multi:true})
            var deletedUser = dbOrigin.secUser.find({_id:userId})[0]
            print("Removing user "+deletedUser.username+" from followers/following")
            deletedFriends++
        }
    })
    user.following.forEach(function(userId){
        var exists = dbDest.kuorumUser.find({_id:userId}).count()
        if (exists == 0){
            dbDest.kuorumUser.update({followers:userId},{$pull:{followers:userId}}, {multi:true})
            dbDest.kuorumUser.update({following:userId},{$pull:{following:userId}}, {multi:true})
        }
    })
})

var notLoaded = dbOrigin.secUser.count({$or:[{enabled:false}, {accountLocked:true}]})
print("######### RESUMEN IMPORT #########")
print("Usuarios cargados a trav�s de facebook:"+numFacebook)
print("Usuarios fallidos a trav�s de facebook:"+numFacebookError)
print("Usuarios saltados porque ya se hab�an cargado con facebook: "+numSkipped)
print("Usuarios saltados por no estar activos: "+numDisabled)
print("Usuarios saltados por estar bloqueados(No llegaron a confirmar el email): "+numLocked)
print("Usuarios eliminados como followers/following: "+deletedFriends)
print("Usuarios cargados: "+numLoaded)
print("--------------------------")
print("Usuarios totales cargados: "+(numLoaded + numFacebook))
print("Usuarios totales desechados: "+notLoaded +numFacebookError)
print("--------------------------")
print("Usuarios totales en la BBDD original: "+dbOrigin.secUser.count())
print("Usuarios totales en la BBDD nueva(sin @example.com): "+dbDest.kuorumUser.count({email:/^(?!.*@example.com$)/}))
print("Usuarios totales en la BBDD nueva(con @example.com): "+dbDest.kuorumUser.count({}))


function createKuorumUserFromOldUser(user){

    var userRoles = dbDest.roleUser.find({authority:"ROLE_USER"})
    var userRole = userRoles.hasNext() ? userRoles.next() : null;

    var kuorumUser = {
        "_class" : "KuorumUser",
        "_id" : user._id,
        "accountExpired" : false,
        "accountLocked" : false,
        "authorities" : [userRole],
        "dateCreated" : user.dateCreated,
        "email" : user.username.toLowerCase(),
        "verified": false,
        "bio":HtmlDecode(user.defend),
        "userType":"PERSON",
        "avatar":createAvatar(user._id,"USER_AVATAR", user.pathAvatar),
        "enabled" : true,
        "followers" : user.friends || [],
        "following" : user.friends || [],
        "favorites" : [],
        "numFollowers":user.friends==undefined?0:user.friends.length,
        "language" : "es_ES",
        "lastUpdated" : user.lastUpdated,
        "lastNotificationChecked":new Date(),
        "name" : HtmlDecode(user.name),
        "password" : user.password,
        "passwordExpired" : false,
        "personalData" : {
            _class:"PersonData",
            "birthday" : user.personalData.birthDay,
            "gender" : user.personalData.gender  || "FEMALE",
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
//            "SUSTAINABLE_MOBILITY",
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