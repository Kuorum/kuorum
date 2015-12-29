var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.region.update({postalCodes:null},{$set:{postalCodes:[]}},{multi:true})

var updateRegionsData = [
    {"oldIso":"EU-ES-MD", "newIso":"EU-ES-CO", "name":"Comunidad de Madrid"},
    {"oldIso":"EU-ES-CO-MD", "newIso":"EU-ES-CO-MA", "name":"Madrid"},
    {"oldIso":"EU-ES-CO-MA-MD", "newIso":"EU-ES-CO-MA-MA", "name":"Madrid"},
    {"oldIso":"EU-ES-PV", "newIso":"EU-ES-PV", "name":"Pais Vasco"},
    {"oldIso":"EU-UK", "newIso":"EU-GB", "name":"Great Britain"},
    {"oldIso":"EU-GB-NL", "newIso":"EU-GB-IL", "name":"England"}
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
            var oldIso = childRegion.iso3166_2
            var newChildIso  = region.newIso + childRegion.iso3166_2.substr(region.oldIso.length);
            childRegion.iso3166_2 = newChildIso;
            dbDest.region.save(childRegion);
        })

        dbDest.kuorumUser.find({'professionalDetails.region.iso3166_2':{$regex:"^"+region.oldIso+"-"}}).forEach(function(user){
            var oldIso = user.professionalDetails.region.iso3166_2
            var newChildIso  = region.newIso + user.professionalDetails.region.iso3166_2.substr(region.oldIso.length);
            print("Updating professionalDetails.region region: "+oldIso+" to "+newChildIso)
            dbDest.kuorumUser.update({_id:user._id},{$set:{'professionalDetails.region.iso3166_2':newChildIso}})
        })

        dbDest.kuorumUser.find({'personalData.provinceCode':{$regex:"^"+region.oldIso+"-"}}).forEach(function(user){
            var oldIso = user.personalData.provinceCode
            var newChildIso  = region.newIso + user.personalData.provinceCode.substr(region.oldIso.length);
            print("Updating personalData.provinceCode region: "+oldIso+" to "+newChildIso)
            dbDest.kuorumUser.update({_id:user._id},{$set:{'personalData.provinceCode':newChildIso}})
        })

        dbDest.kuorumUser.find({'professionalDetails.constituency.iso3166_2':{$regex:"^"+region.oldIso+"-"}}).forEach(function(user){
            var oldIso = user.professionalDetails.constituency.iso3166_2
            var newChildIso  = region.newIso + user.professionalDetails.constituency.iso3166_2.substr(region.oldIso.length);
            print("Updating professionalDetails.constituency region: "+oldIso+" to "+newChildIso)
            dbDest.kuorumUser.update({_id:user._id},{$set:{'professionalDetails.constituency.iso3166_2':newChildIso}})
        })
        dbDest.project.find({'region.iso3166_2':{$regex:"^"+region.oldIso+"-"}}).forEach(function(project){
            var oldIso = project.region.iso3166_2
            var newChildIso  = region.newIso + project.region.iso3166_2.substr(region.oldIso.length);
            print("Updating project.region region: "+oldIso+" to "+newChildIso)
            dbDest.project.update({_id:project._id},{$set:{'region.iso3166_2':newChildIso}})
        })
    })
}

updateRegionsProcess(updateRegionsData);

