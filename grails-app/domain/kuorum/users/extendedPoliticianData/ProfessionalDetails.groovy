package kuorum.users.extendedPoliticianData

import kuorum.Region

/**
 * Created by iduetxe on 11/10/15.
 */
class ProfessionalDetails {
    String position;
    String institution;
    String politicalParty
    String sourceWebsite
    Region constituency
    Region region //Place where the politician has "power" :P
    static embedded = ['constituency', 'region']

    static constraints = {
        position nullable:true
        institution nullable:true
        constituency nullable:true
        region nullable:true
        sourceWebsite nullable: true
    }
}
