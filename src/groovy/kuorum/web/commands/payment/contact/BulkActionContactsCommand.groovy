package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.grails.databinding.BindUsing
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.NumberConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO

@Validateable
class BulkActionContactsCommand extends ContactFilterCommand {

    @BindUsing({obj, source ->
        source.map.listIds?.split(",")?.collect{it.trim()}?.findAll{it}?:[]
    })
    List<Long> listIds

    Boolean checkedAll = false

    static constraints = {
        listIds validator: {val, obj ->
            if (obj.checkedAll) {
                // Use filter
                return true
            } else if (!obj.checkedAll && obj.listIds != null && !obj.listIds.isEmpty()) {
                // Use listIds
                // Covert list of ids to filter
                obj.operator = OperatorTypeRDTO.OR
                obj.filterConditions = new ArrayList<>()
                for (Long idAux : obj.listIds) {
                    ContactFilterOptionCommand aux = new ContactFilterOptionCommand()
                    aux.field = ConditionFieldTypeRDTO.ID
                    aux.operatorText = TextConditionOperatorTypeRDTO.EQUALS
                    aux.operatorNumber = NumberConditionOperatorTypeRDTO.EQUALS
                    aux.value = idAux
                    obj.filterConditions.add(aux)
                }

                return true
            }

            return false
        }
    }

}
