var dbDest = dbDest || connect("localhost:27017/Kuorum");

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
            {$set:{'personalData.provinceCode':region.newIso}},
            {multi:true})
        dbDest.kuorumUser.update({'professionalDetails.region.iso3166_2':region.oldIso},
            {$set:{'professionalDetails.region.iso3166_2':region.newIso, 'professionalDetails.region.name':region.name}},
            {multi:true})
        dbDest.kuorumUser.update({'professionalDetails.constituency.iso3166_2':region.oldIso},
            {$set:{'professionalDetails.constituency.iso3166_2':region.newIso, 'professionalDetails.constituency.name':region.name}},
            {multi:true})
        dbDest.project.update({'region.iso3166_2':region.oldIso},
            {$set:{'region.iso3166_2':region.newIso, 'region.name':region.name}},
            {multi:true})

        dbDest.region.find({iso3166_2:{$regex:"^"+region.oldIso+"-"}}).forEach(function(childRegion){
            var newChildCode = region.newIso + childRegion.iso3166_2.substr(region.oldIso.length);
            childRegion.iso3166_2 = newChildCode;
            dbDest.region.save(childRegion);
        })
    })
}

updateRegionsProcess(updateRegionsData);

