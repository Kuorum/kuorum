package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 11/10/15.
 */
class PoliticianExtraInfo {
    Long ipdbId
    String completeName
    Date birthDate
    String birthPlace
    String university
    String school
    String webSite

    static constraints = {
        birthDate nullable:true
        birthPlace nullable: true
        university nullable: true
        school nullable: true
        webSite nullable: true
    }
}
