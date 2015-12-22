package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 22/12/15.
 */
class CareerDetails {

    String profession;
    String university
    String studies
    String school
    String cvLink
    String declarationLink

    static constraints = {
        university nullable: true
        school nullable: true
        studies nullable: true
        profession nullable:true
        cvLink nullable:true
        declarationLink nullable:true
    }
}
