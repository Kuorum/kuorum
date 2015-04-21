var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.region.save({
    "iso3166_2" : 'SA',
    "name" : 'Sudamérica',
    "version" : 0,
    "postalCodes" : [],
    "regionType": 'SUPRANATIONAL'
});


var parentRegionCode ="SA";
var regionPostFixCode = "CO";
var regionName="Colombia";
var institutionName="Congreso de colombia";
var regionType="NATION"

var regionCode=parentRegionCode+"-"+regionPostFixCode;
var parentRegion = dbDest.region.find({iso3166_2:parentRegionCode}).next()


dbDest.region.save({
    "iso3166_2" : regionCode,
    "name" : regionName,
    "superRegion" : parentRegion._id,
    "version" : 0,
    "postalCodes" : [],
    "regionType": regionType
});

var region = dbDest.region.find({iso3166_2:regionCode}).next()
dbDest.institution.save({
    "name" : institutionName,
    "region" : region,
    "version" : 0
});
