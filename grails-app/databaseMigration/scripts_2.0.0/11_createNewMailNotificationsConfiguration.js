var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update(
    {},
    {$push:{availableMails:"PROJECT_CREATED_NOTIFICATION"}},
    {multi:1}
)