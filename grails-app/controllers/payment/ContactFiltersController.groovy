package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ConditionRDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.springframework.validation.ObjectError
import payment.contact.ContactService

@Secured("ROLE_POLITICIAN")
class ContactFiltersController {

    ContactService contactService;
    SpringSecurityService springSecurityService

    def newFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first();
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }
        String newFilterName = params.newFilterName
        if (!newFilterName){
            render ([status:"error", msg:g.message(code:"tools.contact.filter.form.saveAs.noName")] as JSON)
            return;
        }
        KuorumUser user = springSecurityService.currentUser
        FilterRDTO filterRDTO = convertCommandToFilter(filterCommand);
        filterRDTO.name = newFilterName
        ExtendedFilterRSDTO filterSaved = contactService.createFilter(user,filterRDTO);


        render ([
                status:"ok",
                msg:g.message(code:'tools.contact.filter.form.success', args: [filterSaved.name]),
                data:[filter:filterSaved]
        ] as JSON)
    }

    def updateFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first();
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }
        Long filterId = Long.parseLong(params.filterId)
        if (filterId <= 0){
            render ([status:"error", msg:g.message(code:'tools.contact.filter.form.notExits')] as JSON)
            return;
        }
        KuorumUser user = springSecurityService.currentUser
        FilterRDTO filterRDTO = convertCommandToFilter(filterCommand);
        ExtendedFilterRSDTO filterSaved = contactService.updateFilter(user,filterRDTO, filterId);


        render ([
                status:"ok",
                msg:g.message(code:'tools.contact.filter.form.success', args: [filterSaved.name]),
                data:[filter:filterSaved]
        ] as JSON)
    }

    def refreshFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first();
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }

        KuorumUser user = springSecurityService.currentUser
        FilterRDTO filterRDTO = convertCommandToFilter(filterCommand);
        ContactPageRSDTO usersPage = contactService.getUsers(user, filterRDTO)

        ExtendedFilterRSDTO filterSaved = new ExtendedFilterRSDTO(amountOfContacts: usersPage.total)
        render ([
                status:"ok",
                msg:g.message(code:'tools.contact.filter.form.refreshed'),
                data:[filter:filterSaved]
        ] as JSON)
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
