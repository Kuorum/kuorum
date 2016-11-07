<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>


<g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}" />
<g:each in="${kuorum.core.model.AvailableLanguage.values()}" var="lang">
    <link rel="alternate"
          href="https://${lang.locale.language}.kuorum.org${request.forwardURI}${request.queryString? "?" +request.queryString:''}"
          hreflang="${lang.locale.language}" />
</g:each>

<link
        rel="alternate"
        href="https://kuorum.org${request.forwardURI}${request.queryString? "?" +request.queryString:''}"
        hreflang="x-default" />