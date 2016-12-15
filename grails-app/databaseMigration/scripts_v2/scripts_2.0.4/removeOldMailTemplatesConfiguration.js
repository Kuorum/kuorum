
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update(
    {},
    {$pull:{availableMails:"PROMOTION_OWNER"}},
    {multi:1}
)

dbDest.kuorumUser.update(
    {},
    {$pull:{availableMails:"PROMOTION_SPONSOR"}},
    {multi:1}
)

dbDest.kuorumUser.update(
    {},
    {$pull:{availableMails:"PROMOTION_USERS"}},
    {multi:1}
)