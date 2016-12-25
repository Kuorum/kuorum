package kuorum.web.commands.payment.contact

import grails.validation.Validateable

@Validateable
class BulkRemoveContactsCommand extends ContactFilterCommand {

    List<String> ids;

    static constraints = {
        ids validator: {obj, val ->
            // Check if filter is empty
            if (filterName && filterConditions.isEmpty()
                && ids.isEmpty()) {
                return "modal.bulkAction.error.paramsRequired";
            }
        }
    }
}
