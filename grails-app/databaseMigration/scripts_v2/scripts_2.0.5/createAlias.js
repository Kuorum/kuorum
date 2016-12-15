
var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.update(
    {_id:ObjectId("539a978fe4b0da54bcc2cda2")},
    {$set:{alias:"manuelacarmena"}}
)

dbDest.kuorumUser.update(
    {_id:ObjectId("52220293e4b047381ebea0cf")},
    {$set:{alias:"iduetxe"}}
)


dbDest.kuorumUser.createIndex( { alias: 1 } )


dbDest.kuorumUser.update(
    {_id:ObjectId("552be1b0e4b0e91aeaa5c733")},
    {$set:{alias:"isabelrguez"}}
)

dbDest.kuorumUser.update(
    {_id:ObjectId("552be1b0e4b0e91aeaa5c733")},
    {$set:{email:"isabel.rodriguez@congreso.es"}}
)

dbDest.kuorumUser.update(
    {_id:ObjectId("551957f5e4b0542cbfbf805a")},
    {$set:{alias:"raul_salinero"}}
)

dbDest.kuorumUser.update(
    {_id:ObjectId("551957f5e4b0542cbfbf805a")},
    {$set:{email:"raul.salinero@iuburgos.es "}}
)

dbDest.kuorumUser.update({email:"federico.buyolo@congreso.es"},{$set:{alias:"fbuyolo"}})
dbDest.kuorumUser.update({email:"carles.campuzano@gmail.com"},{$set:{alias:"carlescampuzano"}})
dbDest.kuorumUser.update({email:"nachosamor@congreso.es"},{$set:{alias:"nachosamor"}})
dbDest.kuorumUser.update({email:"ricardo.sixto@congreso.es"},{$set:{alias:"rsixtoiglesias"}})
dbDest.kuorumUser.update({email:"patricia.hernandez@congreso.es"},{$set:{alias:"patriciahdezgut"}})
dbDest.kuorumUser.update({email:"pablo.martin@congreso.es"},{$set:{alias:"pablomartin666"}})
dbDest.kuorumUser.update({email:"gelorriaga@congreso.es"},{$set:{alias:"gabrielorriaga"}})
dbDest.kuorumUser.update({email:"rafa.larreina@gmail.com"},{$set:{alias:"larreina"}})
dbDest.kuorumUser.update({email:"elenabiurrun@telefonica.net"},{$set:{alias:"biurrun74"}})