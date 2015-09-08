
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var uk = dbDest.region.find({iso3166_2:"EU-UK"}).next()

dbDest.project.update({hashtag:/sayNoToFracking/},{$set:{region:uk}})
