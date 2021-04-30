package kuorum.web.commands.payment.contact

import grails.validation.Validateable

/**
 * Created by iduetxe on 1/09/16.
 */
@Validateable
class ContactExtraInfoCommand {
    ContactExtraInfoCommand(){}
    ContactExtraInfoCommand(Map<String,String> extraInfoMap){
        extraInfo = extraInfoMap?.entrySet()?.collect { new ContactExtraInfoValuesCommand(key:it.key, value: it.value)}?:[]
    }
    List<ContactExtraInfoValuesCommand> extraInfo
    static constraints = {
        extraInfo nullable: true
    }
}


@Validateable
class ContactExtraInfoValuesCommand {
    String key;
    String value;

    static constraints = {
        key nullable:false
        value nullable: false
    }
}

