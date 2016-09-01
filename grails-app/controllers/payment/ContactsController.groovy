package payment

import grails.async.Promise
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.contact.ContactCommand
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.ContactRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.SearchContactRSDTO
import org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.sort.SortContactsRDTO
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import payment.contact.ContactService

@Secured("ROLE_POLITICIAN")
class ContactsController {

    private static final String CONTACT_CSV_UPLOADED_SESSION_KEY="CONTACT_CSV_UPLOADED_SESSION_KEY"

    ContactService contactService;
    SpringSecurityService springSecurityService

    def index(){
        KuorumUser user = springSecurityService.currentUser
        SearchContactRSDTO searchContactRSDTO  = new SearchContactRSDTO();
        searchContactRSDTO.sort = new SortContactsRDTO(field:ConditionFieldTypeRDTO.NAME, direction: SortContactsRDTO.Direction.ASC)
        ContactPageRSDTO contacts = contactService.getUsers(user,searchContactRSDTO)
        if (contacts.total <=0){
            redirect mapping:"politicianContactImport"
            return;
        }
        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)

        [
                contacts:contacts,
                filters:filters,
                totalContacts:contacts.total,
                command:new MassMailingCommand(),
                searchContacts:searchContactRSDTO
        ]
    }

    def searchContacts(ContactFilterCommand filterCommand){
        KuorumUser user = springSecurityService.currentUser
        SearchContactRSDTO searchContactRSDTO  = new SearchContactRSDTO();
        searchContactRSDTO.page=Long.parseLong(params.page?:"0")
        searchContactRSDTO.size=params.size?Long.parseLong(params.size):searchContactRSDTO.size
        searchContactRSDTO.sort = new SortContactsRDTO(
                field: params.sort?.field?ConditionFieldTypeRDTO.valueOf(params.sort.field):ConditionFieldTypeRDTO.NAME,
                direction: params.sort?.direction?SortContactsRDTO.Direction.valueOf(params.sort.direction):SortContactsRDTO.Direction.ASC
        )
        Long filterId = Long.parseLong(params.filterId?:'0')
        FilterRDTO filterRDTO = filterCommand.buildFilter()

        if (filterId <0 || filterRDTO.filterConditions ) {
            searchContactRSDTO.filter = filterRDTO
        }else if (filterId == 0 ) {
            // NO FILTER -> ALL CONTACTS
        }else{
            searchContactRSDTO.filterId = filterId
        }
        searchContactRSDTO.quickSearch = params.quickSearchByName
        ContactPageRSDTO contacts = contactService.getUsers(user, searchContactRSDTO)
        render (template: "/contacts/listContacts", model:[contacts:contacts, searchContacts:searchContactRSDTO])
    }


    def addTagsToContact(Long contactId){
        List<String> tags = params.tags?.split(",")?.findAll{it}?:[]
        KuorumUser user = springSecurityService.currentUser
        ContactRSDTO contactRSDTO = contactService.getContact(user, contactId);
        contactRSDTO.tags = tags as Set
        contactRSDTO = contactService.updateContact(user, contactRSDTO)
        render contactRSDTO as JSON
    }

    def removeContact(Long contactId){
        KuorumUser user = springSecurityService.currentUser
        contactService.removeContact(user, contactId);
        render "";
    }

    def editContact(Long contactId){
        KuorumUser user = springSecurityService.currentUser
        ContactRSDTO contact = contactService.getContact(user, contactId)
        ContactCommand command = new ContactCommand();
        command.name = contact.name
        command.email = contact.email
        [command:command]
    }

    def newContact(){
        [command:new ContactCommand()]
    }

    def saveContact(ContactCommand command){
        if (command.hasErrors()){
            render view: 'newContact', model:[command:command]
        }
    }

    def importContacts() {}

    def importCSVContacts(){
        if (!params.get("fileContacts")) {
            flash.message = g.message(code:'tools.contact.import.csv.error.noFile')
            render(view: 'importContacts')
            return
        }
        MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('fileContacts')
        if (uploadedFile.empty) {
            flash.message = g.message(code:'tools.contact.import.csv.error.emptyFile')
            render(view: 'importContacts')
            return
        }
        InputStream data = uploadedFile.inputStream
        byte[] buffer = new byte[data.available()];
        data.read(buffer)
        File csv = File.createTempFile(uploadedFile.originalFilename, ".csv");
        OutputStream outStream = new FileOutputStream(csv);
        outStream.write(buffer);
        outStream.close()
        request.getSession().setAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY, csv);
        modelImportCSVContacts()

    }

    def importCSVContactsSave(){
        if (!request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)){
            flash.message = g.message(code:'tools.contact.import.csv.error.noFile')
            redirect(mapping:'politicianContactImport')
            return
        }

        List<String> columnOption = params.columnOption

        def namePos = columnOption.findIndexOf{it=="name"}
        def emailPos = columnOption.findIndexOf{it=="email"}
        def tagsPos = columnOption.findIndexValues{it=="tag"}
        def tags = params.tags?.split(",")?:[]
        Integer notImport = ((params.notImport?:[]) as List).collect{Integer.parseInt(it)}.max()?:0

        if (namePos == -1 || emailPos == -1){

            flash.message=g.message(code: 'tools.contact.import.csv.error.notEmailNameColumnSelected')
            render(view: 'importCSVContacts', model: modelImportCSVContacts())
            return;
        }


        File csv = (File)request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        InputStream csvStream = new FileInputStream(csv);
        Reader reader = new InputStreamReader(csvStream)
        Iterator lines = com.xlson.groovycsv.CsvParser.parseCsv([readFirstLine:true],reader)
        def contacts =[]
        //Not save first columns
        if (notImport>0) {
            for ( int i = 0; i < notImport; i++){
                lines.next();
            }
//            (0..notImport-1).each {lines.next()}
        }

        KuorumUser loggedUser = springSecurityService.currentUser
        Promise p = grails.async.Promises.task {
            lines.each{line ->
                ContactRDTO contact = new ContactRDTO()
                contact.setName(line[namePos])
                contact.setEmail(line[emailPos])
                contact.setTags(tagsPos.collect{line[it.intValue()]} as Set)
                contact.getTags().addAll(tags)
                contacts << contact
                if (contacts.size() > 1000){
                    contactService.addBulkContacts(loggedUser,contacts);
                    contacts.clear();
                }
            }

            contactService.addBulkContacts(loggedUser,contacts);
            csv.delete();
            session.removeAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        }
        p.onError { Throwable err ->
            log.error("An error occured importing contacts: ${err.message}");
        }
        p.onComplete { result ->
            log.info("Imported contacts has sent: $result");
        }


//        render contacts as JSON
    }

    private Map modelImportCSVContacts(){
        File csv = (File)request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        String fileName = (csv.name =~/(.*)([0-9]{19}\..*$)/)[0][1]
        def lines = parseCsvFile(csv)
        def line = lines.next()
        Set<Integer> emptyColumns= (0..line.values.length) as Set;
        while (line){
            line.values.eachWithIndex{val, idx ->
                if(val){
                    emptyColumns.remove(idx)
                }
            }
            if (lines.hasNext()) {
                line = lines.next()
            }else {
                line = null;
            }
        }
        lines = parseCsvFile(csv)
        [
                fileName:fileName,
                lines: lines,
                emptyColumns:emptyColumns
        ]
    }

    private def parseCsvFile(File csv){
        InputStream csvStream = new FileInputStream(csv);
        Reader reader = new InputStreamReader(csvStream)
        return com.xlson.groovycsv.CsvParser.parseCsv([readFirstLine:true],reader)
    }

    def contactTags(){
        KuorumUser loggedUser = springSecurityService.currentUser
        List<String> tags = contactService.getUserTags(loggedUser)
        render tags as JSON
    }
}
