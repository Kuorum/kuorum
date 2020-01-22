
load("htmlDecoder.js")
load("imageHelper.js")
var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumDev");


dbOrigin.politician.find().forEach(function(politician){
    var kuorumUser = createKuorumUserFromPolitician(politician)
    if (kuorumUser != null){
        dbDest.kuorumUser.insert(kuorumUser)
    }else{
        print("No se ha podido crear el politico "+politician.username + " " +politician.surname)
    }

});


function createKuorumUserFromPolitician(politician){

    var userRoles = dbDest.roleUser.find({authority:"ROLE_USER"})
    var userRole = userRoles.hasNext() ? userRoles.next() : null;

    var userRoles = dbDest.roleUser.find({authority:"ROLE_POLITICIAN"})
    var politicianRole = userRoles.hasNext() ? userRoles.next() : null;

    var politicalParties = dbOrigin.politicalParty.find({_id:politician.politicalParty})
    var politicalPartyOrg = politicalParties.hasNext() ? politicalParties.next() : null;

    var spain = dbDest.region.find({iso3166_2:"EU-ES" })[0]
    var parliamentaryGroups = dbDest.parliamentaryGroup.find({name:politicalPartyOrg.politicalGroup.name, 'region._id':spain._id})
    var parliamentaryGroup = parliamentaryGroups.hasNext() ? parliamentaryGroups.next() : null;

    if (parliamentaryGroup == null){
        print("No se ha encontrado el grupo parlamenterio "+politicalPartyOrg.politicalGroup.name)
        return null
    }

    var institutions = dbDest.institution.find({name:"Parlamento Español"})
    var institution = institutions.hasNext() ? institutions.next() : null;
//    var region = dbDest.region.find({"iso3166_2" : "EU-ES"})[0]
    var provinceName = politician.province.split('_').slice(-1)[0]
    var region = dbDest.region.find({name:{ $regex: provinceName, $options: 'i' }, iso3166_2:/EU-ES-.{2}-.*/ })[0]
    if (region == undefined){
        if (provinceName == "GUIPUZCUA") region = dbDest.region.find({"iso3166_2" : "EU-ES-PV-SS"})[0]
        else if (provinceName == "VIZCAYA") region = dbDest.region.find({"iso3166_2" : "EU-ES-PV-BI"})[0]
        else if (provinceName == "GERONA") region = dbDest.region.find({"iso3166_2" : "EU-ES-CT-GE"})[0]
        else if (provinceName == "CORUNIA") region = dbDest.region.find({"iso3166_2" : "EU-ES-GA-CO"})[0]
        else if (provinceName == "ALMERIA") region = dbDest.region.find({"iso3166_2" : "EU-ES-AN-AL"})[0]
        else if (provinceName == "LEON") region = dbDest.region.find({"iso3166_2" : "EU-ES-CL-LE"})[0]
        else if (provinceName == "ORENSE") region = dbDest.region.find({"iso3166_2" : "EU-ES-GA-OR"})[0]
        else if (provinceName == "MALAGA") region = dbDest.region.find({"iso3166_2" : "EU-ES-AN-MA"})[0]
        else if (provinceName == "LERIDA") region = dbDest.region.find({"iso3166_2" : "EU-ES-CT-LL"})[0]
        else if (provinceName == "ATURIAS") region = dbDest.region.find({"iso3166_2" : "EU-ES-AS-AS"})[0]
        else if (provinceName == "CACERES") region = dbDest.region.find({"iso3166_2" : "EU-ES-EX-CC"})[0]
        else if (provinceName == "CADIZ") region = dbDest.region.find({"iso3166_2" : "EU-ES-AN-CA"})[0]
        else if (provinceName == "ALABA") region = dbDest.region.find({"iso3166_2" : "EU-ES-PV-VI"})[0]
        else if (provinceName == "AVILA") region = dbDest.region.find({"iso3166_2" : "EU-ES-CL-AV"})[0]
        else if (provinceName == "CASTELLON") region = dbDest.region.find({"iso3166_2" : "EU-ES-CV-CS"})[0]
//        else if (provinceName == "MELILLA") region = dbDest.region.find({"iso3166_2" : "EU-ES-ML-ML"})[0]
//        else if (provinceName == "MADRID") region = dbDest.region.find({"iso3166_2" : "EU-ES-MD-MD"})[0]
        else print("No se ha encontrado la region "+provinceName)

    }


    var id = new ObjectId();
    var kuorumUser = {
        "_class" : "KuorumUser",
        "_id" : id,
        "parliamentaryGroup":parliamentaryGroup._id,
        "institution":institution._id,
        "accountExpired" : false,
        "accountLocked" : false,
        "authorities" : [userRole, politicianRole],
        "dateCreated" : new Date(),
        "email" : ("info+"+politician.name+politician.surname+"@kuorum.org").replace(/ /g,""),
        "bio":politician.history,
        "userType":"POLITICIAN",
        "avatar":createAvatar(id,"USER_AVATAR", politician.photo),
        "enabled" : false,
        "followers" : [],
        "following" : [],
        "favorites" : [],
        "numFollowers":0,
        "language" : "es_ES",
        "lastUpdated" : politician.lastUpdated,
        "lastNotificationChecked":new Date(),
        "name" : HtmlDecode(politician.name.trim() + " " +politician.surname.trim()),
        "password" : "passPolitico",
        "passwordExpired" : false,
        "personalData" : {
            _class:"PersonData",
            "birthday" : null,
            "gender" : "MALE",
            "postalCode" : region.postalCode,
            "provinceCode":region.iso3166_2,
            "province":region
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
