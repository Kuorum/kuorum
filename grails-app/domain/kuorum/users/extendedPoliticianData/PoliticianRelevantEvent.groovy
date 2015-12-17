package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 11/10/15.
 */
class PoliticianRelevantEvent {
    String title;
    String url;

    static constraints = {
        title nullable:false
        url nullable: true, url: true
    }
}
