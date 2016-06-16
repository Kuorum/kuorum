var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update({userType:"POLITICIAN", 'careerDetails.cvLink':{$not:/^http/}}, {$unset:{'careerDetails.cvLink':""}}, {multi:1});

dbDest.kuorumUser.find({'careerDetails.cvLink':{$exists:1}},{_id:1, 'careerDetails.cvLink':1}).forEach(function(user){
    var link = user.careerDetails.cvLink;
    var file = {
        _id:ObjectId(),
        "_class" : "KuorumFile",
        "userId" : user._id,
        "temporal" : false,
        "local" : false,
        "url" : link,
        "fileGroup" : "PDF",
        "fileType" : "AMAZON_IMAGE",
        "alt" : "CV.pdf",
        "version" : NumberLong(1),
        "originalName" : "CV.pdf",
        "urlThumb" : link
    }
    dbDest.kuorumFile.save(file)
    dbDest.kuorumUser.update({_id:user._id},{$set:{'careerDetails.cvLink':file}});
});

dbDest.kuorumUser.update({userType:"POLITICIAN", 'careerDetails.declarationLink':{$not:/^http/}}, {$unset:{'careerDetails.declarationLink':""}}, {multi:1});

dbDest.kuorumUser.find({'careerDetails.declarationLink':{$exists:1}},{_id:1, 'careerDetails.declarationLink':1}).forEach(function(user){
    var link = user.careerDetails.declarationLink;
    var file = {
        _id:ObjectId(),
        "_class" : "KuorumFile",
        "userId" : user._id,
        "temporal" : false,
        "local" : false,
        "url" : link,
        "fileGroup" : "PDF",
        "fileType" : "AMAZON_IMAGE",
        "alt" : "CV.pdf",
        "version" : NumberLong(1),
        "originalName" : "Declaracion.pdf",
        "urlThumb" : link
    }
    dbDest.kuorumFile.save(file)
    dbDest.kuorumUser.update({_id:user._id},{$set:{'careerDetails.declarationLink':file}});
});



