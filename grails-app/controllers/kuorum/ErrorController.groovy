package kuorum

import kuorum.core.exception.KuorumException

import javax.servlet.http.HttpServletRequest

class ErrorController {

    def springSecurityService

    def forbidden() {
        log.info("Page not allowed: ${((HttpServletRequest)request).getForwardURI()} by user ${springSecurityService.principal?.id}")
    }
    def notFound() {
        log.debug("Page not found: ${((HttpServletRequest)request).getForwardURI()}")
    }
    def notAuthorized() {
        log.debug("Page not autorized: ${((HttpServletRequest)request).getForwardURI()}")
    }

    def kuorumExceptionHandler(){
        KuorumException exception = request.exception?.cause
        log.error("KuorumException: "+exception.message)
        [errorMessage:message(code:exception.errors[0]?.code?:'error.kuorumException.description')]
    }

    def cookieLost(){
        KuorumException exception = request.exception?.cause
        log.error("Cookie lost: "+exception.message)
        render view: 'kuorumExceptionHandler', model:[errorMessage:message(code:exception.errors[0]?.code?:'error.kuorumException.description')]
    }
    def internalError(){
        def exception = request.exception.cause
        if (request.getForwardURI() == g.createLink(mapping: "politicianContactImportCSV")){
            flash.error=g.message(code:'tools.contact.import.csv.error.generic')
            redirect (mapping:"politicianContactImportCSV")
            return;
        }
        def email = grailsApplication.config.kuorum.contact.email
        log.error("KuorumException: "+exception?.message?:"-no Exception attached-")
        [email:email]
    }
}
