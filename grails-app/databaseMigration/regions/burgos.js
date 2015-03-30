var dbDest = dbDest || connect("localhost:27017/Kuorum");

var burgos = dbDest.region.find({iso3166_2:"EU-ES-CL-BU"}).next()

dbDest.region.save({
    "iso3166_2" : "EU-ES-CL-BU-BU",
    "name" : "Ciudad de Burgos",
    "superRegion" : burgos._id,
    "version" : 0,
    "postalCodes" : ["09001","09002","09003","09004","09005","09006","09007"],
    "regionType": "LOCAL"
});

var burgosCity = dbDest.region.find({iso3166_2:"EU-ES-CL-BU-BU"}).next()
dbDest.institution.save({
    "name" : "Ayuntamiento de Burgos",
    "region" : burgosCity,
    "version" : 0
});
