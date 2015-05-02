
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.region.update({"regionType" : "NATION"},{$set:{postalCodeHandlerType:'STANDARD_FIVE_DIGITS'}}, {multi:true});

dbDest.region.update({"iso3166_2" : "EU-UK"},{$set:{postalCodeHandlerType:'UK'}} );
dbDest.region.update({"iso3166_2" : "LA-CO"},{$set:{postalCodeHandlerType:'STANDARD_SIX_DIGITS'}} );

