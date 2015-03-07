
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.region.update({iso3166_2:'EU-ES'},{$set:{prefixPhone:"+34"}})