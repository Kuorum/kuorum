

var dbDest = dbDest || connect("localhost:27017/Kuorum");

var role = {
    "authority" : "ROLE_EDITOR",
    "version" : 0
}


dbDest.roleUser.save(role)

var roleEditor = dbDest.roleUser.find({"authority" : "ROLE_EDITOR"}).next()
dbDest.kuorumUser.update({"authorities.authority":"ROLE_ADMIN"}, {$addToSet:{authorities:roleEditor}}, {multi:true})
