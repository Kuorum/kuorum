var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.find({domain:'rivas-test.kuorum.org'}).forEach(function(user) {
    var domain = user.domain.trim();
    var adminUser = dbDest.kuorumUser.find({alias:'admin', domain:domain}).next();
    print ("DOMAIN ["+domain+"] => ADMIN: "+adminUser.name + "(id:"+adminUser._id+")  -- USER: "+user.alias+" (id:"+user._id+")" );
    if (!adminUser._id.equals(user._id)){
        dbDest.kuorumUser.update({_id:user._id},{$push:{'following':adminUser._id}});
        dbDest.kuorumUser.update({_id:adminUser._id},{$push:{'followers':user._id}});
    }else{
        print ("-----Skiped")
    }
});


// WIRED USERS
dbDest.kuorumUser.remove({_id:ObjectId('5c98ef95a9aa3136ff7776d0')});