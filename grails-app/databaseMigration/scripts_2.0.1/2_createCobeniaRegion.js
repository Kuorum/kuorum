var dbDest = dbDest || connect("localhost:27017/Kuorum");

var madrid = dbDest.region.find({iso3166_2:"EU-ES-MD"}).next()

dbDest.region.save({
    "iso3166_2" : "EU-ES-MD-CB",
    "name" : "Cobeña",
    "superRegion" : madrid._id,
    "version" : 0,
    "postalCodes" : ["28863"],
    "regionType": "LOCAL"
});

var cobenia = dbDest.region.find({iso3166_2:"EU-ES-MD-CB"}).next()
dbDest.institution.save({
    "name" : "Ayuntamiento de Cobeña",
    "region" : cobenia,
    "version" : 0
});
