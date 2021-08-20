package kuorum

import kuorum.core.customDomain.CustomDomainResolver
import kuorum.users.CookieUUIDService
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.kuorum.rest.model.contact.ContactRSDTO
import org.kuorum.rest.model.kuorumUser.BasicDataKuorumUserRSDTO
import springSecurity.KuorumRegisterCommand

class CensusLoginFilters {

    def springSecurityService

    def kuorumUserService

    def censusService

    CookieUUIDService cookieUUIDService

    def dependsOn=[RRSSConfigFilters, CorsFilters, CustomRegisterFilterFilters,LogsContextFilters]

    def filters = {
        censusLoginFilter(controller:'campaign', action:'show') {
            before = {
//                if (!springSecurityService.isLoggedIn()){
                    if (params['censusLogin']){
                        String censusLogin = params['censusLogin'];
                        String redirectUrl = "https://"+ CustomDomainResolver.domain + request.forwardURI;
                        cookieUUIDService.setDomainCookie(WebConstants.COOKIE_URL_CALLBACK_CENSUS_LOGIN, redirectUrl)
                        def params = [censusLogin:censusLogin]
                        if (params[WebConstants.WEB_PARAM_LANG]) {
                            params.put(WebConstants.WEB_PARAM_LANG,params[WebConstants.WEB_PARAM_LANG])
                        }
                        redirect(mapping: 'campaignValidationLinkCheck', params: params)
                        return false;
                    }
//                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
