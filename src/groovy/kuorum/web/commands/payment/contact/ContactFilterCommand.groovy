package kuorum.web.commands.payment.contact

import grails.validation.Validateable
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.*

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
        this.operatorExists = ExistsConditionOperatorTypeRDTO.EMPTY
        this.operatorContactType = ContactTypeConditionOperatorTypeRDTO.FOLLOWER
        this.operatorAssistantEvent = EventAssistantConditionOperatorTypeRDTO.BOOKED_TICKET
        this.operatorParticipatoryBudget = ParticipatoryBudgetConditionOperatorTypeRDTO.CREATED_PROPOSAL
        this.operatorContest = ContestConditionOperatorTypeRDTO.CREATED_CONTEST_APPLICATION
        this.operatorSurvey = SurveyConditionOperatorTypeRDTO.VOTED_SURVEY
    }
    ContactFilterOptionCommand(ConditionRDTO conditionRDTO){
        this()
        this.field = conditionRDTO.field
        this.operatorAssistantEvent = EventAssistantConditionOperatorTypeRDTO.BOOKED_TICKET;
        if (conditionRDTO instanceof ConditionTextRDTO || conditionRDTO instanceof ConditionLanguageRDTO) {
            this.operatorText = conditionRDTO.operator
        }else if (conditionRDTO instanceof ConditionBooleanRDTO){
            this.operatorBoolean = conditionRDTO.operator
        }else if (conditionRDTO instanceof ConditionExistsRDTO){
            this.operatorExists = conditionRDTO.operator
        }else if (conditionRDTO instanceof ConditionContactTypeRDTO){
            this.operatorContactType = conditionRDTO.operator
        }else if (conditionRDTO instanceof ConditionEventAssistantRDTO){
            this.operatorAssistantEvent = conditionRDTO.operator
        }else if (conditionRDTO instanceof ConditionParticipatoryBudgetRDTO) {
            this.operatorParticipatoryBudget = conditionRDTO.operator
        } else if (conditionRDTO instanceof ConditionContestRDTO) {
            this.operatorContest = conditionRDTO.operator
        } else if (conditionRDTO instanceof ConditionSurveyRDTO) {
            this.operatorSurvey = conditionRDTO.operator
        } else {
            this.operatorNumber = conditionRDTO.operator
        }
        this.value = conditionRDTO.value.toString()
    }


    ConditionFieldTypeRDTO field;
    TextConditionOperatorTypeRDTO operatorText;
    NumberConditionOperatorTypeRDTO operatorNumber;
    BooleanConditionOperatorTypeRDTO operatorBoolean;
    ExistsConditionOperatorTypeRDTO operatorExists;
    ContactTypeConditionOperatorTypeRDTO operatorContactType;
    EventAssistantConditionOperatorTypeRDTO operatorAssistantEvent;
    ParticipatoryBudgetConditionOperatorTypeRDTO operatorParticipatoryBudget;
    ContestConditionOperatorTypeRDTO operatorContest;
    SurveyConditionOperatorTypeRDTO operatorSurvey;
    String value;

    public String getOperator() {
        if (ConditionFieldTypeRDTO.STATUS.equals(field)) {
            return operatorNumber.toString()
        } else if (ConditionFieldTypeRDTO.BLACK_LIST.equals(field)) {
            return operatorBoolean.toString();
        } else if (ConditionFieldTypeRDTO.SUBSCRIBED.equals(field)) {
            return operatorBoolean.toString();
        } else if (ConditionFieldTypeRDTO.CONTACT_TYPE.equals(field)) {
            return operatorContactType.toString();
        }else if(ConditionFieldTypeRDTO.PERSONAL_CODE.equals(field)) {
            return operatorExists.toString();
        }else if(ConditionFieldTypeRDTO.EVENT.equals(field)){
            return operatorAssistantEvent.toString();
        }else if (ConditionFieldTypeRDTO.PARTICIPATORY_BUDGET.equals(field)) {
            return operatorParticipatoryBudget.toString();
        } else if (ConditionFieldTypeRDTO.CONTEST.equals(field)) {
            return operatorContest.toString();
        } else if (ConditionFieldTypeRDTO.SURVEY.equals(field)) {
            return operatorSurvey.toString();
        } else {
            return operatorText.toString()
        }
    }

    static constraints = {
        field nullable: false
        operatorText nullable: false
        operatorNumber nullable: false
        operatorAssistantEvent nullable: false
        value nullable: false
    }
}