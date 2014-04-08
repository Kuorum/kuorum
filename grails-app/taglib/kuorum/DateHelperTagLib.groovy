package kuorum

import java.text.DateFormat
import java.text.SimpleDateFormat

class DateHelperTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [humanDate: 'raw']

    static namespace = "kuorumDate"

    def humanDate={attrs ->
        Date date = attrs.date
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String dateAsISO = df.format(date);

        out << "<time class='timeago' datetime='${dateAsISO}'>${dateAsISO}</time>"
    }
}
