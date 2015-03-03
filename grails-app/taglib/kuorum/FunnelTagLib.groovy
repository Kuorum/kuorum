package kuorum

class FunnelTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']

    static namespace = "funnel"

    def formatAsElegantPrice ={attrs ->
        Double price = attrs.value as Double
        def priceParts = price.toString().split("\\.");
        String decimalPart = ""
        if (price < 1000)
            decimalPart = ",<span class=\"decimals\">${priceParts[1]}</span>"
        out << """
            <span>
                ${priceParts[0]}${decimalPart}
                <span class="euro">${g.message(code:"funnel.successfulStories.offers.currency")}</span>
            </span>
            """
    }
}
