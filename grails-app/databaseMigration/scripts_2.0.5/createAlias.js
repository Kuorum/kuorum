
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update(
    {_id:ObjectId("539a978fe4b0da54bcc2cda2")},
    {$set:{alias:"manuelacarmena"}}
)

dbDest.kuorumUser.update(
    {_id:ObjectId("52220293e4b047381ebea0cf")},
    {$set:{alias:"iduetxe"}}
)


dbDest.kuorumUser.createIndex( { alias: 1 } )
