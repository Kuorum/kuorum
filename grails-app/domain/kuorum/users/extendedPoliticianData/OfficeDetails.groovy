package kuorum.users.extendedPoliticianData

/**
 * Created by iduetxe on 22/12/15.
 */
class OfficeDetails {
    String address;
    String telephone;
    String mobile;
    String fax;
    String assistants;

    static constraints = {
        address nullable: true
        telephone nullable: true
        mobile nullable: true
        fax nullable: true
        assistants nullable: true
    }
}
