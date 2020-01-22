
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({userType:'POLITICIAN'}).forEach(function(politician) {

    if (politician.politicianExtraInfo != undefined){
        dbDest.kuorumUser.update({_id:politician._id},{
            $set:{
                'careerDetails.university':politician.politicianExtraInfo.university,
                'careerDetails.school':politician.politicianExtraInfo.school
            },
            $unset:{
                'politicianExtraInfo.university':'',
                'politicianExtraInfo.studies':'',
                'politicianExtraInfo.school':''
            }
        })
    }

    if (politician.professionalDetails != undefined){
        dbDest.kuorumUser.update({_id:politician._id},{
            $set:{
                'careerDetails.profession':politician.professionalDetails.profession,
                'careerDetails.cvLink':politician.professionalDetails.cvLink,
                'careerDetails.declarationLink':politician.professionalDetails.declarationLink
            },
            $unset:{
                'professionalDetails.profession':'',
                'professionalDetails.cvLink':'',
                'professionalDetails.declarationLink':''
            }
        })
    }
})