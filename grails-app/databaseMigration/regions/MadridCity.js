var dbDest = dbDest || connect("localhost:27017/Kuorum");



postalCodesByRange = function (start, end){
    var postalCodes = [];
    for (var i = start; i <=end ; i++){
        postalCodes.push(i+"");
    }
    return postalCodes;

}

var parentRegionCode ="EU-ES-MD-MD";
var regionPostFixCode = "MD";
var regionName="Ciudad de Madrid";
var institutionName="Ayuntamiento de Madrid";
var regionType="LOCAL"

var regionCode=parentRegionCode+"-"+regionPostFixCode;
var parentRegion = dbDest.region.find({iso3166_2:parentRegionCode}).next()


dbDest.region.save({
    "iso3166_2" : regionCode,
    "name" : regionName,
    "superRegion" : parentRegion._id,
    "version" : 0,
    "postalCodes" : postalCodesByRange(28001, 28055),
    "regionType": regionType
});

var region = dbDest.region.find({iso3166_2:regionCode}).next()
dbDest.institution.save({
    "name" : institutionName,
    "region" : region,
    "version" : 0
});
