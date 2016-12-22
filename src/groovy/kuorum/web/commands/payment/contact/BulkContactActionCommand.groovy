package kuorum.web.commands.payment.contact

class BulkContactActionCommand {

    List<Long> ids;
    Boolean checkedAll;

    static constraints = {
        ids nullable: false
        checkedAll nullable: false
    }

}
