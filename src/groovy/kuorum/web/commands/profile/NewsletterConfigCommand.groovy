package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 22/05/14.
 */
@Validateable
class NewsletterConfigCommand {

    String mainUrl;
    String address1;
    String address2;
    String address3;

    static constraints = {
        mainUrl     nullable:true, url:true
        address1    nullable:true
        address2    nullable:true
        address3    nullable:true
    }
}
