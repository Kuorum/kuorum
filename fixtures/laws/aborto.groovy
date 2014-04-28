import kuorum.core.model.CommissionType
import kuorum.law.Law

fixture {
    aborto(Law){
        hashtag = "#leyAborto"
        urlPdf = "http://www.congreso.es/public_oficiales/L10/CONG/BOCG/A/BOCG-10-A-48-1.PDF"
        shortUrl = "http://ow.ly"
        shortName = "Ley del aborto"
        realName = "Ley de Protección de la Vida Concebido y de los Derechos de la Mujer Embarazada"
        description = """Esta nueva ley sustituirá, si finalmente es aprobada, a la actual Ley Orgánica 2/2010 de salud sexual y reproductiva y de la interrupción voluntaria del embarazo aprobada por el gobierno socialista en 2010 y que tiene varios recursos pendientes en el Tribunal Constitucional presentados por el Partido Popular.
La ley actual contempla el aborto como un derecho exclusivo de la mujer durante las primeras 14 semanas de gestación. A partir de ese momento la interrupción del embarazo se permite en 2 supuestos. Que exista un “grave riesgo para la vida o la salud de la madre o el feto” (alegable durante las primeras 22 semanas de gestación) o que se acredite una "enfermedad extremadamente grave o incurable" del feto o "anomalías incompatibles con la vida" (sin límite de tiempo).
La nueva ley retira la llamada “ley de plazos” y estipula que el aborto es un delito salvo en 2 supuestos. Que el embarazo sea fruto de una violación (alegable durante las primeras 12 semanas de gestación) o que genere un “grave peligro para la vida o la salud física o psíquica de la embarazada” (alegable en las primeras 22 semanas -salvo que en dicho plazo no hubiera podido ser detectado, en cuyo caso, también podrá practicarse después de ese plazo-).
En el segundo supuesto, el riesgo para la salud física o psíquica de la madre deberá "acreditarse de forma suficiente con dos informes motivados emitidos por dos médicos distintos del que practica el aborto" pero, a diferencia de la norma vigente, los médicos que realicen la evaluación no podrán trabajar en el mismo centro donde se vaya a practicar la intervención.
El ministro de justicia, Alberto Ruiz Gallardón destaca que la nueva ley despenalizará la conducta de la mujer al entender que se trata siempre de una víctima, siendo el médico que realice un aborto (fuera de los supuestos que permite la ley) sancionado según la legislación vigente. Asimismo, para que las menores de edad puedan abortar, dentro de los supuestos despenalizados, será obligatoria la participación de los padres o tutores."""
        introduction="""
Ley Orgánica de protección de la vida del concebido y derechos de la mujer embarazada
"""
        commissions = [CommissionType.JUSTICE, CommissionType.EMPLOY_AND_HEALTH_SERVICE]
        region = spain
        published = Boolean.TRUE
        institution = parliament
    }
}