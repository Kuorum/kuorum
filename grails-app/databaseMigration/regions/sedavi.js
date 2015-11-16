var dbDest = dbDest || connect("localhost:27017/Kuorum");

var valencia = dbDest.region.find({iso3166_2:"EU-ES-CV"}).next()

dbDest.region.save({
    "iso3166_2" : "EU-ES-CV-CB",
    "name" : "Sedaví",
    "superRegion" : valencia._id,
    "version" : 0,
    "postalCodes" : ["46910"],
    "regionType": "LOCAL"
});

