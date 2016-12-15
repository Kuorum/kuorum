var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update({},{$push:{availableMails:"NOTIFICATION_COMMENTED_POST_OWNER"}},{multi:true})
dbDest.kuorumUser.update({},{$push:{availableMails:"NOTIFICATION_VICTORY_USERS"}},{multi:true})
