package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.ConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.ConditionRDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO

/**
 * Created by iduetxe on 22/08/16.
 */
@Validateable
class ContactFilterCommand {

    ContactFilterCommand(FilterRDTO filterRDTO){
        this.operator = filterRDTO.operator
        this.filterConditions = filterRDTO.filterConditions.collect {new ContactFilterOptionCommand(it)}
    }


    OperatorTypeRDTO operator;
    List<ContactFilterOptionCommand> filterConditions;

    static constraints = {
        operator nullable: false
        filterConditions nullable: false, maxSize: 20
    }
}

@Validateable
class ContactFilterOptionCommand{
    ContactFilterOptionCommand(){
        this.field = ConditionFieldTypeRDTO.NAME
        this.operator = ConditionOperatorTypeRDTO.EQUALS
    }
    ContactFilterOptionCommand(ConditionRDTO conditionRDTO){
        this.field = conditionRDTO.field
        this.operator = conditionRDTO.operator
        this.value = conditionRDTO.value
    }


    ConditionFieldTypeRDTO field;
    ConditionOperatorTypeRDTO operator;
    String value;

    static constraints = {
        field nullable: false
        operator nullable: false
        value nullable: false
    }
}