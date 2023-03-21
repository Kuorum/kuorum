package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 4/01/16.
 */
@Validateable
class DomainUserPhoneValidationCommand {

    String phoneNumberPrefix
    Long phoneNumber

    static List<String> INVALID_PHONES = ["00291", "+291", "00292", "+292", "00299", "+299", "00298", "+298"];
    static String SPANISH_CODE = "0034"

    static constraints = {
        phoneNumberPrefix nullable: false, validator: { val, obj ->
            if (obj.phoneNumber) {
                // CHECK only if the user has introduced the phone number. If not, the system supposes that the phone will be recovered from Contact DDBB
                if (INVALID_PHONES.contains(val)) {
                    return "CountryNotAllowed"
                }
                if (SPANISH_CODE.equals(val)) {
                    if (obj.phoneNumber.toString().length() != 9 && !obj.phoneNumber.toString().startsWith("6")) {
                        return "NotSpanishPhone"
                    }
                }
            }
        }
        phoneNumber nullable: true
    }
}
