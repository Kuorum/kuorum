var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({}).forEach(function(user) {
    var fullName = user.name;
    var name = fullName.substr(0,fullName.indexOf(' '));
    var surname = fullName.substr(fullName.indexOf(' ')+1); //
    dbDest.kuorumUser.update({_id:user._id},{$set:{'name':name, 'surname':surname}})
})