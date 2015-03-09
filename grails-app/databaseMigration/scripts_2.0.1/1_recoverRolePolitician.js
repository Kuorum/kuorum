var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.update({userType:"POLITICIAN"},{$pull:{authorities:{'authority':'ROLE_POLITICIAN'}}},{multi:1})
dbDest.kuorumUser.update({userType:"POLITICIAN"},{$push:{authorities:{ "_id" : ObjectId("538f3468e4b0f5aaca4edd07"), "authority" : "ROLE_POLITICIAN", "version" : 0 }}},{multi:1})
