
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({"politicalParty":{$exists:1}}).forEach(function(politician) {
    var politicalParty = dbDest.politicalParty.find({_id:politician.politicalParty}).next()
    if (politicalParty != undefined){
        dbDest.kuorumUser.update({_id:politician._id},{$set:{'professionalDetails.politicalParty':politicalParty.name}})
    }
})