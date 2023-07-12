package kuorum.captcha

import grails.transaction.Transactional
import groovyx.net.http.RESTClient
import kuorum.core.customDomain.CustomDomainResolver
import org.springframework.beans.factory.annotation.Value

@Transactional
class CaptchaService {

    @Value('${recaptcha.providers.google.secretKey}')
    String RECAPTCHA_SECRET

    def verifyCaptcha(String responseCaptcha) {

            String secretKey = RECAPTCHA_SECRET
            String path = "/recaptcha/api/siteverify"
            def query = [secret: secretKey, response: responseCaptcha]
            RESTClient mailKuorumServices = new RESTClient("https://www.google.com")
            def response = mailKuorumServices.get(path: path,
                    headers: ["User-Agent": "Kuorum Web"],
                    query: query,
                    requestContentType: groovyx.net.http.ContentType.JSON)

            log.info("Checking CAPTCHA :: Google response - ${response.data.hostname} || domain : ${CustomDomainResolver.domain}")

            def isCaptchaVerified = false
            if (response.data.hostname != CustomDomainResolver.domain) {
                log.info("invalid captcha domain")
            } else {
                isCaptchaVerified = response.data.success
            }
            return isCaptchaVerified

    }
}
