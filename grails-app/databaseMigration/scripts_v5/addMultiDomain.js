var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.kuorumUser.update({enabled:true},{$set:{domain:"test.kuorum.org"}},{multi:true})
dbDest.kuorumUser.delete({alias:{$exists:0}},{name:1, email:1, enabled:1})

// INDICES
dbDest.kuorumUser.createIndex( {email:1, domain:1}, { unique: true } )
dbDest.kuorumUser.createIndex( {alias:1, domain:1}, { unique: true } )

dbDest.kuorumUser.dropIndex("email_1")
dbDest.kuorumUser.dropIndex("alias_1")
dbDest.kuorumUser.dropIndex("professionalDetails.constituency.name")
dbDest.kuorumUser.dropIndex("professionalDetails.region.name")


dbDest.cause.update({},{$set:{domain:"test.kuorum.org"}},{multi:true})
dbDest.cause.dropIndex("name")


/// ?????????????????
dbDest.cause.createIndex( {name:1, domain:1}, { unique: true, name:"domain_cause_idx" } )