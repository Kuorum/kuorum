package kuorum.web.interceptors

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.LocaleEditor
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.CookieLocaleResolver
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

//    CookieLocaleResolver localeResolver

    String paramName

    private static final List<String> SPANISH_LANGS =["es", "ca", "eu", "gl"]

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        GrailsWebRequest webRequest = GrailsWebRequest.lookup(request)
        def params = webRequest.params
        def localeParam = params?.get(paramName)
        AvailableLanguage userLanguage
        LocaleResolver localeResolver = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request)
        if (springSecurityService.isLoggedIn()){
            KuorumUser user = springSecurityService.getCurrentUser()
            userLanguage = user.language
        }else{
            userLanguage = AvailableLanguage.fromLocaleParam(localeParam);
            if (!userLanguage){
                Locale local = localeResolver.resolveLocale(request)
                if (SPANISH_LANGS.contains(local.language)){
                    userLanguage = AvailableLanguage.es_ES
                }else{
                    userLanguage = AvailableLanguage.en_EN
                }
            }
        }
        localeResolver?.setLocale request, response, userLanguage.locale
        return true;
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = (CookieLocaleResolver)localeResolver
    }

    public void setParamName(String paramName) {
        this.paramName = paramName
    }
}
