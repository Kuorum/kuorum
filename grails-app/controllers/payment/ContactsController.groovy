package payment

import grails.converters.JSON
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class ContactsController {

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
        request.getSession().setAttribute("fileContacts", csv);
        InputStream csvStream = new FileInputStream(csv);
        Reader reader = new InputStreamReader(csvStream)
        def lines = com.xlson.groovycsv.CsvParser.parseCsv(reader)
        [
                fileName:uploadedFile.originalFilename,
                lines: lines
        ]

    }

    def importCSVContactsSave(){

    }

    def contactTags(){

        render (
                ["con espacios",
                 "con espacios2",
                 "perritoFaldero",
                 "republicano",
                 "estafador",
                 "ecologista",
                 "escandaloso",
                 "verde",
                 "veterano",
                 "anti-abortista",
                 "vividor",
                "pepero",
                "podemita",
                 "pacifista"
            ] as JSON
        )
    }
}
