var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.update({status:"OPEN"},{$set:{deadline:new ISODate("2015-03-31")}},{multi:true});

dbDest.project.update({status:{$ne:"OPEN"}},{$set:{deadline:new ISODate("2014-12-31")}},{multi:true});
