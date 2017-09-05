<!DOCTYPE html>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.springframework.context.i18n.LocaleContextHolder; springSecurity.KuorumRegisterCommand; grails.plugin.springsecurity.ui.RegisterCommand" contentType="text/html;charset=UTF-8" %>
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
    <link rel="shortcut icon" href="${resource(dir: 'images/icons', file: 'favicon.ico')}">

    <r:require modules="vimeo" />
    <g:set var="lang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request).language}" />
    <r:require modules="lang_${lang}, kuorumCookies, application" />
    <g:layoutHead/>
    <g:javascript library="jquery" plugin="jquery"/>
    <r:layoutResources />
    <meta property="twitter:account_id" content="4503599627910348" />
    <g:render template="/layouts/internationalization/otherLangsRef"/>
</head>

<g:if test="${!schema}">
    <g:set var="schema" value="http://schema.org/WebSite"/>
</g:if>

<body itemscope itemtype="${schema}" class="${pageProperty(name:"page.bodyCss")}">




    <span class="hidden" itemprop="name"><g:layoutTitle default="${g.message(code:"layout.head.title.default")}"/></span>
    <g:render template="/layouts/googleTagManager"/>

<div class ="container-fluid">
    <a href="#main" accesskey="S" class="sr-only first"><g:message code="layout.mainContent.skipMenu"/></a>
    <g:layoutBody/>
</div>
%{--<r:require module="application"/>--}%
<g:render template="/layouts/jsAjaxUrls" model="[currentLang:currentLang]"/>
<r:layoutResources />
%{--<script src="${resource(dir: '/js', file: 'jquery.slimscroll.min.js')}"></script>--}%
%{--<script src="${resource(dir: '/js', file: 'custom.js')}"></script>--}%
<script>
    <g:if test="${flash.message}">
    var messageDisplay = '${flash.message}'; // For resend this messages
        $(function(){
            display.success(messageDisplay)
        });
    </g:if>
    <g:if test="${flash.error}">
    var messageError = '${raw(flash.error)}'; // For resend this messages
        $(function(){
            display.warn(messageError)
        });
    </g:if>
    <sec:ifAnyGranted roles="ROLE_INCOMPLETE_USER">
        <nav:ifActiveMapping mappingNames="registerStep2, registerStep3, registerSubscriptionStep1, registerSubscriptionStep1Save, registerSubscriptionStep3, tour_dashboard" equals="false">
            function notMailConfirmedWarn(){
                display.warn("<userUtil:showMailConfirm /> ${pageProperty(name:'page.hiddeMailConfirmMessage')}");
            }
            $(function(){
                notMailConfirmedWarn();
            });
        </nav:ifActiveMapping>
    </sec:ifAnyGranted>

</script>

<asset:deferredScripts/>

<sec:ifNotLoggedIn>


    <!-- Modal registro/login -->
    <div class="modal fade modal-register" id="registro" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                    <h4><g:message code="register.title"/></h4>
                    <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>
                </div>
                <div class="modal-body">
                    <!-- Formulario de Entrar -->
                    <g:include controller="login" action="loginForm"/>
                    <!-- Formulario de Registro -->
                    <g:render template="/layouts/registerForm" model="[registerCommand: new KuorumRegisterCommand(), formId:'signup-modal']"/>
                    <g:render template="/register/registerSocial"/>
                </div>
            </div>
        </div>
    </div>

    <div id="fb-root"></div>
    <script>
        window.fbAsyncInit = function() {
            FB.init({
    //            appId: '226641644202506',
                appId: '${_facebookConfig?.key?:''}',
                status: true,
                cookie: true, // enable cookies to allow the server to access the session
                oauth: true, // parse social plugins on this page
                version:'v2.8',
                xfbml: true});

    //        FB.getLoginStatus(function(response) {
    //            console.log("FB Login")
    //            console.log(response)
    //        });
        };
        (function() {
            var e = document.createElement('script'); e.async = true;
            e.src = document.location.protocol +
                    '//connect.facebook.net/es_ES/all.js';
            document.getElementById('fb-root').appendChild(e);
        }());

        function FBLogin(callbackSuccess, callbackError)
        {
            callbackError = callbackError || function(){display.warn("ERROR LOGIN FACEBOOK")};
            callbackSuccess = callbackSuccess || noLoggedCallbacks.reloadPage;
            FB.login(function(response) {
                if (response.authResponse)
                {
                    $.ajax({
                        type:"POST",
                        url:"${g.createLink(mapping: 'registerAjaxFacebook')}",
                        data:response.authResponse,
                        dataType:"json"
                    }).success(function(data){
                        callbackSuccess()
                    }).error(function(jqXHR, textStatus,errorThrown){
                        callbackError()
                    }).done(function(data){
                    });
                } else
                {
//                    callbackError()
                }
            },{scope: 'public_profile,email'});
        }
        $("#registro .socialGo .btn.fb").on("click", function(e){
            e.preventDefault();
            var callbackFunctionName = $('#registro').find("form").attr("callback");
            FBLogin(noLoggedCallbacks[callbackFunctionName]);
        })
    </script>
</sec:ifNotLoggedIn>
</body>
</html>
