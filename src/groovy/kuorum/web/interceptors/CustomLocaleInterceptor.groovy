package kuorum.web.interceptors

import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.web.constants.WebConstants
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.LocaleEditor
import org.springframework.context.i18n.LocaleContextHolder
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
        def localeParam = recoverLangFromDomain(request)?:params?.get(paramName)
        AvailableLanguage userLanguage
        request.getServerName()
        LocaleResolver localeResolver = org.springframework.web.servlet.support.RequestContextUtils.getLocaleResolver(request)
        try{
            if (springSecurityService.isLoggedIn()){
                KuorumUser user = KuorumUser.get(springSecurityService.principal.id)
                userLanguage = user.language
            }else{
                userLanguage = AvailableLanguage.fromLocaleParam(localeParam);
                if (!userLanguage){
                    Locale local = localeResolver.resolveLocale(request)
                    if (SPANISH_LANGS.contains(local.language)){
                        userLanguage = AvailableLanguage.es_ES
                    }else{
                        userLanguage = AvailableLanguage.fromLocaleParam(local.getLanguage())?:AvailableLanguage.en_EN
                    }
                }
            }
        }catch(Throwable t){
            log.warn("Not language discover due to exception. ${webRequest.baseUrl} ${webRequest.getParams()}", t)
            userLanguage = AvailableLanguage.en_EN
        }
        setCountrySession(request, userLanguage.locale.language)
        localeResolver?.setLocale request, response, userLanguage.locale
        return true;
    }

    private String recoverLangFromDomain (HttpServletRequest request){
        String domain = request.getServerName();
        def domainSplitter= /([^\.]*).*\.kuorum\.org/
        def matcher = ( domain =~ domainSplitter )
        matcher.size()>0?matcher[0][1]:null
    }

    private void setCountrySession(HttpServletRequest request, String lang){
        String countryCode = "";
        //FAST CHAPU
        switch (lang){
            case "es": countryCode="EU-ES"; break;
            case "en": countryCode="EU-GB"; break;
            case "it": countryCode="EU-IT"; break;
            case "de": countryCode="EU-DE"; break;
            case "de": countryCode="EU-LT"; break;
            default: countryCode=""
        }
        request.session.setAttribute(WebConstants.COUNTRY_CODE_SESSION, countryCode)
    }

    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = (CookieLocaleResolver)localeResolver
    }

    public void setParamName(String paramName) {
        this.paramName = paramName
    }
}
