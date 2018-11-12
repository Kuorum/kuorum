package kuorum.web.commands.profile

import grails.validation.Validateable
import kuorum.users.KuorumUser
import org.apache.commons.validator.routines.UrlValidator

/**
 * Created by iduetxe on 22/05/14.
 */
@Validateable
class SocialNetworkCommand {

    public SocialNetworkCommand(){}
    public SocialNetworkCommand(KuorumUser user){
        this.properties.each {
            if (it.key!= "class" && user.socialLinks.hasProperty(it.key))
                this."$it.key" = user.socialLinks."${it.key}"
        }
    }
    String facebook
    String twitter
    String linkedIn
    String youtube
    String blog
    String instagram
    String officialWebSite
    //String institutionalWebSite

    static constraints = {
        facebook            nullable:true, url:true
        twitter             nullable:true, validator: { String twitter, command ->
            if (twitter.startsWith("http")) {
                // URL
                UrlValidator urlValidator = new UrlValidator("http","https");
                if (!urlValidator.isValid(twitter)){
                    return 'kuorum.web.commands.profile.SocialNetworkCommand.twitter.url.invalid'
                }
            }
        }
        linkedIn            nullable:true, url:true
        youtube             nullable:true, url:true
        blog                nullable:true, url:true
        instagram           nullable:true, url:true
        officialWebSite     nullable:true, url:true
        //institutionalWebSite nullable:true, url:true

    }

    String getTwitter(){
        if (twitter){
            return twitter.decodeTwitter();
        }else{
            return "";
        }
    }
}
