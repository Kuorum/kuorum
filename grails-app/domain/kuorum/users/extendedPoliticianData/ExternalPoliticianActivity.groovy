package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 11/10/15.
 */
class ExternalPoliticianActivity {
    Date date;
    String title;
    String link;
    String actionType;
    String outcomeType;
    static constraints = {
        date nullable:true
        title nullable:false
        actionType nullable: true
        outcomeType nullable:true
        link nullable:true
    }
}
