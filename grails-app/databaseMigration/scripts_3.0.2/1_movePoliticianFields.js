
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({userType:'POLITICIAN'}).forEach(function(politician) {

    dbDest.kuorumUser.update({_id:politician._id},{
        $set:{
            'careerDetails.university':politician.politicianExtraInfo.university,
            'careerDetails.studies':politician.politicianExtraInfo.studies,
            'careerDetails.school':politician.politicianExtraInfo.school,
            'careerDetails.profession':politician.professionalDetails.profession,
            'careerDetails.cvLink':politician.professionalDetails.cvLink,
            'careerDetails.declarationLink':politician.professionalDetails.declarationLink
        },
        $unset:{
            'politicianExtraInfo.university':'',
            'politicianExtraInfo.studies':'',
            'politicianExtraInfo.school':'',
            'professionalDetails.profession':'',
            'professionalDetails.cvLink':'',
            'professionalDetails.declarationLink':''
        }
    })
})