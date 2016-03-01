var dbDest = dbDest || connect("localhost:27017/Kuorum");

var basicRole = dbDest.roleUser.find({authority:"ROLE_USER"})
var politicianRole = dbDest.roleUser.find({authority:"ROLE_POLITICIAN"})

dbDest.kuorumUser.update(
    {'professionalDetails.politicalParty':'Ciudadanos'},
    {
        $set:{
            enabled:true,
            "password" : "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
        },
        $addToSet:{authorities:[basicRole, politicianRole]}
    },
    {multi:true}
)