package kuorum.web.interceptors

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.customDomain.CustomDomainResolver
import kuorum.core.model.AvailableLanguage
import kuorum.register.KuorumUserSession
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

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
        request.getServerName()
        LocaleResolver localeResolver = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request)
        try{
            if (springSecurityService.isLoggedIn()){
                KuorumUserSession user = springSecurityService.principal
                userLanguage = user.language
            }else{
                userLanguage = AvailableLanguage.fromLocaleParam(localeParam)
                if (!userLanguage){
                    userLanguage = getLanguageFromDomain(request, localeResolver)
                }
            }
        }catch(Throwable t){
            log.warn("Not language discover due to exception. ${webRequest.baseUrl} ${webRequest.getParams()}. [Excp: ${t.getLocalizedMessage()}")
            userLanguage = AvailableLanguage.en_EN
        }
        setCountrySession(request, userLanguage.locale.language)
        localeResolver?.setLocale request, response, userLanguage.locale
        return true
    }

    private AvailableLanguage getLanguageFromDomain(HttpServletRequest request, LocaleResolver localeResolver ){
        return AvailableLanguage.valueOf(CustomDomainResolver.domainRSDTO.language.toString())
    }

    // GET LANGUAGE FROM REQUEST (BROWSER)
    private AvailableLanguage getLanguageFromRequest(HttpServletRequest request, LocaleResolver localeResolver ){
        Locale local = localeResolver.resolveLocale(request)
        AvailableLanguage userLanguage
        if (SPANISH_LANGS.contains(local.language)){
            userLanguage = AvailableLanguage.es_ES
        }else{
            userLanguage = AvailableLanguage.fromLocaleParam(local.getLanguage())?:AvailableLanguage.en_EN
        }
        return userLanguage
    }
//    private String recoverLangFromDomain (HttpServletRequest request){
//        String domain = request.getServerName();
//        def domainSplitter= /([^\.]*).*\.kuorum\.org/
//        def matcher = ( domain =~ domainSplitter )
//        matcher.size()>0?matcher[0][1]:null
//    }

    private void setCountrySession(HttpServletRequest request, String lang){
        String countryCode = ""
        //FAST CHAPU
        switch (lang){
            case "es": countryCode="EU-ES"; break
            case "en": countryCode="EU-GB"; break
            case "it": countryCode="EU-IT"; break
            case "de": countryCode="EU-DE"; break
            case "de": countryCode="EU-LT"; break
            default: countryCode=""
        }
        request.session.setAttribute(WebConstants.COUNTRY_CODE_SESSION, countryCode)
    }

    void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = (CookieLocaleResolver)localeResolver
    }

    void setParamName(String paramName) {
        this.paramName = paramName
    }
}
