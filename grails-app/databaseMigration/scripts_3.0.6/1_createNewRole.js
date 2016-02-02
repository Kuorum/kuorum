

var dbDest = dbDest || connect("localhost:27017/Kuorum");

var role = {
    "authority" : "ROLE_EDITOR",
    "version" : 0
}


dbDest.roleUser.save(role)