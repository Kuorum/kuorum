package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.project.Project
import kuorum.project.ProjectService
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.contact.ContactCommand
import kuorum.web.commands.payment.contact.ContactFilterCommand
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import org.bson.types.ObjectId
import org.kuorum.rest.model.contact.ContactPageRSDTO
import org.kuorum.rest.model.contact.ContactRDTO
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.contact.SearchContactRSDTO
import org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO
import org.kuorum.rest.model.contact.filter.FilterRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO
import org.kuorum.rest.model.contact.filter.condition.ConditionRDTO
import org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO
import org.kuorum.rest.model.contact.sort.SortContactsRDTO
import org.mozilla.universalchardet.UniversalDetector
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import payment.contact.ContactService

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class ContactsController {

    private static final String CONTACT_CSV_UPLOADED_SESSION_KEY="CONTACT_CSV_UPLOADED_SESSION_KEY"
    private static final String CONTACT_CSV_UPLOADED_EXTENSION=".csv"

    ContactService contactService;
    SpringSecurityService springSecurityService

    ProjectService projectService

    def index(){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
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
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Boolean asJson = params.asJson?true:false
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
        if (asJson){
            render contacts as JSON
        }else{
            render (template: "/contacts/listContacts", model:[contacts:contacts, searchContacts:searchContactRSDTO])
        }
    }


    def addTagsToContact(Long contactId){
        List<String> tags = params.tags?.split(",")?.findAll{it}?:[]
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contactRSDTO = contactService.getContact(user, contactId);
        contactRSDTO.tags = tags as Set
        contactRSDTO = contactService.updateContact(user, contactRSDTO, contactId)
        render contactRSDTO as JSON
    }

    def removeContact(Long contactId){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        contactService.removeContact(user, contactId);
        render "";
    }

    def editContact(Long contactId){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contact = contactService.getContact(user, contactId)
        ContactCommand command = new ContactCommand();
        command.name = contact.name
        command.email = contact.email?:g.message(code: 'tools.contact.edit.noMailVisible')
        KuorumUser contactUser = contact.mongoId?KuorumUser.get(new ObjectId(contact.mongoId)):null
        [command:command,contact:contact,contactUser:contactUser]
    }

    def updateContact(ContactCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contact = contactService.getContact(user, command.contactId)
        if (command.hasErrors() || !contact){
            KuorumUser contactUser = contact.mongoId?KuorumUser.get(new ObjectId(contact.mongoId)):null
            render view: "/contacts/editContact", model:[command:command,contact:contact,contactUser:contactUser]
            return;
        }
        contact.name = command.name
        contact.email = command.email
        ContactRSDTO contactUpdated = contactService.updateContact(user, contact, contact.getId())
        flash.message=g.message(code: 'tools.contact.edit.success', args: [contact.name])
        redirect(mapping:"politicianContactEdit", params: [contactId: contactUpdated.getId()])
    }

    def updateContactNotes(Long contactId){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contact = contactService.getContact(user, contactId)
        if (!contact){
            render ([err:g.message(code: 'tools.contact.edit.error')]  as JSON)
            return;
        }
        contact.notes = params.notes
        ContactRSDTO contactUpdated = contactService.updateContact(user, contact, contact.getId())
        render ([msg:g.message(code: 'tools.contact.edit.success', args: [contactUpdated.name])]  as JSON)

    }

    def newContact() {
        [command:new ContactCommand()]
    }

    def saveContact(ContactCommand command) {
        if (command.hasErrors()){
            render view: 'newContact', model:[command:command]
        }
        FilterRDTO filterRDTO = new FilterRDTO()
        filterRDTO.setFilterConditions([ConditionRDTO.factory(ConditionFieldTypeRDTO.EMAIL, TextConditionOperatorTypeRDTO.EQUALS.toString(), command.email)])
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactPageRSDTO alreadyExistsContact = contactService.getUsers(user, filterRDTO)
        ContactRSDTO contactRSDTO
        if (alreadyExistsContact.total>0){
            flash.message=g.message(code: 'tools.contact.new.alreadyExists')
            contactRSDTO = alreadyExistsContact.data.first()
            redirect(mapping: "politicianContactEdit", params: [contactId:contactRSDTO.id])
        }else{
            ContactRDTO contactRDTO = new ContactRDTO();
            contactRDTO.name = command.name
            contactRDTO.email = command.email
            contactRSDTO = contactService.addContact(user, contactRDTO)
            if (contactRSDTO){
                flash.message=g.message(code: 'tools.contact.new.success', args: [contactRSDTO.email])
                redirect(mapping: "politicianContactEdit", params: [contactId:contactRSDTO.id])
            }else{
                flash.error=g.message(code: 'tools.contact.new.error', args: [contactRSDTO.email])
                render view: 'newContact', model:[command:command]
            }
        }
    }

    def importContacts() {

    }

    def importCSVContacts() {
        request.getSession().removeAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY);
    }

    def importCSVContactsUpload() {
        if (!params.get("fileContacts")) {
            flash.error = g.message(code:'tools.contact.import.csv.error.noFile')
            redirect(mapping: 'politicianContactImportCSV')
            return
        }
        MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('fileContacts')
        if (uploadedFile.empty) {
            flash.error = g.message(code: 'tools.contact.import.csv.error.emptyFile')
            redirect(mapping: 'politicianContactImportCSV')
            return
        }
        if (!uploadedFile.originalFilename.endsWith(CONTACT_CSV_UPLOADED_EXTENSION)) {
            flash.error = g.message(code: 'tools.contact.import.csv.error.wrongExtension', args: [CONTACT_CSV_UPLOADED_EXTENSION])
            redirect(mapping: 'politicianContactImportCSV')
            return
        }
        File csv = File.createTempFile(uploadedFile.originalFilename, CONTACT_CSV_UPLOADED_EXTENSION);
        log.info("Creating temporal file ${csv.absoluteFile}")
        uploadedFile.transferTo(csv)
        log.info("Saved data into temporal file ${csv.absoluteFile}")
//        InputStream data = uploadedFile.inputStream
//        byte[] buffer = new byte[data.available()];
//        data.read(buffer)
//        OutputStream outStream = new FileOutputStream(csv);
//        outStream.write(buffer);
//        outStream.close()
        request.getSession().setAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY, csv);
        try {
            modelUploadCSVContacts()
        } catch(KuorumException e) {
            log.error("Error in the CSV file", e)
            flash.error = g.message(code: 'tools.contact.import.csv.error.noEmailColumn')
            redirect(mapping: 'politicianContactImportCSV')
        } catch(Exception e) {
            log.error("Error uploading CSV file", e)
            flash.error = g.message(code: 'tools.contact.import.csv.error.emptyFile')
            redirect(mapping: 'politicianContactImportCSV')
        }
    }

    def importCSVContactsSave(){
        if (!request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)){
            flash.error = g.message(code:'tools.contact.import.csv.error.noFile')
            redirect(mapping:'politicianContactImport')
            return
        }

        List<String> columnOption = params.columnOption

        Integer namePos = columnOption.findIndexOf{it=="name"}
        Integer emailPos = columnOption.findIndexOf{it=="email"}
        List<Number> tagsPos = columnOption.findIndexValues{it=="tag"}
        def tags = params.tags?.split(",")?:[]
        Integer notImport = ((params.notImport?:[]) as List).collect{Integer.parseInt(it)}.max()?:0

        List<String> realPos = params.realPos
        namePos = namePos<0 || namePos > realPos.size()?namePos:Integer.parseInt(realPos[namePos])
        emailPos = emailPos<0 || emailPos > realPos.size()?emailPos:Integer.parseInt(realPos[emailPos])
        tagsPos = tagsPos?.collect{Integer.parseInt(realPos[it.intValue()])}?:[]

        if (namePos == -1 || emailPos == -1){

            flash.error=g.message(code: 'tools.contact.import.csv.error.notEmailNameColumnSelected')

            try{
                def model = modelUploadCSVContacts(emailPos, namePos)
                render(view: 'importCSVContactsUpload', model: model)
                return;
            }catch (Exception e){
                log.error("Error uploading CSV file",e)
                flash.error = g.message(code:'tools.contact.import.csv.error.emptyFile')
                render(view: 'importContacts')
                return;
            }

        }


        File csv = (File)request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        log.info("Recovered temporal file ${csv.absoluteFile}")
        ObjectId loggedUserId = springSecurityService.principal.id

        asyncUploadContacts(loggedUserId, csv,notImport, namePos, emailPos, tagsPos, tags as List)

        session.removeAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        log.info("Programed async uploaded contacts")
        redirect(mapping:'politicianContactImportSuccess')
//        render contacts as JSON
    }

    private static final def EMAIL_PATTERN =/^[_A-Za-z0-9\\.]+[\+_A-Za-z0-9]*@[A-Za-z0-9-]+\.[A-Za-z]{2,}$/
    private Integer detectEmailPosition(final File csv){
        Iterator lines = parseCsvFile(csv)
        Integer emailPos = null;
        while (lines.hasNext() && !emailPos){
            def line = lines.next();
            Integer i = 0;
            line.values.each{ val ->
                if (val && val.trim() =~ EMAIL_PATTERN){
                    emailPos = i;
                }
                i++;
            }
        }
        emailPos
    }

    private void asyncUploadContacts(final ObjectId loggedUserId, final File csv, final Integer notImport, final Integer namePos, final Integer emailPos, final List<Integer> tagsPos, final List<String> tags){
//        Promise p = grails.async.Promises.task {
            try{
                log.info("Importing ${csv.absoluteFile}")
                Iterator lines = parseCsvFile(csv)
                KuorumUser loggedUser = KuorumUser.get(loggedUserId)
                def contacts =[]
                //Not save first columns
                if (notImport>0) {
                    for ( int i = 0; i < notImport; i++){
                        lines.next();
                    }
                }

                lines.each{line ->
                    ContactRDTO contact = new ContactRDTO()
                    contact.setName(line[namePos])
                    contact.setEmail(line[emailPos])
                    def tagsSecuredTransformed = tagsPos.collect{
                        try{
                            line[it.intValue()]
                        }catch (Exception e){
                            //Wrong file
                            ""
                        }
                    }
                    contact.setTags(tagsSecuredTransformed as Set)
                    contact.getTags().addAll(tags)
                    contacts << contact
                    if (contacts.size() > 1000){
                        contactService.addBulkContacts(loggedUser,contacts);
                        contacts.clear();
                    }
                }

                contactService.addBulkContacts(loggedUser,contacts);
                log.info("Finisehd ${csv.absoluteFile}")
                csv.delete();
//                return "SUCCESS";
            }catch (Exception e){
                log.error("Captured exception importing contacts: ${e.message}", e);
            }
//        }
//        p.onError { Throwable err ->
//            log.error("An error occured importing contacts: ${err.message}", err);
//        }
//        p.onComplete { result ->
//            log.info("Imported contacts has sent: $result");
//        }
//        try{
//            p.get(1, TimeUnit.MICROSECONDS)
//        }catch (Throwable e){
//            log.warn("Me la pela esta exception")
//        }
    }

    private Map modelUploadCSVContacts(Integer emailPos = -1, Integer namePos = -1){
        File csv = (File)request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        log.info("Calculating uploaded name: "+csv)
        String fileNamePattern = "(.*[a-zA-Z])([0-9]+${CONTACT_CSV_UPLOADED_EXTENSION})\$"
        String fileName = (csv.name =~/${fileNamePattern}/)[0][1]
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
        if (emailPos == null || emailPos < 0){
            emailPos = detectEmailPosition(csv);
        }
        if (emailPos == null) {
            throw new KuorumException("No se ha detectado email", "Kuorum.exception.code.importCSVNoEmail")
        }
        lines = parseCsvFile(csv)
        [
                fileName:fileName,
                lines: lines,
                emptyColumns:emptyColumns,
                emailPos:emailPos,
                namePos:namePos
        ]
    }

    private def parseCsvFile(File csv){
        InputStream csvStream = new FileInputStream(csv);
        String charSet = detectCharset(csv)
        Reader reader
        if (charSet){
            reader = new InputStreamReader(csvStream, charSet)
        }else{
            reader = new InputStreamReader(csvStream)
        }
        return com.xlson.groovycsv.CsvParser.parseCsv([readFirstLine:true],reader)
    }

    private String detectCharset(File csv){
        java.io.FileInputStream fis = new java.io.FileInputStream(csv);
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();
        String encoding = detector.getDetectedCharset()
        log.info("File uploaded with encoding: "+encoding)
        fis.close()
        encoding
    }

    def contactTags(){
        KuorumUser loggedUser = KuorumUser.get(springSecurityService.principal.id)
        List<String> tags = contactService.getUserTags(loggedUser)
        render tags as JSON
    }

    def importSuccess(){}



    /* UNSUBSCRIBE */
    // USING userId instead of alias because the unsusbscribe email can't change.
    @Secured("permitAll")
    def unsubscribe(String userId, String email, String digest){
        KuorumUser user = KuorumUser.get(new ObjectId(userId));
        if (user == null){
            redirect controller: 'error', action: 'notFound'
            return ;
        }
        ContactRSDTO contact = contactService.checkContactUser(user, email, digest);
        if (contact == null){
            redirect controller: 'error', action: 'notFound'
            return ;
        }
        List<Project> userProjects = projectService.politicianProjects(user)
        [user:user,userProjects:userProjects, contact:contact, digest:digest]
    }

    @Secured("permitAll")
    def unsubscribeConfirm(String userId, String email, String digest){

    }
}
