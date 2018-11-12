package kuorum.users

class SocialLinks{
    String facebook
    String twitter
    String linkedIn
    String youtube
    String blog
    String instagram
    String officialWebSite
    String institutionalWebSite
    static constraints = {
        facebook      nullable: true, blank: true
        twitter       nullable: true, blank: true
        blog          nullable: true, blank: true
        linkedIn      nullable: true, blank: true
        youtube       nullable: true, blank: true
        instagram     nullable: true, blank: true
        officialWebSite       nullable: true, blank: true
        institutionalWebSite       nullable: true, blank: true
    }

}