
var dbOrigin = dbOrigin || connect("localhost:27017/KuorumWeb");
var dbDest = dbDest || connect("localhost:27017/KuorumDev");

load("htmlDecoder.js")

dbDest.kuorumUser.update({},{$set:{"password" : "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"}},{"upsert":false, "multi":true}) //test

// EMAIL
dbDest.kuorumUser.find({email:/^(?!.*@example.com$)/}).forEach(function(user){
    user.email = removeDiacritics("info+"+user.email.replace(/@/g,'').replace(/\./g,'').toLowerCase()+"@kuorum.org")
    dbDest.kuorumUser.save(user)
})