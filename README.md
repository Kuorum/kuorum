# web
Kuorum es una red social de democracia participativa online que amplifica tu voz en las instituciones.



## Detalles para montar el entorno
* Recursos
* IntelliJ-IDEA (IDE aconsejado)
* Mongodb 2.6.1
* Grails 2.3.7
* Java 1.7

### Crear una carpeta para el desarrollo
Se va a dejar todo en una misma carpeta bien recojido y ordenado. :) A lo largo del proceso de instalaci�n haremos
referencia a este carpeta con el nombre DESARROLLO

    var DESARROLLO=/home/user/kuorum

### Instalamos Java 1.7 (JDK)

### Grails
Descargar grails 2.3.7 en la carpeta $DESARROLLO/grails

    http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-2.3.7.zip

y descomprimir

### Descargamos mongo en $(DESARROLLO)/mongo
* Linux (64 bits) https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.6.1.tgz
* Descomprimimios el ficherode mongo y se crear� una carpeta mongodb-linux-x86_64-2.6.1 con un subdirectorio /bin
* Creamos un directorio $DESARROLLO/mongo/logs
* Creamos un directorio $DESARROLLO/mongo/data
* Creamos un fichero que se llama mongo.cfg. El contenido del fichero es:

    logpath=$DESARROLLO/mongo/logs/log
    pidfilepath=$DESARROLLO/mongo/mongo.pid
    dbpath=$DESARROLLO/mongo/data

* Arrancamos la mongo con la siguiente linea

    $DESARROLLO/mongo/mongodb-linux-x86_64-2.6.1/bin/mongod --config $DESARROLLO/mongo/mongo.cfg

* TIP: crear un script llamado startMongo.sh que te facilite la vida para no escribir todo este churro.
### SOLR
El solr es la BBDD documental basada en Lucene para realizar las b�squedas.
La configuraci�n del solar esta a parte del proyecto, y hay que descargarla a parte.
* Creamos la carpeta $DESAROLLO/solr
* Nos vamos a esa carpeta y clonamos el proyecto de Sole de Kuorm

    git clone https://github.com/Kuorum/solr.git

### IDEA
Este es el IDE aconsejado para trabajar con GRAILS, pero se puede usar cualquier otro (ie: Eclipse)

Creamos una carpeta que se llame $DESARROLLO/kuorum

Vamos a crear el proyecto desde el github.
* VCS -> Checkout form version contro -> Github

    https://github.com/Kuorum/kuorum.git

* Seguir los pasos de creaci�n de un proyecto del IDEA.
** Cuando pide seleccionar grails seleccionamos el que nos acabamos de bajar
** Yo seleccionaria la descarga del c�digo en la carpeta creada anteriormente $DESARROLLO/kuorum
### Configuraci�n entorno
* En la carpeta $DESARROLLO/Kuorum/kuorum/src/java/ hay un fichero que se llama example_conf.properties.
* Copiar y renombrar el fichero a la misma carpeta pero llamandolo development_config.properties