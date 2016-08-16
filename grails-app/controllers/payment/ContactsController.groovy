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
