var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.law.find().forEach(function(law){

    print("updating law "+law._id + "status = "+law.status)
    if (law.status == "OPEN"){
        dbDest.law.update({_id:law._id},{$set:{relevance:1}})
    }else{
        dbDest.law.update({_id:law._id},{$set:{relevance:0}})
    }

})