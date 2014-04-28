import kuorum.core.model.CommissionType
import kuorum.law.Law

fixture {
    codigoPenal(Law){
        hashtag = "#codigoPenal"
        urlPdf = "http://www.congreso.es/public_oficiales/L10/CONG/BOCG/A/BOCG-10-A-48-1.PDF"
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
        commissions = [CommissionType.JUSTICE, CommissionType.EMPLOY_AND_HEALTH_SERVICE]
        region = spain
        published = Boolean.TRUE
        institution = parliament
    }
}