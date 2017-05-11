package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.BooleanConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionBooleanRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionTextRDTO
import org.kuorum.rest.model.contact.filter.condition.ContactTypeConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.NumberConditionOperatorTypeRDTO
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
                        .findAll{it}
                        .collect{ConditionRDTO.factory(it.field, it.operator, it.value)}
                        .findAll{it && it.value!=null}
        )
        filterRDTO
    }
}

@Validateable
class ContactFilterOptionCommand{
    ContactFilterOptionCommand(){
        this.field = ConditionFieldTypeRDTO.NAME
        this.operatorText = TextConditionOperatorTypeRDTO.EQUALS
        this.operatorNumber = NumberConditionOperatorTypeRDTO.EQUALS
        this.operatorBoolean = BooleanConditionOperatorTypeRDTO.TRUE
        this.operatorContactType = ContactTypeConditionOperatorTypeRDTO.FOLLOWER
    }
    ContactFilterOptionCommand(ConditionRDTO conditionRDTO){
        this.field = conditionRDTO.field

        //DEFAULT
        this.operatorText = TextConditionOperatorTypeRDTO.EQUALS;
        this.operatorNumber = NumberConditionOperatorTypeRDTO.EQUALS;
        this.operatorBoolean = BooleanConditionOperatorTypeRDTO.TRUE;
        this.operatorContactType = ContactTypeConditionOperatorTypeRDTO.FOLLOWER;
        if (conditionRDTO instanceof ConditionTextRDTO) {
            this.operatorText = conditionRDTO.operator
        }else if (conditionRDTO instanceof ConditionBooleanRDTO){
            this.operatorBoolean = conditionRDTO.operator
        }else if (conditionRDTO instanceof ContactTypeConditionOperatorTypeRDTO){
            this.operatorContactType = conditionRDTO.operator
        }else{
            this.operatorNumber = conditionRDTO.operator
        }
        this.value = conditionRDTO.value.toString()
    }


    ConditionFieldTypeRDTO field;
    TextConditionOperatorTypeRDTO operatorText;
    NumberConditionOperatorTypeRDTO operatorNumber;
    BooleanConditionOperatorTypeRDTO operatorBoolean;
    ContactTypeConditionOperatorTypeRDTO operatorContactType;
    String value;

    public String getOperator(){
        if (ConditionFieldTypeRDTO.STATUS.equals(field)) {
            return operatorNumber.toString()
        }else if(ConditionFieldTypeRDTO.BLACK_LIST.equals(field)){
            return operatorBoolean.toString();
        }else if(ConditionFieldTypeRDTO.CONTACT_TYPE.equals(field)){
            return operatorContactType.toString();
        }else{
            return operatorText.toString()
        }
    }

    static constraints = {
        field nullable: false
        operatorText nullable: false
        operatorNumber nullable: false
        value nullable: false
    }
}