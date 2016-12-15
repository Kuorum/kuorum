var dbDest = dbDest || connect("localhost:27017/Kuorum");


// External activities
dbDest.kuorumUser.update({},{$unset:{externalPoliticianActivities:''}}, {multi:true});

// Leaning index
dbDest.kuorumUser.update({},{$unset:{politicianLeaning:''}}, {multi:true});

// timeLine
dbDest.kuorumUser.update({},{$unset:{timeLine:''}}, {multi:true});