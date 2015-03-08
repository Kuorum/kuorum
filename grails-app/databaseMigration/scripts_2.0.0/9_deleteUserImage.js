
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update({},{$unset: {imageProfile:""}},{multi:1})
