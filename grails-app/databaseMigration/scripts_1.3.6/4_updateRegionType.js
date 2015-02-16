var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.region.find().forEach(function(region){
    var regionType = "";
    if (region.iso3166_2.length <= 2){
        regionType = "SUPRANATIONAL"
    }else if (region.iso3166_2.length == 5){
        regionType = "NATION"
    }else if (region.iso3166_2.length == 8){
        regionType = "STATE"
    }else{
        regionType = "LOCAL"
    }
    dbDest.region.update({_id:region._id},{$set:{'regionType':regionType}})
})

dbDest.project.find().forEach(function(project){
    var region = dbDest.region.find({_id:project.region._id}).next()
    project.region = region;
    dbDest.project.save(project);
})


dbDest.kuorumUser.find({'politicianOnRegion':{$exists:1}}).forEach(function(politician){
    var region = dbDest.region.find({_id:politician.politicianOnRegion._id}).next()
    politician.politicianOnRegion = region;
    print(politician.name +"=> "+region.name + "("+region.regionType+")")
    dbDest.kuorumUser.save(politician);
})

dbDest.kuorumUser.find().forEach(function(user){
    var country = dbDest.region.find({iso3166_2:"EU-ES"}).next()
    if (user.personalData != undefined){
        user.personalData.country = country._id
        dbDest.kuorumUser.save(user);
    }else{
        print("WARN: Usuario sin personalData: " + user.name +" "+user._id)
    }
})