var dbDest = dbDest || connect("localhost:27017/Kuorum");

var europe = dbDest.region.find({iso3166_2:"EU"}).next()

dbDest.region.save({
    "iso3166_2" : "EU-UK",
    "name" : "United Kingdom",
    "superRegion" : europe._id,
    "version" : 0,
    "postalCodes" : [],
    "regionType" : "NATION",
    "prefixPhone" : "+44"
});

dbDest.region.save({
    "iso3166_2" : "NA",
    "name" : "North America",
    "version" : 0,
    "postalCodes" : [],
    "regionType" : "SUPRANATIONAL" }
);

var northAmerica = dbDest.region.find({iso3166_2:"NA"}).next()

dbDest.region.save({
    "iso3166_2" : "NA-US",
    "name" : "United States of America",
    "superRegion" : northAmerica._id,
    "version" : 0,
    "postalCodes" : [],
    "regionType" : "NATION",
    "prefixPhone" : "+1"
});

