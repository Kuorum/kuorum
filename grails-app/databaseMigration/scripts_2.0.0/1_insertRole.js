var dbDest = dbDest || connect("localhost:27017/Kuorum");
dbDest.roleUser.insert( {"authority" : "ROLE_INCOMPLETE_USER", "version" : 0 });
dbDest.roleUser.insert( {"authority" : "ROLE_PASSWORDCHANGED", "version" : 0 });

