
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({'politicianExtraInfo.officialWebSite': {$exists: 1}}).forEach(function (user) {

    dbDest.kuorumUser.update(
        {_id: user._id},
        {
            $set: {'socialLinks.officialWebSite': user.politicianExtraInfo.webSite},
            $unset: {'politicianExtraInfo.webSite': ''}
        })
    }
);

dbDest.kuorumUser.find({'professionalDetails.sourceWebsite': {$exists: 1}}).forEach(function (user) {

        dbDest.kuorumUser.update(
            {_id: user._id},
            {
                $set: {'socialLinks.institutionalWebSite': user.professionalDetails.sourceWebsite},
                $unset: {'professionalDetails.sourceWebsite': ''}
            })
    }
);


