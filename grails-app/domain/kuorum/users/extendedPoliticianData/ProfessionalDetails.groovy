package kuorum.users.extendedPoliticianData
/**
 * Created by iduetxe on 11/10/15.
 */
class ProfessionalDetails {
    String position;
    String institution;
    String politicalParty

    static constraints = {
        position nullable:true
        institution nullable:true
        politicalParty nullable:true
    }
}
