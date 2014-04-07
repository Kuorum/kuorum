package kuorum.web.commands.customRegister

import com.sun.org.apache.xpath.internal.operations.Bool
import grails.validation.Validateable
import kuorum.core.model.Gender
import kuorum.web.commands.profile.BirthdayCommad

/**
 * Created by iduetxe on 17/03/14.
 */
@Validateable
class Step1Command extends BirthdayCommad{
    Gender gender
    String postalCode

    static constraints = {
        importFrom BirthdayCommad
        gender nullable: false
        postalCode nullable: false, minSize: 5, maxSize: 5, matches:"[0-9]+"
    }
//    private boolean esBisiesto(Integer year){
//        year % 4 == 0 && (year % 100 != 0 || year %100==0 && year%400==0)
//    }
}
