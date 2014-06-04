var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/Kuorum");
load("users.js")
load("politicians.js")
load("laws.js")

dbDest.cluckNotification.drop()
dbDest.debateAlertNotification.drop()
dbDest.debateNotification.drop()
dbDest.defendedPostAlert.drop()
dbDest.defendedPostNotification.drop()
dbDest.followerNotification.drop()
dbDest.lawClosedNotification.drop()
dbDest.milestoneNotification.drop()
dbDest.publicMilestoneNotification.drop()

var adminRole = dbDest.roleUser.find({authority:"ROLE_ADMIN"})[0]


dbDest.kuorumUser.update({email:"iduetxe@gmail.com"},{$addToSet:{authorities:adminRole}})
dbDest.kuorumUser.update({email:"mat.nso@gmail.com"},{$addToSet:{authorities:adminRole}})
dbDest.kuorumUser.update({email:"josemaria.garcia@kuorum.org"},{$addToSet:{authorities:adminRole}})