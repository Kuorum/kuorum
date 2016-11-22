package kuorum.core.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

@Aspect
class LanguageInjectorAOP {

    @Around("execution(public * org.codehaus.groovy.grails.web.mapping.LinkGenerator+.*(..)) && args(params)")
    public Object processLink(final ProceedingJoinPoint pjp,
                                 final Map params) throws Throwable {

        addLang(params)
        return pjp.proceed(params);

    }

    @Around("execution(public * org.codehaus.groovy.grails.web.mapping.LinkGenerator+.*(..)) && args(params,encoding)")
    public Object processLink(final ProceedingJoinPoint pjp,
                              final Map params,
                              final String encoding) throws Throwable {

        addLang(params)
        return pjp.proceed(params, encoding);

    }

    private void addLang(Map params){
        if (params.mapping && !params.params?.lang){
            params.params = params.params?:[:]
            Locale locale = org.springframework.context.i18n.LocaleContextHolder.getLocale()
            params.params.lang = locale.getLanguage()
        }
    }
}
