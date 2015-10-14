package kuorum.users

class SocialLinks{
    String facebook
    String twitter
    String googlePlus
    String linkedIn
    String youtube
    String blog
    String instagram
    static constraints = {
        facebook nullable: true, blank: true
        twitter  nullable: true, blank: true
        googlePlus   nullable: true, blank: true
        blog     nullable: true, blank: true
        linkedIn     nullable: true, blank: true
        youtube     nullable: true, blank: true
        instagram     nullable: true, blank: true
    }

}