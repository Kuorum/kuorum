<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>


<g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}" />

<nav:generateAlternateLangLink />

%{--<link   rel="alternate"
        href="https://kuorum.org${request.forwardURI}${request.queryString ? "?" +request.queryString:''}"
        hreflang="x-default" />--}%