package kuorum

import java.text.DateFormat
import java.text.SimpleDateFormat

class DateHelperTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [humanDate: 'raw']

    static namespace = "kuorumDate"

    def humanDate={attrs ->
        Date date = attrs.date
        String cssClass = attrs.cssClass?:""
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String dateAsISO = df.format(date);

        out << "<time class='timeago ${cssClass}' datetime='${dateAsISO}'>${dateAsISO}</time>"
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
}
