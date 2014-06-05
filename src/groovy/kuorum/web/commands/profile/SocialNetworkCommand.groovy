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
    String blog

    static constraints = {
        facebook nullable:true, url:true
        twitter nullable:true, matches: '@[a-zA-Z0-9_]+'
        blog nullable:true, url:true
        googlePlus nullable:true, url:true
    }
}
