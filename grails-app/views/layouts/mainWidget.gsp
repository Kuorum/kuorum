<!DOCTYPE html>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.springframework.context.i18n.LocaleContextHolder; springSecurity.KuorumRegisterCommand; grails.plugin.springsecurity.ui.RegisterCommand" contentType="text/html;charset=UTF-8" %>
<html class="no-js" lang="es">
<head>
    <meta charset="UTF8-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><g:layoutTitle default="${g.message(code:"layout.head.title.default", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}"/></title>
    <g:layoutHead/>
    <meta name="description" content="${g.message(code:"layout.head.meta.description", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
    <meta name="Keywords" content="${g.message(code:"layout.head.meta.keywords", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
    <meta name="dcterms.rightsHolder" content="${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}">
    %{--<meta name="dcterms.dateCopyrighted" content="2013">--}%

    <meta name="robots" content="all">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/font-awesome/css', file: 'font-awesome.min.css')}">
    %{--<link rel="stylesheet" href="${resource(dir: 'fonts/font-awesome', file: 'styles.css')}">--}%
    <link rel="stylesheet" href="${resource(dir: 'fonts/icomoon', file: 'styles.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/icomoon2', file: 'styles.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/icomoon3', file: 'styles.css')}">
    %{--<link rel="stylesheet" href="css/bootstrap-tour.min.css">--}%
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'datepicker3.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap-tour.min.css')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'custom.css')}">
    <!-- Estilos sólo para IE -->
    <!--[if IE]><link rel="stylesheet" href="${resource(dir: 'css', file: 'style_ie.css')}" type="text/css" media="screen"><![endif]-->
    <!--[if IE 9]><link rel="stylesheet" href="${resource(dir: 'css', file: 'style_ie9.css')}" type="text/css" media="screen"><![endif]-->
    <!--[if IE 8]><link rel="stylesheet" href="${resource(dir: 'css', file: 'style_ie8.css')}" type="text/css" media="screen"><![endif]-->
    <!-- Soporte HTML5 y pseudo-clases CSS3 para IE9 e inferior -->
    <!--[if (lt IE 9) & (!IEMobile)]>
        <script src="${resource(dir: 'js', file: 'respond.min.js')}"></script>
        <script src="${resource(dir: 'js', file: 'selectivizr.js')}"></script>
    <![endif]-->


    <!-- JavaScript -->
    <script src="${resource(dir: 'js', file: 'modernizr.js')}"></script>

    <!-- For iPhone 5 and iPod touch -->
    <link rel="apple-touch-icon" sizes="120x120" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-120x120.png')}">

    <!-- For iPhone 4 -->
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-114x114.png')}">

    <!-- For the new iPad and iPad mini -->
    <link rel="apple-touch-icon" sizes="152x152" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-152x152.png')}">

    <!-- For iPad 2 -->
    <link rel="apple-touch-icon" sizes="144x144" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-114x114.png')}">

    <!-- For iPad 1-->
    <link rel="apple-touch-icon" sizes="72x72" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-72x72.png')}">

    <!-- For iPhone 3G, iPod Touch and Android -->
    <link rel="apple-touch-icon" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-76x76.png')}">

    <!-- For Nokia -->
    <link rel="shortcut icon" href="${resource(dir: 'images/icons', file: 'apple-touch-icon-76x76.png')}">

    <!-- For everything else -->
    <link rel="shortcut icon" href="${resource(dir: 'images/icons', file: 'faviconKuorum.ico')}">

    <g:set var="lang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" />
    <r:require modules="lang_${lang}, widget" />
    <g:layoutHead/>
    <g:javascript library="application"/>
    <r:layoutResources />
    <meta property="twitter:account_id" content="4503599627910348" />
</head>

<body itemscope itemtype="http://schema.org/WebPage" class="widget">
    <g:render template="/layouts/googleTagManager"/>
    <g:layoutBody/>
    <g:render template="/layouts/jsAjaxUrls"/>
    <r:layoutResources />


</body>
</html>
