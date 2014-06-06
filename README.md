# web
Kuorum es una red social de democracia participativa online que amplifica tu voz en las instituciones.



## Detalles para montar el entorno
* Recursos
* Intellij
* Mongodb 2.6.1
* Grails 2.3.7
* Java 1.7

Crear una carpeta para el desarrollo
var DESARROLLO=/home/user/kuorum

Instalamos Java 1.7 (JDK)

Descargamos grails en $DESARROLLO/grails
http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-2.3.7.zip

Descargamos mongo en $(DESARROLLO)/mongo
* Linux (64 bits) https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.6.1.tgz
* Descomprimimios el ficherode mongo y se creará una carpeta mongodb-linux-x86_64-2.6.1 con un subdirectorio /bin
* Creamos un directorio $DESARROLLO/mongo/logs
* Creamos un directorio $DESARROLLO/mongo/data
* Creamos un fichero que se llama mongo.cfg. El contenido del fichero es:

    logpath=$DESARROLLO/mongo/logs/log
    pidfilepath=$DESARROLLO/mongo/mongo.pid
    dbpath=$DESARROLLO/mongo/data

* Arrancamos la mongo con la siguiente linea

    $DESARROLLO/mongo/mongodb-linux-x86_64-2.6.1/bin/mongod --config $DESARROLLO/mongo/mongo.cfg

* Aconsejo crear un script llamado startMongo.sh que te facilite la vida para no escribir todo este churro.



