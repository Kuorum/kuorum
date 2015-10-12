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
    Region constituency
    Region region
    static embedded = ['constituency', 'region']
}
