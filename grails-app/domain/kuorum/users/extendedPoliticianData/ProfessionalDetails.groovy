package kuorum.users.extendedPoliticianData

import kuorum.Region

/**
 * Created by iduetxe on 11/10/15.
 */
class ProfessionalDetails {
    String position;
    String institution;
    String profession;
    String politicalParty
    String sourceWebsite
    String cvLink
    String declarationLink
    Region constituency
    Region region
    static embedded = ['constituency', 'region']

    static constraints = {
        position nullable:true
        institution nullable:true
        profession nullable:true
        constituency nullable:true
        region nullable:true
        sourceWebsite nullable: true
    }
}
