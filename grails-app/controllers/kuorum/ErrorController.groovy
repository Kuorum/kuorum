package kuorum

import javax.servlet.http.HttpServletRequest

class ErrorController {

    def springSecurityService

    def forbidden() {
        log.info("Page not allowed: ${((HttpServletRequest)request).getRequestURL()} by user ${springSecurityService.principal.id}")
    }
    def notFound() {
        log.debug("Page not foung: ${((HttpServletRequest)request).getRequestURL()}")
    }
    def kuorumExceptionHandler(){
        def exception = request.exception
    }
    def internalError(){
        def exception = request.exception
    }
}
