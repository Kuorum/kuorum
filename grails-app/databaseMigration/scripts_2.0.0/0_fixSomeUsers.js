var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.remove({followers:{$exists:0}})