package kuorum.numberCodecs
/**
 * Hide the phone number replacing first numbers with *****
 */
class HiddenPhoneCodec {

    static encode = {String target->
        if (!target){
            return ""
        }
        if (target.length()<4){
            return "****"
        }
        return target?.replaceFirst(/^.*(\d{4})$/,"******\$1");
    }

    static decode = {target->
        return target;
    }
}
