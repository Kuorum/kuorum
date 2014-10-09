
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.region.find().forEach(function(region){

    region.postalCodes = [region.postalCode];
    dbDest.region.save(region);
})