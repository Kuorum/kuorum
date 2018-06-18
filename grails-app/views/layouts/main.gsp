<!DOCTYPE html>
<%@ page import="kuorum.core.customDomain.CustomDomainResolver; org.springframework.web.servlet.support.RequestContextUtils; org.springframework.context.i18n.LocaleContextHolder;" contentType="text/html;charset=UTF-8" %>
<g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}" />
<html class="no-js" lang="${currentLang.language}" xml:lang="${currentLang.language}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF8-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><g:layoutTitle default="${g.message(code:"layout.head.title.default", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}"/></title>
    <meta name="Keywords" content="${g.message(code:"layout.head.meta.keywords", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
    <meta name="dcterms.rightsHolder" content="${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}g">
    %{--<meta name="dcterms.dateCopyrighted" content="2013">--}%

    <meta name="robots" content="all">


    <link rel="apple-touch-icon" sizes="180x180" href="${_domainResourcesPath}/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="${_domainResourcesPath}/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="${_domainResourcesPath}/favicon/favicon-16x16.png">
    <link rel="manifest" href="${_domainResourcesPath}/favicon/site.webmanifest">
    <link rel="mask-icon" href="${_domainResourcesPath}/favicon/safari-pinned-tab.svg" color="#ff9431">
    <link rel="shortcut icon" href="${_domainResourcesPath}/favicon/favicon.ico">
    <meta name="msapplication-TileColor" content="#20a2ea">
    <meta name="msapplication-config" content="${_domainResourcesPath}/favicon/browserconfig.xml">
    <meta name="theme-color" content="#ffffff">


    <r:require modules="vimeo" />
    <g:set var="lang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" />
    <r:require modules="lang_${lang}, kuorumCookies, application, loginApi" />
    <g:layoutHead/>

    <r:layoutResources />
    <g:render template="/layouts/internationalization/otherLangsRef"/>

    <domain:customCss/>
    %{--<link rel="stylesheet" href="http://localhost/customDomainCss.css" type="text/css"/>--}%
</head>

<g:if test="${!schemaData?.schema}">
    <g:set var="schemaData" value="${[schema:'http://schema.org/WebSite', name:g.layoutTitle(default:g.message(code:'layout.head.title.default', args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]))]}" scope="request"/>
</g:if>

<g:set var="dynamicBodyCss" value=""/>
<sec:ifNotLoggedIn>
    <g:set var="dynamicBodyCss" value="noLogged"/>
</sec:ifNotLoggedIn>
<body itemscope itemtype="${schemaData.schema}" class="${pageProperty(name:"page.bodyCss")} ${dynamicBodyCss}">
<meta itemprop="url" content="${nav.canonical([onlyLink:true])}"/>



    <span class="hidden" itemprop="name">${schemaData.name}</span>
    <g:render template="/layouts/googleTagManager"/>

<div class ="container-fluid">
    <a href="#main" accesskey="S" class="sr-only first"><g:message code="layout.mainContent.skipMenu"/></a>
    <g:layoutBody/>
</div>

<g:render template="/layouts/jsAjaxUrls" model="[currentLang:currentLang]"/>

<g:if test="${flash.message}">
    <r:script>
    var messageDisplay = '${flash.message}'; // For resend this messages
        $(function(){
            display.success(messageDisplay)
        });
    </r:script>
</g:if>
<g:if test="${flash.error}">
    <r:script>
        var messageError = '${raw(flash.error)}'; // For resend this messages
            $(function(){
                display.warn(messageError)
            });
    </r:script>
</g:if>
<sec:ifAnyGranted roles="ROLE_INCOMPLETE_USER">
    <nav:ifActiveMapping mappingNames="customProcessRegisterStep2, customProcessRegisterStep3, registerSubscriptionStep1, registerSubscriptionStep1Save, registerSubscriptionStep3, tour_dashboard" equals="false">
        <r:script>
            function notMailConfirmedWarn(){
                display.warn("<userUtil:showMailConfirm />");
            }
            $(function(){
                notMailConfirmedWarn();
            });
        </r:script>
    </nav:ifActiveMapping>
</sec:ifAnyGranted>

<sec:ifNotLoggedIn>
    <g:render template="/layouts/modalLogin"/>
</sec:ifNotLoggedIn>
%{--<g:render template="/layouts/modalDomainValidation"/>--}%

<asset:deferredScripts/>
<r:layoutResources />

</body>
</html>
