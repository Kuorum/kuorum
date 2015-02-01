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