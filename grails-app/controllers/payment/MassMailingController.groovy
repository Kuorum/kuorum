package payment

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.ConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.filter.ConditionRDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.OperatorTypeRDTO
import payment.contact.ContactService

@Secured("ROLE_POLITICIAN")
class MassMailingController {

    SpringSecurityService springSecurityService

    ContactService contactService;

    def index(){

    }

    def createMassMailing(){
        KuorumUser loggedUser = springSecurityService.currentUser
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(loggedUser)
        MassMailingCommand command = new MassMailingCommand()
        if (params.testFilter){
//            ExtendedFilterRSDTO testFilter = createTestFilter(filters, loggedUser)
            ExtendedFilterRSDTO testFilter = fakeTestFilter(filters, loggedUser)
            command.filterId = testFilter.id
        }
        [filters:filters, command:command]
    }

    private ExtendedFilterRSDTO fakeTestFilter(List<ExtendedFilterRSDTO> filters , KuorumUser user){
        FilterRDTO filter = createTestFilter(user)
        ExtendedFilterRSDTO filterTest = new ExtendedFilterRSDTO(filter.class.declaredFields.findAll { !it.synthetic }.collectEntries {
            [ (it.name):filter."$it.name" ]
        })
        filterTest.id = -1L;
        filterTest.amountOfContacts=1;
        filters.add(filterTest)
        filterTest
    }

    private ExtendedFilterRSDTO createTestFilter(List<ExtendedFilterRSDTO> filters , KuorumUser user){
        ExtendedFilterRSDTO filterTest = filters.find{it.name=~ "\\(TEST\\)"}
        if (!filterTest){
            FilterRDTO filter = new FilterRDTO();
            filter.setOperator(OperatorTypeRDTO.AND)
            filter.setName("MysSelf (TEST)")
            filter.filterConditions = []
            filter.filterConditions.add(new ConditionRDTO([
                    field:ConditionFieldTypeRDTO.EMAIL,
                    operator: ConditionOperatorTypeRDTO.EQUALS,
                    value:user.email
            ]))
            filterTest = contactService.createFilter(user, filter)
            filters.add(filterTest)
        }
        if (filterTest.amountOfContacts==0){
            List<ContactRSDTO> contactRSDTOs = []
            contactRSDTOs.add(new ContactRSDTO(name:user.name, email: user.email))
            contactService.addBulkContacts(user, contactRSDTOs)
            filterTest.amountOfContacts++ //Chapu por asincronia y por no volver a llamar
        }
        return filterTest
    }

    private FilterRDTO createTestFilter(KuorumUser user){
        FilterRDTO filter = new FilterRDTO();
        filter.setOperator(OperatorTypeRDTO.AND)
        filter.setName("MysSelf -TEST-")
        filter.filterConditions = []
        filter.filterConditions.add(new ConditionRDTO([
                field:ConditionFieldTypeRDTO.EMAIL,
                operator: ConditionOperatorTypeRDTO.EQUALS,
                value:user.email
        ]))
        return filter
    }

    def saveMassMailing(){
        render "OK"
    }
}
