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
    <meta name="dcterms.rightsHolder" content="${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}">
    %{--<meta name="dcterms.dateCopyrighted" content="2013">--}%

    <meta name="robots" content="all">
    <domain:favicon domainResourcesPath="${_domainResourcesPath}"/>

    %{--This line wirtes the goole verification code. It is a meta tag with its code--}%
    ${raw(kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.googleSiteVerification)}

    <g:set var="${kuorum.web.constants.WebConstants.WEB_PARAM_LANG}" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" />
    <r:require modules="lang_${lang}, kuorumCookies, application" />
    <r:require modules="widget, widgetResizer" />
%{--    <sec:ifNotLoggedIn>--}%
        <r:require modules="loginApi" />
%{--    </sec:ifNotLoggedIn>--}%
    <g:layoutHead/>

    <r:layoutResources />
    <g:render template="/layouts/internationalization/otherLangsRef"/>

    <g:if test="${tour && _VisibleFieldForUser && !_isSurveyPlatform}">
        <r:require module="tour"/>
    </g:if>
    <g:elseif test="${tour && _VisibleFieldForUser && _isSurveyPlatform}">
        <r:require module="tour_tutorial"/>
    </g:elseif>

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
    <nav:ifActiveMapping mappingNames="customProcessRegisterStep2, customProcessRegisterStep3, registerSubscriptionStep1, registerSubscriptionStep1Save, registerSubscriptionStep3" equals="false">
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

%{--<sec:ifNotLoggedIn>--}%
    <g:render template="/layouts/modals/modalLogin"/>
%{--</sec:ifNotLoggedIn>--}%
<g:render template="/layouts/modals/modalDomainValidation"/>

<asset:deferredScripts/>
<r:layoutResources />

</body>
</html>
