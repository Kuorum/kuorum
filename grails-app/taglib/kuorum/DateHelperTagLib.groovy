package kuorum

import java.text.DateFormat
import java.text.SimpleDateFormat

class DateHelperTagLib {
    static defaultEncodeAs = 'html'
    static encodeAsForTags = [humanDate: 'raw', printTimeZoneName:'raw', printCountDown:'raw']

    static namespace = "kuorumDate"

    def humanDate={attrs ->
        Date date = attrs.date
        String idTag = attrs.id?"id='${attrs.id}'":""
        if (!date) {
            out << "---"
        } else {
            String itemprop = attrs.itemprop?"itemprop='${attrs.itemprop}'":''
            String cssClass = attrs.cssClass?:""
            TimeZone tz = TimeZone.getTimeZone("UTC")
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
            df.setTimeZone(tz)
            String dateAsISO = df.format(date)

            out << "<time class='timeago ${cssClass}' ${itemprop} datetime='${dateAsISO}' ${idTag}>${dateAsISO}</time>"
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
    def printTimeZoneName = {attrs ->
        sun.util.calendar.ZoneInfo zoneInfo = attrs.zoneInfo
        Date date = attrs.date
        Long offsetHours = zoneInfo.rawOffset / 1000 / 3600
        String symbol = offsetHours >=0?"+":"";
        String timeZoneName = g.formatDate(format:"z",date:date, timeZone:zoneInfo);
        out << "<span data-toggle='tooltip' data-placement='bottom' data-original-title='UTC ${symbol}${offsetHours} h'>${timeZoneName}</span>";
    }

    def printCountDown = {attrs ->
        Date date = attrs.date
        String idTag = attrs.id?"id='${attrs.id}'":""
        if (!date) {
            out << "---"
        } else {
            String itemprop = attrs.itemprop?"itemprop='${attrs.itemprop}'":''
            String cssClass = attrs.cssClass?:""
            TimeZone tz = TimeZone.getTimeZone("UTC")
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
            df.setTimeZone(tz)
            String dateAsISO = df.format(date)
            Long diff = date.getTime() - new Date().getTime()
            long diffSeconds = Math.floor(diff / 1000) % 60;
            long diffMinutes = Math.floor(diff / (60 * 1000)) % 60;
            long diffHours = Math.floor(diff / (60 * 60 * 1000)) % 24;
            long diffDays = Math.floor(diff / (24 * 60 * 60 * 1000));


            out << "<div class='count-down-timer ${cssClass}' datetime='${dateAsISO}' ${idTag}>${diffDays * 24+diffHours}:${diffMinutes}:${diffSeconds}</div>"
        }
    }
}
