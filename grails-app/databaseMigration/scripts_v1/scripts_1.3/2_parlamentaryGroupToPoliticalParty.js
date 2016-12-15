
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.parliamentaryGroup.renameCollection("politicalParty")

dbDest.politicalParty.find().forEach(function(politicalParty){

    politicalParty.institution = null;
    politicalParty.region = null;
    dbDest.politicalParty.save(politicalParty);
})

dbDest.kuorumUser.find().forEach(function(user){

    if (user.parliamentaryGroup!=undefined){
        user.politicalParty = user.parliamentaryGroup;
        user.parliamentaryGroup = null
        print("Actualizado politco "+user.name)
        dbDest.kuorumUser.save(user)
    }
})