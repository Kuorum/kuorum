<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>

<nav:generateAlternateLangLink />

<nav:canonical/>

%{--<link   rel="alternate"
        href="https://kuorum.org${request.forwardURI}${request.queryString ? "?" +request.queryString:''}"
        hreflang="x-default" />--}%