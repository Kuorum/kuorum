package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 11/10/15.
 */
class PoliticianExtraInfo {
    Long ipdbId
    String completeName
    Date birthDate
    String birthPlace
    String webSite
    String family

    static constraints = {
        birthDate nullable:true
        birthPlace nullable: true
        webSite nullable: true, url:true
        family nullable: true
    }
}
