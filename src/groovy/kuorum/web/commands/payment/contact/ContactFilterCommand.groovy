package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionRDTO
import org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO

/**
 * Created by iduetxe on 22/08/16.
 */
@Validateable
class ContactFilterCommand {

    ContactFilterCommand(){
        this.operator = OperatorTypeRDTO.AND
        this.filterConditions =[]
    }
    ContactFilterCommand(FilterRDTO filterRDTO){
        this.operator = filterRDTO.operator
        this.filterConditions = filterRDTO.filterConditions.collect {new ContactFilterOptionCommand(it)}
        this.filterName = filterRDTO.name
    }

    String filterName;
    OperatorTypeRDTO operator;
    List<ContactFilterOptionCommand> filterConditions;

    static constraints = {
        filterName nullable: true
        operator nullable: false
        filterConditions nullable: false, maxSize: 20
    }

    public FilterRDTO buildFilter(){
        FilterRDTO filterRDTO = new FilterRDTO()
        filterRDTO.name = this.filterName
        filterRDTO.operator = this.operator
        filterRDTO.setFilterConditions(
                this.filterConditions
                        .findAll{it && it.value}
                        .collect{ConditionRDTO.factory(it.field, it.operator.toString(), it.value)
        })
        filterRDTO
    }
}

@Validateable
class ContactFilterOptionCommand{
    ContactFilterOptionCommand(){
        this.field = ConditionFieldTypeRDTO.NAME
        this.operator = TextConditionOperatorTypeRDTO.EQUALS
    }
    ContactFilterOptionCommand(ConditionRDTO conditionRDTO){
        this.field = conditionRDTO.field
        this.operator = conditionRDTO.operator.toString()
        this.value = conditionRDTO.value.toString()
    }


    ConditionFieldTypeRDTO field;
    TextConditionOperatorTypeRDTO operator;
    String value;

    static constraints = {
        field nullable: false
        operator nullable: false
        value nullable: false
    }
}