package kuorum

import java.text.DateFormat
import java.text.SimpleDateFormat

class DateHelperTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [humanDate: 'raw']

    static namespace = "kuorumDate"

    def humanDate={attrs ->
        Date date = attrs.date
        if (!date) {
            out << "---"
        } else {
            String itemprop = attrs.itemprop?"itemprop='${attrs.itemprop}'":''
            String cssClass = attrs.cssClass?:""
            TimeZone tz = TimeZone.getTimeZone("UTC")
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
            df.setTimeZone(tz)
            String dateAsISO = df.format(date)

            out << "<time class='timeago ${cssClass}' ${itemprop} datetime='${dateAsISO}'>${dateAsISO}</time>"
        }
    }

    def differenceDays={attrs->
        Date initDate = attrs.initDate
        Date endDate = attrs.endDate
        use(groovy.time.TimeCategory) {
            def duration = initDate - endDate
//            print "Days: ${duration.days}, Hours: ${duration.hours}, etc."
            out << duration.days
        }

    }

    def showShortedText={attrs->
        Integer numChars = attrs.numChars as Integer
        String text = attrs.text?.trim()
        if (text){
            text = text.encodeAsRemovingHtmlTags()
        }
        if (text){
            def words = text.split()

            Integer paintedChars = 0;
            Integer it = 0;
            while (paintedChars < numChars){
                out << words[it]
                out << " "
                if (it >= words.size()-1){
                    paintedChars = Integer.MAX_VALUE
                }else{
                    paintedChars += words[it].size()
                }
                it++
            }
            if (paintedChars != Integer.MAX_VALUE){
                out << "..."
            }
        }
    }
}
