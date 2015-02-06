import kuorum.KuorumFile
import kuorum.core.FileGroup
import kuorum.core.FileType
import kuorum.core.model.CommissionType
import kuorum.core.model.ProjectStatusType
import kuorum.project.Project

fixture {

    codigoPenalImage(KuorumFile){
        user = admin
        local = Boolean.FALSE
        temporal =Boolean.FALSE
//      storagePath
        alt='Parques Nacionales'
        fileName = "${grailsApplication.config.grails.serverURL}/static/images/img-post.jpg" //Para ver si funciona grailsApplication
//        url= "http://127.0.0.1:8080/kuorum/static/images/img-post.jpg"
        url= "${grailsApplication.config.grails.serverURL}/static/images/img-post.jpg"
        fileGroup = FileGroup.PROJECT_IMAGE
        fileType = FileType.IMAGE
        originalName = 'test'

    }
    //Creates the new objects related with project: urlYoutube and pdfFile
    urlYoutubeTest(KuorumFile){
        user = admin
        local = Boolean.FALSE
        temporal = Boolean.FALSE
        url= "http://kuorum.org"
        fileGroup = FileGroup.PROJECT_IMAGE
        originalName = 'test'
    }

    pdfFileTest(KuorumFile){
        user = admin
        local = Boolean.FALSE
        temporal =Boolean.FALSE
        fileName = "test.pdf"
        url= "http://kuorum.org"
        fileGroup = FileGroup.PDF
        originalName = 'test'
    }

    codigoPenal(Project){
        hashtag = "#codigoPenal"
        image = codigoPenalImage
        //urlPdf = "http://www.congreso.es/public_oficiales/L10/CONG/BOCG/A/BOCG-10-A-48-1.PDF"
        shortUrl = "http://ow.ly/3jLTrs"
        shortName = "Reforma del Código Penal"
        realName = "Proyecto de Ley Orgánica por la que se modifica la Ley Orgánica 10/1995, de 23 de noviembre, del Código Penal"
        description = """Se trata de la reforma más profunda del Código Penal desde 1995. Entre las novedades que introduce la ley destacan:
Condena de “prisión permanente revisable”: Este tipo de condena aplicaría en casos de genocidio y crímenes de lesa humanidad con homicidio, homicidio terrorista o contra jefes de Estado. También se contempla en casos de asesinato agravado: cuando la víctima sea menor de dieciséis años o se trate de una persona especialmente vulnerable; cuando sea subsiguiente a un delito contra la libertad sexual. Las condenas se cumplirían íntegramente durante entre 25 y 35 años. Pasado ese período se haría una revisión anual o bianual.
Medidas anticorrupción: Se revisan los tipos de prevaricación, malversación, cohecho o tráfico de influencias.
Otras medidas: Incitar al desorden público con mensajes o consignas a través de cualquier medio será castigado con una pena de multa o prisión de tres meses a un año. La edad mínima de consentimiento sexual se eleva de los 13 a los 16 años. Se persigue robo digital de derechos de autor con fines lucrativos.
"""
        introduction="""
Modificación de la Ley Orgánica del Código Penal
"""
        commissions = [CommissionType.JUSTICE]
        region = spain
        published = Boolean.TRUE
        institution = parliament
        politicalParty = grupoSocialista
        status = ProjectStatusType.OPEN
        availableStats = Boolean.FALSE
        relevance = 0
        publishDate = new Date()
        deadline = new Date() + 10
        pdfFile = pdfFileTest
        urlYoutube = urlYoutubeTest
        owner = admin
    }
}