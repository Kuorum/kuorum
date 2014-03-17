package kuorum.web.commands.customRegister

import com.sun.org.apache.xpath.internal.operations.Bool
import grails.validation.Validateable
import kuorum.core.model.Gender

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step1Command {
    Gender gender
    String postalCode
    Integer year
    Integer month
    Integer day
    static constraints = {
        gender nullable: false
        postalCode nullable: false, minSize: 5, maxSize: 5, matches:"[0-9]+"
        year min:1900
        month nullable: false, min:1, max: 12
        day nullable: false, min: 1, max: 31,validator: {val, obj ->
            try{
                Date date = Date.parse('yyyy/MM/dd', "$obj.year/$obj.month/$val")
                if (date[Calendar.YEAR]==obj.year && date[Calendar.MONTH] == obj.month -1 && date[Calendar.DAY_OF_MONTH]==val)
                    return Boolean.TRUE
                else
                    return "notCorrectBirthday"
            }catch (Exception e){
                return "notCorrectBirthday"
            }
        }
    }

//    private boolean esBisiesto(Integer year){
//        year % 4 == 0 && (year % 100 != 0 || year %100==0 && year%400==0)
//    }
}
