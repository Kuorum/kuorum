
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.region.find().forEach(function(region){

    region.postalCodes = [region.postalCode];
    dbDest.region.save(region);
})

dbDest.region.ensureIndex( { iso3166_2: 1 }, { unique: true, dropDups: true })


dbDest.kuorumUser.find().forEach(function(user){
    if (user.personalData!= undefined && user.personalData.postalCode != undefined){
        var provincePostalCode = user.personalData.postalCode.substr(0,2)
        var region = dbDest.region.find({postalCodes:provincePostalCode})[0]
        user.personalData.province = region._id
        user.personalData.provinceCode = region.iso3166_2
        print("Usuario "+user.name+"(id: "+user._id+") con postalCode:"+user.personalData.postalCode +"("+provincePostalCode+") asignado a "+region.name+"(CP:"+region.postalCodes+", provinceCode:"+user.personalData.provinceCode+")")
        dbDest.kuorumUser.save(user)
    }else{
        print("SKIPPED => _id: " + user._id +" - "+user.email)
    }
})

dbDest.kuorumUser.find({userType:"POLITICIAN"}).forEach(function(user){
    if (user.institution!= undefined){
        var institution = dbDest.institution.find({_id:user.institution})[0]
        user.politicianOnRegion = institution.region
        dbDest.kuorumUser.save(user)
    }else{
        print("SKIPPED => _id: " + user._id +" - "+user.email)
    }
})

dbDest.lawVote.find().forEach(function(vote){
    var user = dbDest.kuorumUser.find({_id:vote.kuorumUser})[0]
    if (user.personalData!= undefined && user.personalData.postalCode != undefined){
        vote.personalData = user.personalData
        dbDest.lawVote.save(vote)
    }else{
        print("SKIPPED => _id: " + user._id +" - "+user.email)
    }
})