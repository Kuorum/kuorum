<!DOCTYPE html>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.springframework.context.i18n.LocaleContextHolder;" contentType="text/html;charset=UTF-8" %>
<g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}" />
<html class="no-js" lang="${currentLang.language}" xml:lang="${currentLang.language}" xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF8-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><g:layoutTitle default="${g.message(code:"layout.head.title.default")}"/></title>
    <meta name="Keywords" content="${g.message(code:"layout.head.meta.keywords")}">
    <meta name="dcterms.rightsHolder" content="Kuorum.org">
    %{--<meta name="dcterms.dateCopyrighted" content="2013">--}%

    <meta name="robots" content="all">


    <link rel="apple-touch-icon" sizes="180x180" href="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/favicon-16x16.png">
    <link rel="manifest" href="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/site.webmanifest">
    <link rel="mask-icon" href="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/safari-pinned-tab.svg" color="#ff9431">
    <link rel="shortcut icon" href="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/favicon.ico">
    <meta name="msapplication-TileColor" content="#20a2ea">
    <meta name="msapplication-config" content="https://test-kuorumorg.s3.amazonaws.com/domains/${_domain}/favicon/browserconfig.xml">
    <meta name="theme-color" content="#ffffff">


    <r:require modules="vimeo" />
    <g:set var="lang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" />
    <r:require modules="lang_${lang}, kuorumCookies, application" />
    <g:layoutHead/>

    <r:layoutResources />
    <meta property="twitter:account_id" content="4503599627910348" />
    <g:render template="/layouts/internationalization/otherLangsRef"/>

    <domain:customCss/>
    %{--<link rel="stylesheet" href="http://localhost/customDomainCss.css" type="text/css"/>--}%
</head>

<g:if test="${!schemaData?.schema}">
    <g:set var="schemaData" value="${[schema:'http://schema.org/WebSite', name:g.layoutTitle(default:g.message(code:'layout.head.title.default'))]}" scope="request"/>
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

<asset:deferredScripts/>
<r:layoutResources />

</body>
</html>
