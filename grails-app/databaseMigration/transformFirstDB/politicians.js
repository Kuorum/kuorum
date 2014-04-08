
load("htmlDecoder.js")
dbOrigin = connect("localhost:27017/KuorumWeb");
dbDest = connect("localhost:27017/KuorumDev");


dbOrigin.politician.find().forEach(function(politician){
    var kuorumUser = createKuorumUserFromPolitician(politician)
    dbDest.kuorumUser.insert(kuorumUser)
});


function createKuorumUserFromPolitician(politician){

    var userRoles = db.roleUser.find({authority:"ROLE_USER"})
    var userRole = userRoles.hasNext() ? userRoles.next() : null;
    var id = new ObjectId();
    var kuorumUser = {
        "_class" : "KuorumUser",
        "_id" : id,
        "accountExpired" : false,
        "accountLocked" : false,
        "authorities" : [userRole],
        "dateCreated" : new Date(),
        "email" : user.username,
        "bio":user.defend,
        "userType":"PERSON",
        "avatar":createAvatar(user),
        "enabled" : true,
        "followers" : user.friends,
        "following" : user.friends,
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
        availableMails:[
            "REGISTER_VERIFY_EMAIL",
            "REGISTER_RESET_PASSWORD",
            "REGISTER_RRSS",
            "REGISTER_ACCOUNT_COMPLETED",
            "NOTIFICATION_CLUCK",
            "NOTIFICATION_FOLLOWER",
            "NOTIFICATION_PUBLIC_MILESTONE",
            "NOTIFICATION_DEBATE_USERS",
            "NOTIFICATION_DEBATE_AUTHOR",
            "NOTIFICATION_DEBATE_POLITICIAN",
            "NOTIFICATION_DEFENDED_USERS",
            "NOTIFICATION_DEFENDED_AUTHOR",
            "NOTIFICATION_DEFENDED_BY_POLITICIAN",
            "NOTIFICATION_DEFENDED_POLITICIANS",
            "NOTIFICATION_VICTORY_USERS",
            "NOTIFICATION_VICTORY_DEFENDER",
            "PROMOTION_OWNER",
            "PROMOTION_SPONSOR",
            "PROMOTION_USERS",
            "POST_CREATED_1",
            "POST_CREATED_2",
            "POST_CREATED_3",
            "POST_CREATED_4"
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

function createAvatar(user){
    var id = new ObjectId();
    if (user.pathAvatar != undefined && user.pathAvatar!= null){
        var kuorumFile = {
            "_class":"KuorumFile",
            "_id":id,
            "user":user._id,
            "temporal":false,
            "local":user.pathAvatar.indexOf("http://") > 0,
            "storagePath":storagePath(user.pathAvatar).storagePath,
            "fileName":storagePath(user.pathAvatar).fileName,
            "url":absoluteUrl(user.pathAvatar),
            "fileGroup":"USER_AVATAR"
        }

        db.kuorumFile.insert(kuorumFile)
        return kuorumFile
    }else{
        return null
    }
}

function storagePath(pathAvatar){
    var absoluteRootPath = "/home/tomcat7/uploadedImages/"
    if (pathAvatar.indexOf("http://") == 0){
        //External file (FACEBOOK)
        return {
            "storagePath":null,
            "fileName": null
        }
    }else if (pathAvatar){
        return {
            "storagePath":absoluteRootPath+pathAvatar.substring(0,pathAvatar.lastIndexOf("/")),
            "fileName": pathAvatar.substring(pathAvatar.lastIndexOf("/")+1)
        }
    }else{
        return {
            "storagePath":null,
            "fileName": null
        }
    }
}

function absoluteUrl(pathAvatar){
    var absoluteRootUrl = "http://kuorum.org/uploadedImages/"
    if (pathAvatar.indexOf("http://") == 0){
        //External file (FACEBOOK)
        return pathAvatar
    }else if (pathAvatar){
        return absoluteRootUrl+pathAvatar
    }else{
        return null
    }
}