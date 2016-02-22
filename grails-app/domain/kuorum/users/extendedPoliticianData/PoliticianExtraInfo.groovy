package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 11/10/15.
 */
class PoliticianExtraInfo {
    String externalId
    String completeName
    Date birthDate
    String birthPlace
    String family

    static constraints = {
        externalId nullable: true
        completeName nullable:true
        birthDate nullable:true
        birthPlace nullable: true
        family nullable: true
    }
}
