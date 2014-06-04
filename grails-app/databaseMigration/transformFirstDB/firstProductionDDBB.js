var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumTest");
load("users.js")
load("politicians.js")
load("laws.js")

db.cluckNotification.drop()
db.debateAlertNotification.drop()
db.debateNotification.drop()
db.defendedPostAlert.drop()
db.defendedPostNotification.drop()
db.followerNotification.drop()
db.lawClosedNotification.drop()
db.milestoneNotification.drop()
db.publicMilestoneNotification.drop()

var adminRole = dbDest.roleUser.find({authority:"ROLE_USER"})[0]


db.kuorumUser.update({email:"iduetxe@gmail.com"},{$addToSet:{authorities:adminRole}})
db.kuorumUser.update({email:"mat.nso@gmail.com"},{$addToSet:{authorities:adminRole}})
db.kuorumUser.update({email:"josemaria.garcia@kuorum.org"},{$addToSet:{authorities:adminRole}})