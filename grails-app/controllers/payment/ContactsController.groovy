package payment

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import kuorum.users.KuorumUser
import org.kuorum.rest.model.contact.ContactRSDTO
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import payment.contact.ContactService

@Secured("ROLE_POLITICIAN")
class ContactsController {

    private static final String CONTACT_CSV_UPLOADED_SESSION_KEY="CONTACT_CSV_UPLOADED_SESSION_KEY"

    ContactService contactService;
    SpringSecurityService springSecurityService

    def importContacts() {}

    def importCSVContacts(){
        if (!params.get("fileContacts")) {
            flash.message = 'Not file defined'
            render(view: 'importContacts')
            return
        }
        MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('fileContacts')
        if (uploadedFile.empty) {
            flash.message = 'file cannot be empty'
            render(view: 'importContacts')
            return
        }
        InputStream data = uploadedFile.inputStream
        byte[] buffer = new byte[data.available()];
        data.read(buffer)
        File csv = File.createTempFile("temp-file-name-${new Date().time}", ".csv");
        OutputStream outStream = new FileOutputStream(csv);
        outStream.write(buffer);
        outStream.close()
        request.getSession().setAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY, csv);
        InputStream csvStream = new FileInputStream(csv);
        Reader reader = new InputStreamReader(csvStream)
        def lines = com.xlson.groovycsv.CsvParser.parseCsv(reader)
        [
                fileName:uploadedFile.originalFilename,
                lines: lines
        ]

    }

    def importCSVContactsSave(){
        if (!request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)){
            flash.message = 'Not file defined'
            render(view: 'importContacts')
            return
        }
        File csv = (File)request.getSession().getAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)

        List<String> columnOption = params.columnOption

        def namePos = columnOption.findIndexOf{it=="name"}
        def emailPos = columnOption.findIndexOf{it=="email"}
        def tagsPos = columnOption.findIndexValues{it=="tag"}
        def tags = params.tags.split(",")

        InputStream csvStream = new FileInputStream(csv);
        Reader reader = new InputStreamReader(csvStream)
        def lines = com.xlson.groovycsv.CsvParser.parseCsv(reader)
        def contacts =[]
        lines.each{line ->
            ContactRSDTO contact = new ContactRSDTO()
            contact.setName(line[namePos])
            contact.setEmail(line[emailPos])
            contact.setTags(tagsPos.collect{line[it.intValue()]} as Set)
            contact.getTags().addAll(tags)
            contacts << contact
        }
        KuorumUser loggedUser = springSecurityService.currentUser
        contactService.addBulkContacts(loggedUser,contacts);

        csv.delete();
        session.removeAttribute(CONTACT_CSV_UPLOADED_SESSION_KEY)
//        render contacts as JSON
    }

    def contactTags(){
        KuorumUser loggedUser = springSecurityService.currentUser
        List<String> tags = contactService.getUserTags(loggedUser)
        render tags as JSON
    }
}
