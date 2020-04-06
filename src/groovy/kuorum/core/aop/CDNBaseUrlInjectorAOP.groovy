package kuorum.core.aop


import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Value

@Aspect
class CDNBaseUrlInjectorAOP {

    private static def VALID_URL_MAPPING = /^landing.*|^register.*|^home$|^footer.*|^reset.*|^login.*|^blog.*|^searcher.*|^lang.*/


    @Value('${grails.resources.mappers.baseurl.default}')
    String cdnBaseUrl

    @Value('${grails.resources.mappers.amazoncdn.enabled}')
    String cdnEnabled

    @Around("execution(public * org.codehaus.groovy.grails.web.mapping.LinkGenerator+.*(..)) && args(params)")
    public Object processLink(final ProceedingJoinPoint pjp,
                                 final Map params) throws Throwable {

        replaceAbsoluteParamsToCDN(params)
        return pjp.proceed(params);

    }

    @Around("execution(public * org.codehaus.groovy.grails.web.mapping.LinkGenerator+.*(..)) && args(params,encoding)")
    public Object processLink(final ProceedingJoinPoint pjp,
                              final Map params,
                              final String encoding) throws Throwable {
        replaceAbsoluteParamsToCDN(params)
        return pjp.proceed(params, encoding);

    }

    private void replaceAbsoluteParamsToCDN(Map params){
//        if (params.cdn==true && cdnEnabled){
        if (params.dir && cdnEnabled){ // DIR indicates that it is a static resource
            params.absolute=true
            params.base=cdnBaseUrl
            params.contextPath = ""
        }
    }
}
