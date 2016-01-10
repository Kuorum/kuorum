
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({'politicianExtraInfo.officialWebSite': {$exists: 1}}).forEach(function (user) {

    dbDest.kuorumUser.update(
        {_id: user._id},
        {
            $set: {'socialLinks.webSite': user.politicianExtraInfo.webSite},
            $unset: {'politicianExtraInfo.webSite': ''}
        })
    }
);

