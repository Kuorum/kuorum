import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.CommissionType
import kuorum.law.Law

fixture {

//    def grailsApplication

    parquesNacionalesImage(KuorumFile){
        user = admin
        local = Boolean.FALSE
        temporal =Boolean.FALSE
//      storagePath
        alt='Parques Nacionales'
        fileName = "${grailsApplication.config.grails.serverURL}/static/images/img-post.jpg" //Para ver si funciona grailsApplication
//        url= "http://127.0.0.1:8080/kuorum/static/images/img-post.jpg"
        url= "${grailsApplication.config.grails.serverURL}/static/images/img-post.jpg"
        fileGroup = FileGroup.LAW_IMAGE
        fileType = FileType.IMAGE

    }

    parquesNacionales(Law){
        hashtag = "#parquesNacionales"
        image = parquesNacionalesImage
        urlPdf = "http://www.congreso.es/public_oficiales/L10/CONG/BOCG/A/BOCG-10-A-48-1.PDF"
        shortUrl = "http://ow.ly/3jLTrs"
        shortName = "Parques Nacionales"
        realName = "Proyecto de Ley de Parques Nacionales"
        description = """España cuenta con 15 parques nacionales que ocupan una superficie total de 381.716 hectáreas (un 0,76% del territorio español). Este Proyecto de Ley tiene por objetivo, según el Ministro de Agricultura, Alimentación y Medio Ambiente, Miguel Arias Cañete, garantizar la adecuada conservación de estos espacios y reforzar la coordinación y colaboración entre el Estado y las Comunidades Autónomas (CCAA) encargadas de su gestión.
La nueva ley recoge la posibilidad de intervención estatal en los Parques Nacionales en caso de catástrofes o situaciones extraordinarias que pongan en peligro su riqueza natural.
Igualmente se prevé la creación de la marca Parques Nacionales de España como «identificador común de calidad para las producciones de estos espacios»
El texto también amplía la superficie mínima requerida para los territorios sobre los que se declaren nuevos Parques Nacionales que se incrementa desde las 15.000 hectáreas actuales hasta las 20.000 hectáreas. Sin embargo, se incorpora la posibilidad de ampliar Parques Nacionales ya declarados sobre áreas marinas exteriores colindantes, algo que no recoge la normativa actual.
Por otra parte, se declaran como incompatibles en los Parques Nacionales la caza deportiva y comercial, así como la pesca deportiva y recreativa y la tala con fines comerciales. Sin embargo, el proyecto contempla que, por motivos de gestión y de acuerdo al mejor conocimiento científico, la administración del parque "podrá programar actividades de control de poblaciones y de restauración de hábitats". Esta es una de las cuestiones más criticadas por las organizaciones ambientales que consideran que bajo la figura de control de poblaciones se van a poder organizar monterías.
Asimismo se introducen excepciones que permitirán la navegación turística en Monfragüe, el vuelo sin motor en Guadarrama y la posibilidad de urbanizar y edificar dentro de los parques ya existentes. Sobre esta última, muy criticada por diferentes organizaciones, el secretario de estado, Federico Ramos, ha aclarado que “no se va a urbanizar en parques nacionales” y que dicha excepción se refiere a las necesidades de los núcleos poblacionales que ya existían en estas zonas antes de declararse Parques Naturales (2 pequeños pueblos en Picos de Europa, Cabrera y 25 habitantes de Villareal de San Carlos).
"""
        introduction="""
Nuevo marco jurídico para la gestión de los Parques Naturales
"""
        commissions = [CommissionType.ECONOMY, CommissionType.EMPLOY_AND_HEALTH_SERVICE]
        region = spain
        published = Boolean.TRUE
        institution = parliament
    }
}