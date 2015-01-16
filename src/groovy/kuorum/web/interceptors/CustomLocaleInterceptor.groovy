package kuorum.web.interceptors

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.LocaleEditor
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.support.RequestContextUtils

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by iduetxe on 16/01/15.
 */
class CustomLocaleInterceptor extends LocaleChangeInterceptor{

    @Autowired
    SpringSecurityService springSecurityService

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        GrailsWebRequest webRequest = GrailsWebRequest.lookup(request)
        def params = webRequest.params
        def localeParam = params?.get(paramName)
        AvailableLanguage userLanguage = AvailableLanguage.fromLocaleParam(localeParam);
        if (!userLanguage && springSecurityService.isLoggedIn()){
            KuorumUser user = springSecurityService.getCurrentUser()
            userLanguage = user.language
        }
        if (userLanguage){
            def localeResolver = RequestContextUtils.getLocaleResolver(request)
            localeResolver?.setLocale request, response, userLanguage.locale
            return true;

        }else{
            return super.preHandle(request,response,handler)
        }
    }
}
