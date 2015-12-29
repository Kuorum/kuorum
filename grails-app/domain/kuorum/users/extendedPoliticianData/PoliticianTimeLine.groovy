package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 11/10/15.
 */
class PoliticianTimeLine {
    Date date
    String title
    String text
    Boolean important

    static constraints = {
        date nullable:false
        title nullable:false
        text nullable: true
        important nullable:true
    }
}
