package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.register.KuorumUserSession
import kuorum.web.commands.payment.contact.ContactFilterCommand
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionRDTO
import org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO
import org.springframework.validation.ObjectError
import payment.contact.ContactService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ContactFiltersController {

    ContactService contactService
    SpringSecurityService springSecurityService

    def newFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first()
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }
        String newFilterName = params.newFilterName
        if (!newFilterName){
            render ([status:"error", msg:g.message(code:"tools.contact.filter.form.saveAs.noName")] as JSON)
            return
        }
        KuorumUserSession user = springSecurityService.principal
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        filterRDTO.name = newFilterName
        ExtendedFilterRSDTO filterSaved = contactService.createFilter(user,filterRDTO)

        def filterRendered = g.render( template:"/contacts/filter/filterFieldSet",model:[filter:filterSaved])

        render ([
                status:"ok",
                msg:g.message(code:'tools.contact.filter.form.success', args: [filterSaved.name]),
                data:[filter:filterSaved, filterRendered:filterRendered]
        ] as JSON)
    }

    def updateFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first()
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }
        Long filterId = Long.parseLong(params.filterId)
        if (filterId <= 0){
            render ([status:"error", msg:g.message(code:'tools.contact.filter.form.notExits')] as JSON)
            return
        }
        KuorumUserSession user = springSecurityService.principal
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        ExtendedFilterRSDTO filterSaved = contactService.updateFilter(user,filterRDTO, filterId)


        render ([
                status:"ok",
                msg:g.message(code:'tools.contact.filter.form.success', args: [filterSaved.name]),
                data:[filter:filterSaved]
        ] as JSON)
    }

    def refreshFilter(ContactFilterCommand filterCommand){
        if (filterCommand.hasErrors()){
            ObjectError error = filterCommand.errors.allErrors.first()
            render ([status:"error", msg:error.defaultMessage] as JSON)
            return
        }

        KuorumUserSession user = springSecurityService.principal
        FilterRDTO filterRDTO = filterCommand.buildFilter()
        ContactPageRSDTO usersPage = contactService.getUsers(user, filterRDTO)

        ExtendedFilterRSDTO filterMock = new ExtendedFilterRSDTO(amountOfContacts: usersPage.total)
        render ([
                status:"ok",
                msg:g.message(code:'tools.contact.filter.form.refreshed'),
                data:[filter:filterMock]
        ] as JSON)
    }


    def getFilterData(Long filterId){
        if (filterId == null){
            render "Null filter ID";
            return;
        }
        KuorumUserSession user = springSecurityService.principal
        ExtendedFilterRSDTO filterRDTO
        if (filterId ==-2){
            filterRDTO = new ExtendedFilterRSDTO()
            filterRDTO.filterConditions = [ConditionRDTO.factory(ConditionFieldTypeRDTO.NAME, TextConditionOperatorTypeRDTO.STARTS_WITH.toString(), "")]
            filterRDTO.operator = OperatorTypeRDTO.AND
            filterRDTO.id = -2
        }else{
            filterRDTO = contactService.getFilter(user, filterId)
        }
        if (params.render && params.render=='JSON'){
            render filterRDTO as JSON
        }else{
            render template: '/contacts/filter/filterFieldSet', model:[filter:filterRDTO]
        }
    }

    def deleteFilter(Long filterId){
        KuorumUserSession user = springSecurityService.principal
        contactService.removeFilter(user, filterId)
        render "removed"
    }
}
