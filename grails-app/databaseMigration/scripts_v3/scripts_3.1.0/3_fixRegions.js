

var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.region.update({iso3166_2:"NA-US-CA-LA-S"},{$set:{iso3166_2:"NA-US-CA-LA-S0"}})

dbDest.region.find().forEach(function(region){
    if (region.iso3166_2.length >5){
        var isoCodeParent = region.iso3166_2.slice(0,-3)
        var cursor = dbDest.region.find({iso3166_2:isoCodeParent})
        if (cursor.hasNext()){
            var parentRegion = cursor.next()
            dbDest.region.update({_id:region._id},{$set:{superRegion:parentRegion._id}})
        }else{
            print(isoCodeParent);
        }
    }
})