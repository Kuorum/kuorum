package kuorum.web.commands.profile

import grails.validation.Validateable

/**
 * Created by iduetxe on 22/05/14.
 */
@Validateable
class SocialNetworkCommand {
    String facebook
    String twitter
    String googlePlus
    String linkedIn
    String youtube
    String blog
    String instagram
    String officialWebSite
    String institutionalWebSite

    static constraints = {
        facebook            nullable:true, url:true
        twitter             nullable:true, url:true
        googlePlus          nullable:true, url:true
        linkedIn            nullable:true, url:true
        youtube             nullable:true, url:true
        blog                nullable:true, url:true
        instagram           nullable:true, url:true
        officialWebSite     nullable:true, url:true
        institutionalWebSite nullable:true, url:true

    }
}
