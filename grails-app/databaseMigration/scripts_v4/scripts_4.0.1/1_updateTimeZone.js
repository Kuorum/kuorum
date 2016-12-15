var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update(
    {},
    {$set:{'timeZoneId':"Europe/Madrid"}}, {multi:1}
);
