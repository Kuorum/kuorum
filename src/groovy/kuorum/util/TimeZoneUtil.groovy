package kuorum.util

import java.text.SimpleDateFormat

/**
 * Created by iduetxe on 27/03/17.
 */
class TimeZoneUtil {

    private static Date convertToUserTimeZone(Date date, TimeZone userTimeZone){
        if (date){
            def dateFormat = 'yyyy/MM/dd HH:mm'
            String rawDate = date.format(dateFormat)
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setTimeZone(userTimeZone);
            return sdf.parse(rawDate)
        }else{
            return null;
        }
    }
}
