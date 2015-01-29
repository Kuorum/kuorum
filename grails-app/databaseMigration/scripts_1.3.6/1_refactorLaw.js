var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.project.drop();
dbDest.projectClosedNotification.drop();
dbDest.law.renameCollection("project");
dbDest.lawClosedNotification.renameCollection("projectClosedNotification");
dbDest.lawVote.renameCollection("projectVote");
dbDest.kuorumFile.update({fileGroup: 'LAW_IMAGE'},{$set:{fileGroup:'PROJECT_IMAGE'}}, {multi:true});

db.kuorumUser.update({"avatar.fileGroup":"LAW_IMAGE"},{$set:{"avatar.fileGroup":"PROJECT_IMAGE"}},{multi:true} )
db.kuorumUser.update({"imageProfile.fileGroup":"LAW_IMAGE"},{$set:{"imageProfile.fileGroup":"PROJECT_IMAGE"}},{multi:true} )
db.post.update({},{$rename:{'law':'project'}}, {multi:true})
db.post.update({"multimedia.fileGroup":"LAW_IMAGE"},{$set:{"multimedia.fileGroup":"PROJECT_IMAGE"}},{multi:1} )
db.project.update({"image.fileGroup":"LAW_IMAGE"},{$set:{"image.fileGroup":"PROJECT_IMAGE"}},{multi:1} )

