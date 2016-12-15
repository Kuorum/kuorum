
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.update(
        {owner:ObjectId("54ce5269e4b0d335a40f5ee3"), status:"OPEN"},
        {$set:{deadline:ISODate("2015-03-22T00:00:00.000Z"), status:"REJECTED"}},
        {multi:1}
)