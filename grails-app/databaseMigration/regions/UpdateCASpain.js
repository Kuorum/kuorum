var dbDest = dbDest || connect("localhost:27017/Kuorum");

var spain = dbDest.region.find({iso3166_2:"EU-ES"}).next()

dbDest.region.update({postalCodes:null},{$set:{postalCodes:[]}},{multi:true})

var updateRegionsData = [
    {"oldIso":"EU-ES-MD", "newIso":"EU-ES-CO", "name":"Comunidad de Madrid"},
    {"oldIso":"EU-ES-CO-MD", "newIso":"EU-ES-CO-MA", "name":"Madrid"},
    {"oldIso":"EU-ES-CO-MA-MD", "newIso":"EU-ES-CO-MA-MA", "name":"Madrid"},
    {"oldIso":"EU-ES-PV", "newIso":"EU-ES-PV", "name":"Pais Vasco"}
]

function updateRegionsProcess(updateRegions){

    updateRegions.forEach(function(region) {

        print("Updating "+region.oldIso)
        dbDest.region.update    ({iso3166_2:region.oldIso},
            {$set:{iso3166_2:region.newIso, name:region.name}})
        dbDest.kuorumUser.update({'personalData.provinceCode':region.oldIso},
            {$set:{'personalData.provinceCode':region.newIso}})
        dbDest.kuorumUser.update({'professionalDetails.region.iso3166_2':region.oldIso},
            {$set:{'professionalDetails.region.iso3166_2':region.newIso, 'professionalDetails.region.name':region.name}})
        dbDest.kuorumUser.update({'professionalDetails.constituency.iso3166_2':region.oldIso},
            {$set:{'professionalDetails.constituency.iso3166_2':region.newIso, 'professionalDetails.region.name':region.name}})
        dbDest.project.update({'region.iso3166_2':region.oldIso},
            {$set:{'region.iso3166_2':region.newIso, 'region.name':region.name}})

        dbDest.region.find({iso3166_2:{$regex:"^"+region.oldIso+"-"}}).forEach(function(childRegion){
            print(childRegion.iso3166_2.substr(region.oldIso.length));
            var newChildCode = region.newIso + childRegion.iso3166_2.substr(region.oldIso.length);
            childRegion.iso3166_2 = newChildCode;
            dbDest.region.save(childRegion);
        })
    })
}

updateRegionsProcess(updateRegionsData);


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