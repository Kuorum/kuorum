package payment

import com.xlson.groovycsv.CsvParser
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.exception.KuorumException
import kuorum.dashboard.DashboardService
import kuorum.users.KuorumUser
import kuorum.web.commands.payment.contact.*
import kuorum.web.commands.payment.massMailing.MassMailingCommand
import kuorum.web.session.CSVDataSession
import org.bson.types.ObjectId
import org.kuorum.rest.model.contact.*
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
    private static final String CONTACT_CSV_UPLOADED_EXTENSION = ".csv"

    ContactService contactService
    SpringSecurityService springSecurityService
    DashboardService dashboardService

    // Grails renderer -> For CSV hack
    grails.gsp.PageRenderer groovyPageRenderer

    def index(ContactFilterCommand filterCommand){

        if (dashboardService.forceUploadContacts()){
            render view: "/dashboard/payment/paymentNoContactsDashboard", model: [:]
            return;
        }

        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SearchContactRSDTO searchContactRSDTO  = new SearchContactRSDTO()
        searchContactRSDTO.sort = new SortContactsRDTO(field:ConditionFieldTypeRDTO.NAME, direction: SortContactsRDTO.Direction.ASC)
        ContactPageRSDTO contacts = contactService.getUsers(user,searchContactRSDTO)
        if (contacts.total <= 0) {
            redirect mapping:"politicianContactImport"
            return
        }

        MassMailingCommand command = new MassMailingCommand();
        ExtendedFilterRSDTO anonymousFilter = null;
        if (filterCommand.filterConditions){
            anonymousFilter = new ExtendedFilterRSDTO();
            anonymousFilter.id = -1;
            anonymousFilter.filterConditions = filterCommand.buildFilter().filterConditions;
            anonymousFilter.operator = filterCommand.operator
            //SearchContactRSDTO temporalContact = new SearchContactRSDTO(filter: anonymousFilter);
            ContactPageRSDTO temporalContacts = contactService.getUsers(user, anonymousFilter);
            anonymousFilter.amountOfContacts = temporalContacts.total;
            anonymousFilter.name = g.message(code:'tools.contact.filter.newAnonymousName');
            command.filterId = anonymousFilter.id
        }

        List<ExtendedFilterRSDTO> filters = contactService.getUserFilters(user)

        [
                contacts:contacts,
                filters:filters,
                anonymousFilter:anonymousFilter,
                totalContacts:contacts.total,
                command:command,
                searchContacts:searchContactRSDTO,
                bulkActionContactsCommand: new BulkActionContactsCommand(),
                bulkAddTagsContactsCommand: new BulkAddTagsContactsCommand()
        ]

    }

    def searchContacts(ContactFilterCommand filterCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        Boolean asJson = params.asJson?true:false
        SearchContactRSDTO searchContactRSDTO  = new SearchContactRSDTO()
        populateSearchContact(searchContactRSDTO, filterCommand, params)
        ContactPageRSDTO contacts = contactService.getUsers(user, searchContactRSDTO)
        if (asJson){
            render contacts as JSON
        }else{
            render (template: "/contacts/listContacts", model: [
                    contacts: contacts,
                    searchContacts: searchContactRSDTO,
                    bulkActionContactsCommand: new BulkActionContactsCommand()
            ])
        }
    }


    def addTagsToContact(Long contactId){
        List<String> tags = params.tags?.split(",")?.findAll{it}?:[]
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contactRSDTO = contactService.getContact(user, contactId)
        contactRSDTO.tags = tags as Set
        contactRSDTO = contactService.updateContact(user, contactRSDTO, contactId)
        render contactRSDTO as JSON
    }

    def removeContact(Long contactId){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        contactService.removeContact(user, contactId)

        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO()
        searchContactRSDTO.sort = new SortContactsRDTO(field:ConditionFieldTypeRDTO.NAME, direction: SortContactsRDTO.Direction.ASC)
        ContactPageRSDTO contacts = contactService.getUsers(user,searchContactRSDTO)

        Map result =[contacts: contacts.getTotal()]

        render "" //ÑAPA para filtros dinamicos
    }

    def editContact(Long contactId){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contact = contactService.getContact(user, contactId)
        ContactCommand command = new ContactCommand()
        command.name = contact.name
        command.email = contact.email?:g.message(code: 'tools.contact.edit.noMailVisible')
        command.surname = contact.surname
        KuorumUser contactUser = contact.mongoId?KuorumUser.get(new ObjectId(contact.mongoId)):null
        [command:command,contact:contact,contactUser:contactUser]
    }

    def updateContact(ContactCommand command){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contact = contactService.getContact(user, command.contactId)
        if (command.hasErrors() || !contact){
            KuorumUser contactUser = contact.mongoId?KuorumUser.get(new ObjectId(contact.mongoId)):null
            render view: "/contacts/editContact", model:[command:command,contact:contact,contactUser:contactUser]
            return
        }
        contact.name = command.name
        contact.email = command.email
        contact.surname = command.surname
        ContactRSDTO contactUpdated = contactService.updateContact(user, contact, contact.getId())
        flash.message=g.message(code: 'tools.contact.edit.success', args: [contact.name])
        redirect(mapping:"politicianContactEdit", params: [contactId: contactUpdated.getId()])
    }

    def updateContactNotes(Long contactId){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        ContactRSDTO contact = contactService.getContact(user, contactId)
        if (!contact){
            render ([err:g.message(code: 'tools.contact.edit.error')]  as JSON)
            return
        }
        contact.notes = params.notes
        ContactRSDTO contactUpdated = contactService.updateContact(user, contact, contact.getId())
        render ([msg:g.message(code: 'tools.contact.edit.success', args: [contactUpdated.name])]  as JSON)

    }

    def newContact() {
        [command:new NewContactCommand()]
    }

    def saveContact(NewContactCommand command) {
        if (!command.email){
            command.errors.rejectValue("email","kuorum.web.commands.payment.contact.ContactCommand.email.nullable")
        }
        if (command.hasErrors()){
            render view: 'newContact', model:[command:command]
            return
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
            ContactRDTO contactRDTO = new ContactRDTO()
            contactRDTO.name = command.name
            contactRDTO.surname = command.surname
            contactRDTO.email = command.email
            contactRSDTO = contactService.addContact(user, contactRDTO)
            String displayerName = "${contactRSDTO.name} ${contactRSDTO.surname}"
            if (contactRSDTO){
                flash.message=g.message(code: 'tools.contact.new.success', args: [displayerName])
                redirect(mapping: "politicianContactEdit", params: [contactId:contactRSDTO.id])
            }else{
                flash.error=g.message(code: 'tools.contact.new.error', args: [displayerName])
                render view: 'newContact', model:[command:command]
            }
        }
    }

    def importContacts() {

    }

    def importCSVContacts() {
        request.getSession().removeAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
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
        if (!uploadedFile.originalFilename?.toLowerCase().endsWith(CONTACT_CSV_UPLOADED_EXTENSION)) {
            flash.error = g.message(code: 'tools.contact.import.csv.error.wrongExtension', args: [CONTACT_CSV_UPLOADED_EXTENSION])
            redirect(mapping: 'politicianContactImportCSV')
            return
        }
        CSVDataSession csvDataSession = new CSVDataSession()
        File csv = File.createTempFile(uploadedFile.originalFilename, CONTACT_CSV_UPLOADED_EXTENSION)
        csvDataSession.file = csv
        log.info("Creating temporal file ${csv.absoluteFile}")
        uploadedFile.transferTo(csv)
        log.info("Saved data into temporal file ${csv.absoluteFile}")
        request.getSession().setAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY, csvDataSession)
        try {
            def model = modelUploadCSVContacts()
            // THe table is rendered using the hack because some files are wrong and it is not possible to detect them.
            // If painting the table fails, the exception is thrown and can be handled with ErrorController.
            // If the exception is thrown while is been rendered, AWS not redirect to ErrorController, it shows a ugly page.
            def table = groovyPageRenderer.render(template: '/contacts/csvTableExample', model: model)
            model["table"] = table
            model
        } catch(KuorumException e) {
            log.error("Error in the CSV file", e)
            flash.error = g.message(code: e.getMessage())
            redirect(mapping: 'politicianContactImportCSV')
        } catch(Exception e) {
            log.error("Error uploading CSV file", e)
            flash.error = g.message(code: 'tools.contact.import.csv.error.emptyFile')
            redirect(mapping: 'politicianContactImportCSV')
        }
    }

    def importCSVContactsSave(){
        if (!request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)) {
            flash.error = g.message(code:'tools.contact.import.csv.error.noFile')
            redirect(mapping:'politicianContactImport')
            return
        }

        // From session
        CSVDataSession csvDataSession = (CSVDataSession) request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)

        List<String> columnOption = params.columnOption

        Integer namePos = columnOption.findIndexOf{it=="name"}
        Integer surnamePos = columnOption.findIndexOf{it=="surname"}
        Integer emailPos = columnOption.findIndexOf{it=="email"}
        List<Number> tagsPos = columnOption.findIndexValues{it=="tag"}
        def tags = params.tags?.split(",")?:[]

        Integer numOfTotalColumns = csvDataSession.numTotalColumns
        Integer numOfEmptyColumns = csvDataSession.numOfEmptyColumns
        Integer notImport = ((params.notImport?:[]) as List).collect{Integer.parseInt(it)}.max()?:0

        List<String> realPos = params.realPos
        surnamePos = surnamePos<0 || surnamePos > realPos.size() ? surnamePos : Integer.parseInt(realPos[surnamePos])
        emailPos = emailPos<0 || emailPos > realPos.size() ? emailPos : Integer.parseInt(realPos[emailPos])
        namePos = namePos<0 || namePos > realPos.size() ? namePos : Integer.parseInt(realPos[namePos])
        tagsPos = tagsPos?.collect{Integer.parseInt(realPos[it.intValue()])}?:[]

        if ((namePos == -1 && (numOfTotalColumns - numOfEmptyColumns) != 1) || emailPos == -1) {

            flash.error=g.message(code: 'tools.contact.import.csv.error.notEmailNameColumnSelected')

            try {
                def model = modelUploadCSVContacts(emailPos, namePos, surnamePos)
                def table = groovyPageRenderer.render(template: '/contacts/csvTableExample', model: model)
                model["table"] = table
                render(view: 'importCSVContactsUpload', model: model)
                return
            } catch (Exception e) {
                log.error("Error uploading CSV file",e)
                flash.error = g.message(code:'tools.contact.import.csv.error.emptyFile')
                render(view: 'importContacts')
                return
            }
        }

        File csv = csvDataSession.file
        log.info("Recovered temporal file ${csv.absoluteFile}")
        ObjectId loggedUserId = springSecurityService.principal.id

        asyncUploadContacts(loggedUserId, csv,notImport, namePos,surnamePos, emailPos, tagsPos, tags as List)

        session.removeAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        log.info("Programed async uploaded contacts")
        redirect(mapping:'politicianContactImportSuccess')
//        render contacts as JSON
    }

    private static final def EMAIL_PATTERN =/^[_A-Za-z0-9\\.]+[\+_A-Za-z0-9]*@[A-Za-z0-9-]+\.[A-Za-z]{2,}$/
    private Integer detectEmailPosition(final File csv){
        Iterator lines = parseCsvFile(csv)
        Integer emailPos = null
        while (lines.hasNext() && !emailPos){
            def line = lines.next()
            Integer i = 0
            line.values.each{ val ->
                if (val && val.trim() =~ EMAIL_PATTERN){
                    emailPos = i
                }
                i++
            }
        }
        emailPos
    }

    private void asyncUploadContacts(final ObjectId loggedUserId, final File csv, final Integer notImport, final Integer namePos, final Integer surnamePos, final Integer emailPos, final List<Integer> tagsPos, final List<String> tags){
//        Promise p = grails.async.Promises.task {
            try{
                log.info("Importing ${csv.absoluteFile}")
                Iterator lines = parseCsvFile(csv)
                KuorumUser loggedUser = KuorumUser.get(loggedUserId)
                def contacts = []
                // Not save first columns
                if (notImport > 0) {
                    for (int i = 0; i < notImport; i++){
                        lines.next()
                    }
                }

                lines.each{line ->
                    // Ignore empty rows
                    if (line.values.length == 1 && line[0].isEmpty()) {
                        return
                    }

                    ContactRDTO contact = new ContactRDTO()
                    if (namePos >= 0) {
                        contact.setName(line[namePos] as String)
                    }
                    if (surnamePos >= 0) {
                        contact.setSurname(line[surnamePos] as String)
                    }
                    contact.setEmail(line[emailPos] as String)
                    def tagsSecuredTransformed = tagsPos.collect{
                        try {
                            line[it.intValue()]
                        } catch (Exception ignored) {
                            // Wrong file
                            ""
                        }
                    }
                    contact.setTags(tagsSecuredTransformed as Set<String>)
                    contact.getTags().addAll(tags)
                    contacts << contact
                    if (contacts.size() > 1000) {
                        contactService.addBulkContacts(loggedUser,contacts)
                        contacts.clear()
                    }
                }

                contactService.addBulkContacts(loggedUser,contacts)
                log.info("Finisehd ${csv.absoluteFile}")
                csv.delete()
//                return "SUCCESS";
            } catch (Exception e) {
                log.error("Captured exception importing contacts: ${e.message}", e)
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

    private Map modelUploadCSVContacts(Integer emailPos = -1, Integer namePos = -1, surnamePos= -1) {
        CSVDataSession csvDataSession = (CSVDataSession) request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
        File csv = csvDataSession.file
        log.info("Calculating uploaded name: "+csv)
        String fileNamePattern = "(.*[a-zA-Z])([0-9]+${CONTACT_CSV_UPLOADED_EXTENSION})\$"
        String fileName = (csv.name =~/${fileNamePattern}/)[0][1]
        def linesIterator = parseCsvFile(csv)
        def nextLine = linesIterator.next()
        Set<Integer> emptyColumns = (0..nextLine.values.length) as Set
        csvDataSession.numTotalColumns = nextLine.values.length
        while (nextLine) {
            // Empty row => error
            if (nextLine.values.join("").isEmpty()) {
                throw new KuorumException("tools.contact.import.csv.error.emptyRow")
            }

            nextLine.values.eachWithIndex{val, idx ->
                if (val) {
                    emptyColumns.remove(idx)
                }
            }
            if (linesIterator.hasNext()) {
                nextLine = linesIterator.next()
            } else {
                nextLine = null
            }
        }
        if (emailPos == null || emailPos < 0) {
            emailPos = detectEmailPosition(csv)
        }
        if (emailPos == null) {
            throw new KuorumException("tools.contact.import.csv.error.noEmailColumn")
        }

        // Save in session
        csvDataSession.numOfEmptyColumns = emptyColumns.size() - 1

        linesIterator = parseCsvFile(csv)
        [
                fileName: fileName,
                lines: linesIterator,
                emptyColumns: emptyColumns,
                emailPos: emailPos,
                namePos: namePos,
                surnamePos: surnamePos
        ]
    }

    private def parseCsvFile(File csv){
        InputStream csvStream = new FileInputStream(csv)
        String charSet = detectCharset(csv)
        Reader reader
        if (charSet){
            reader = new InputStreamReader(csvStream, charSet)
        }else{
            reader = new InputStreamReader(csvStream)
        }
        return CsvParser.parseCsv([readFirstLine:true],reader)
    }

    private String detectCharset(File csv){
        FileInputStream fis = new FileInputStream(csv)
        byte[] buf = new byte[4096]
        UniversalDetector detector = new UniversalDetector(null)
        int nread
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread)
        }
        detector.dataEnd()
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

    def importSuccess(){
        log.info("Skipping force upload contacts")
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        user.skipUploadContacts = true;
        user.save()
    }



    /* UNSUBSCRIBE */
    // USING userId instead of alias because the unsusbscribe email can't change.
    @Secured("permitAll")
    def unsubscribe(String userId, String email, String digest){
        KuorumUser user = KuorumUser.get(new ObjectId(userId))
        if (user == null){
            redirect controller: 'error', action: 'notFound'
            return
        }
        ContactRSDTO contact = contactService.checkContactUser(user, email, digest)
        if (contact == null){
            redirect controller: 'error', action: 'notFound'
            return
        }
        boolean success = contactService.unsubscribeContactUser(user, email, digest)
        if (!success){
            flash.error="There was an error deleting your user. If the problem persists, please contact with info@kuorum.org"
            redirect mapping:'userUnsubscribe', params: [userId:userId, email:email, digest: digest]
            return
        }
        [user:user, contact:contact]
    }

    def removeContactsBulkAction(BulkRemoveContactsCommand bulkRemoveCommand) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        // Filter
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO()
        Long filterId = Long.parseLong(params.filterId?:'0')
        FilterRDTO filterRDTO = bulkRemoveCommand.buildFilter()

        if (filterId < 0 || filterRDTO.filterConditions) {
            searchContactRSDTO.filter = filterRDTO
        } else if (filterId == 0l) {
            // NO FILTER -> ALL CONTACTS
        } else {
            searchContactRSDTO.filterId = filterId
        }

        if (bulkRemoveCommand.validate()) {
            if (contactService.bulkRemoveContacts(user, searchContactRSDTO)) {
                return render ([
                    status: 'ok',
                    msg: g.message(code: "modal.bulkAction.deleteAll.done")
                ] as JSON)
            } else {
                return render ([
                    status: 'error',
                    msg: g.message(code: "modal.bulkAction.deleteAll.error.deleting")
                ] as JSON)
            }
        } else {
            return render ([
                status: 'error',
                msg: g.message(code: "modal.bulkAction.deleteAll.error.validating")
            ] as JSON)
        }
    }

    def addTagsBulkAction(BulkAddTagsContactsCommand bulkAddTagsCommand) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        // Filter
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO()
        Long filterId = Long.parseLong(params.filterId?:'0')
        FilterRDTO filterRDTO = bulkAddTagsCommand.buildFilter()

        if (filterId < 0 || filterRDTO.filterConditions) {
            searchContactRSDTO.filter = filterRDTO
        } else if (filterId == 0l) {
            // NO FILTER -> ALL CONTACTS
        } else {
            searchContactRSDTO.filterId = filterId
        }

        if (bulkAddTagsCommand.validate()) {
            BulkUpdateContactTagsRDTO bulkUpdateContactTagsRDTO = new BulkUpdateContactTagsRDTO()
            bulkUpdateContactTagsRDTO.setSearchContacts(searchContactRSDTO)
            bulkUpdateContactTagsRDTO.setAddTagNames(bulkAddTagsCommand.tags)

            if (contactService.bulkAddTagsContacts(user, bulkUpdateContactTagsRDTO)) {
                return render ([
                    status: 'ok',
                    msg: g.message(code: "modal.bulkAction.addTags.processing")
                ] as JSON)
            } else {
                return render ([
                    status: 'error',
                    msg: g.message(code: "modal.bulkAction.addTags.error.adding")
                ] as JSON)
            }
        } else {
            return render ([
                status: 'error',
                msg: g.message(code: "modal.bulkAction.addTags.error.validating")
            ] as JSON)
        }
    }

    def removeTagsBulkAction(BulkRemoveTagsContactsCommand bulkRemoveTagsCommand) {
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)

        // Filter
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO()
        Long filterId = Long.parseLong(params.filterId?:'0')
        FilterRDTO filterRDTO = bulkRemoveTagsCommand.buildFilter()

        if (filterId < 0 || filterRDTO.filterConditions) {
            searchContactRSDTO.filter = filterRDTO
        } else if (filterId == 0l) {
            // NO FILTER -> ALL CONTACTS
        } else {
            searchContactRSDTO.filterId = filterId
        }

        if (bulkRemoveTagsCommand.validate()) {
            BulkUpdateContactTagsRDTO bulkUpdateContactTagsRDTO = new BulkUpdateContactTagsRDTO()
            bulkUpdateContactTagsRDTO.setSearchContacts(searchContactRSDTO)
            bulkUpdateContactTagsRDTO.setRemoveTagNames(bulkRemoveTagsCommand.tags)

            if (contactService.bulkRemoveTagsContacts(user, bulkUpdateContactTagsRDTO)) {
                return render ([
                        status: 'ok',
                        msg: g.message(code: "modal.bulkAction.removeTags.processing")
                ] as JSON)
            } else {
                return render ([
                        status: 'error',
                        msg: g.message(code: "modal.bulkAction.removeTags.error.removing")
                ] as JSON)
            }
        } else {
            return render ([
                    status: 'error',
                    msg: g.message(code: "modal.bulkAction.removeTags.error.validating")
            ] as JSON)
        }
    }

    def exportContacts(ContactFilterCommand filterCommand){
        KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
        SearchContactRSDTO searchContactRSDTO = new SearchContactRSDTO();
        populateSearchContact(searchContactRSDTO, filterCommand, params)
        contactService.exportContacts(user, searchContactRSDTO)
        render ([success:"ok"] as JSON)
    }

    private void populateSearchContact(SearchContactRSDTO searchContactRSDTO, ContactFilterCommand filterCommand, def params){
        searchContactRSDTO.page = Long.parseLong(params.page?:"0")
        searchContactRSDTO.size = params.size?Long.parseLong(params.size):searchContactRSDTO.size
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
    }
}
