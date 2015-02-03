
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumFile.update({},{$set:{originalName:'XX'}},{multi:true});