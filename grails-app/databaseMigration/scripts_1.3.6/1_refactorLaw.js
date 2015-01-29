var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.drop();
dbDest.projectClosedNotification.drop();
dbDest.law.renameCollection("project");
dbDest.lawClosedNotification.renameCollection("projectClosedNotification");
dbDest.lawVote.renameCollection("projectVote");
dbDest.kuorumFile.update({fileGroup: 'LAW_IMAGE'},{$set:{fileGroup:'PROJECT_IMAGE'}}, {multi:true});
