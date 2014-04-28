import kuorum.core.model.CommissionType
import kuorum.law.Law

fixture {
    prohibicionFraking(Law){
        hashtag = "#prohibicionFraking"
        urlPdf = "http://www.congreso.es/public_oficiales/L10/CONG/BOCG/A/BOCG-10-A-48-1.PDF"
        shortUrl = "http://ow.ly"
        shortName = "Prohibición del fracking"
        realName = "Proposición de Ley de prohibición de prospecciones y explotaciones de hidrocarburos no convencionales mediante fractura hidráulica, fracking"
        description = """La fractura hidráulica o "fracking" es un método de extracción de hidrocarburos no convencionales relativamente nuevo en Europa, aunque ya se ha utilizado de forma masiva en algunos estados de EEUU. Esta tecnología permite explotar yacimientos de hidrocarburos (principalmente de gas pizarra) imposibles de aprovechar con las técnicas convencionales. La UE no dispone por ahora de una regulación específica para esta práctica industrial. Aunque, tras un proceso de investigación iniciado en 2011 y una encuesta ciudadana cerrada recientemente, ha incluido en su programa de trabajo para 2013 el desarrollo de un "marco de evaluación medioambiental, climática y energética para permitir una extracción segura y protegida de hidrocarburos no convencionales".
En este contexto, esta proposición de ley aspira a prohibir la aplicación del "fracking" en territorio español en previsión de los efectos negativos que podría tener para el medioambiente y las personas.
"""
        introduction="""
Prohibición de prospecciones y explotaciones de hidrocarburos mediante fractura hidráulica
"""
        commissions = [CommissionType.NUTRITION_AND_ENVIRONMENT, CommissionType.INDUSTRY]
        region = spain
        published = Boolean.FALSE
        institution = parliament
    }
}