

var africaRaw = {
    "iso3166_2" : "AF",
    "name" : "Africa",
    "postalCodes" : [ ],
    "regionType" : "SUPRANATIONAL"
}

dbDest.region.save(africaRaw);

var africa = dbDest.region.find({iso3166_2:"AF"}).next()

/// NEW COUNTRIES
var uganda = {
    "iso3166_2" : "AF-UG",
    "name" : "Uganda",
    "superRegion" : africa._id,
    "postalCodes" : [ ],
    "regionType" : "NATION",
    "prefixPhone" : "+256",
    "postalCodeHandlerType" : "STANDARD_FIVE_DIGITS"
}

dbDest.region.save(uganda);