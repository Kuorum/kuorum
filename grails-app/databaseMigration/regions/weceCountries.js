var dbDest = dbDest || connect("localhost:27017/Kuorum");


/// NEW COUNTRIES
var germany = {
    "iso3166_2" : "EU-DE",
    "name" : "Germany",
    "superRegion" : ObjectId("538f3467e4b0f5aaca4edca9"),
    "postalCodes" : [ ],
    "regionType" : "NATION",
    "prefixPhone" : "+49",
    "postalCodeHandlerType" : "STANDARD_FIVE_DIGITS"
}

dbDest.region.save(germany);

var lithuania = {
    "iso3166_2" : "EU-LT",
    "name" : "Lithuania",
    "superRegion" : ObjectId("538f3467e4b0f5aaca4edca9"),
    "postalCodes" : [ ],
    "regionType" : "NATION",
    "prefixPhone" : "+370",
    "postalCodeHandlerType" : "STANDARD_FIVE_DIGITS"
}
dbDest.region.save(lithuania);

var italia = {
    "iso3166_2" : "EU-IT",
    "name" : "Italia",
    "superRegion" : ObjectId("538f3467e4b0f5aaca4edca9"),
    "postalCodes" : [ ],
    "regionType" : "NATION",
    "prefixPhone" : "+39",
    "postalCodeHandlerType" : "STANDARD_FIVE_DIGITS"
}

dbDest.region.save(italia);