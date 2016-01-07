

var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.dropIndex({alias:1})
dbDest.kuorumUser.createIndex( { alias: 1 }, { unique: true , sparse:true} )