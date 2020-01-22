
db = connect("localhost:27017/KuorumDev");
load("htmlDecoder.js")

db.kuorumUser.update({},{$set:{"password" : "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"}},{"upsert":false, "multi":true}) //test

var madrid = db.region.find({"iso3166_2" : "EU-ES-MD-MD"})[0]

//PERSONAL DATA
db.kuorumUser.update(
    {"personalData.postalCode":{$exists:1}, userType:{$ne:"ORGANIZATION"}},
    {$set:{
        "personalData.postalCode":28220,
        "personalData.provinceCode":madrid.iso3166_2,
        "personalData.province":madrid._id,
        "personalData.birthday":new Date()
    }},
    {"upsert":false, "multi":true}) //test

//SET GENDER IF IS NOT SET
db.kuorumUser.update(
    {"personalData.gender":{$exists:0}, userType:{$ne:"ORGANIZATION"}},
    {$set:{
        "personalData.gender":"MALE"
    }},
    {"upsert":false, "multi":true}) //test

//SET GENDER IF IS NOT SET AND IS AN ORGANIZATION
db.kuorumUser.update(
    {"personalData.gender":{$exists:0}, userType:"ORGANIZATION"},
    {$set:{
        "personalData.gender":"ORGANIZATION"
    }},
    {"upsert":false, "multi":true}) //test
