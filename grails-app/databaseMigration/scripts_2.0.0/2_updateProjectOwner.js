var dbDest = dbDest || connect("localhost:27017/Kuorum");

var ownerOldPost = ObjectId("54ce5269e4b0d335a40f5ee3"); //Congreso

dbDest.project.update({},{$set:{owner:ownerOldPost}},{multi:1})

var buyolo = ObjectId("538f37cd69be8b0266f743c3");
dbDest.project.update({hashtag:"#biblioNacional"},{$set:{owner:buyolo}})

var carlesCampuzano = ObjectId("538f37cd69be8b0266f743f7");
dbDest.project.update({hashtag:"#pobrezaInfantil"},{$set:{owner:carlesCampuzano}})

var gabrielElorriaga = ObjectId("538f37cd69be8b0266f74415");
dbDest.project.update({hashtag:"#sefardies"},{$set:{owner:gabrielElorriaga}})

var enriqueBenitez = ObjectId("5420b0b5e4b0913eb37452ae");
dbDest.project.update({hashtag:"#movSostenible"},{$set:{owner:enriqueBenitez}})
