package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.kuorum.rest.model.contact.filter.ConditionRDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.springframework.validation.ObjectError
import payment.contact.ContactService

@Secured("ROLE_POLITICIAN")
class ContactFiltersController {

    ContactService contactService;
    SpringSecurityService springSecurityService

    def updateFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first();
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }
        Long filterId = Long.parseLong(params.filterId)
        if (filterId <= 0){
            render ([status:"error", msg:"Estas intentando actualizar un filtro que no existe"] as JSON)
            return;
        }
        KuorumUser user = springSecurityService.currentUser
        FilterRDTO filterRDTO = convertCommandToFilter(filterCommand);
        ExtendedFilterRSDTO filterSaved = contactService.updateFilter(user,filterRDTO, filterId);


        render ([status:"ok",msg:"SUCCESS", data:[filter:filterSaved]] as JSON)
    }

    private FilterRDTO convertCommandToFilter(ContactFilterCommand command){
        FilterRDTO filterRDTO = new FilterRDTO()
        filterRDTO.name = command.filterName
        filterRDTO.operator = command.operator
        filterRDTO.setFilterConditions(command.filterConditions.findAll{it && it.value}.collect{
            ConditionRDTO conditionRDTO = new ConditionRDTO()
            conditionRDTO.operator = it.operator
            conditionRDTO.field = it.field
            conditionRDTO.value= it.value
            return conditionRDTO
//            new ConditionRDTO(it.properties)
        })
        filterRDTO
    }
}
