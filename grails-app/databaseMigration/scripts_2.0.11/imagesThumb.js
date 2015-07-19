
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumFile.find({url:/^http:/}).forEach(function(file) {
    dbDest.kuorumFile.update({_id:file._id},{$set:{'urlThumb':file.url}})
})


dbDest.kuorumFile.find({fileType:"YOUTUBE"}).forEach(function(file) {
    dbDest.kuorumFile.update({_id:file._id},{$set:{'urlThumb':"https://img.youtube.com/vi/"+file.fileName+"/maxresdefault.jpg"}})
})