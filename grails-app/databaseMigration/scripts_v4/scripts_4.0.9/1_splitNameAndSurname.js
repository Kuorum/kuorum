var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.find({}).forEach(function(user) {
    var fullName = user.name.trim();
    var name = fullName;
    var surname = "";
    if (fullName.indexOf(' ')>0){
        name = fullName.substr(0,fullName.indexOf(' '));
        surname = fullName.substr(fullName.indexOf(' ')+1); //
    }
    dbDest.kuorumUser.update({_id:user._id},{$set:{'name':name, 'surname':surname}})
})