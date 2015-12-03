<!DOCTYPE html>
<%@ page import="springSecurity.KuorumRegisterCommand; grails.plugin.springsecurity.ui.RegisterCommand" contentType="text/html;charset=UTF-8" %>
<html class="no-js" lang="es">
<head>
    <meta charset="UTF8-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><g:layoutTitle default="${g.message(code:"layout.head.title.default")}"/></title>
    <meta property="og:site_name" content="${g.message(code:"kuorum.name")}"/>
    <meta name="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta name="Keywords" content="${g.message(code:"layout.head.meta.keywords")}">
    <meta name="dcterms.rightsHolder" content="Kuorum.org">
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
    <link rel="shortcut icon" href="${resource(dir: 'images/icons', file: 'favicon.ico')}">

    <r:require modules="vimeo" />
    <g:layoutHead/>
    <g:javascript library="jquery" plugin="jquery"/>
    <g:javascript library="application"/>
    <r:layoutResources />
    %{--<g:render template="/layouts/googleAnalyticsRemarketing"/>--}%
    %{--<g:render template="/layouts/facebookRemarketing"/>--}%
    %{--<g:render template="/layouts/twitterRemarketing"/>--}%
    <meta property="twitter:account_id" content="4503599627910348" />
</head>

<body itemscope itemtype="http://schema.org/WebPage">

    <g:render template="/layouts/googleTagManager"/>


<div class ="container-fluid">
    <a href="#main" accesskey="S" class="sr-only first"><g:message code="layout.mainContent.skipMenu"/></a>
    <g:layoutBody/>
</div>
%{--<r:require module="application"/>--}%
<g:render template="/layouts/jsAjaxUrls"/>
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
    var messageError = '${flash.error}'; // For resend this messages
        $(function(){
            display.warn(messageError)
        });
    </g:if>
    <sec:ifAnyGranted roles="ROLE_INCOMPLETE_USER">
        <g:if test="${actionName!='verifyRegistration' && controllerName!='funnel' && controllerName!='register'}">
                $(function(){
                    display.warn("<userUtil:showMailConfirm /> ${pageProperty(name:'page.hiddeMailConfirmMessage')}");
                });
        </g:if>
    </sec:ifAnyGranted>

</script>


<!-- Modal -->
<aside class="modal fade" id="modalVictory" tabindex="-1" role="dialog" aria-labelledby="Victoria" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header clearfix">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <div class="col" id="modalVictoryUser">
                    <img class="user-img big" alt="nombre" src="">
                    <h1>${message(code:'modalVictory.title')}</h1>
                    <h2 id="myModalLabel">${message(code:'modalVictory.label')}</h2>
                </div>
                <div class="col" id="modalVictoryDefender">
                    <span class="fdoDiagonal"></span>
                    <div class="user">
                        <img class="user-img big" alt="nombre" src="">
                        <span class="name" itemprop="name">Nombre usuario</span>
                        <span class="what">se ha comprometido a</span>
                        <span class="action">llevarla al congreso</span>
                    </div>
                </div>
            </div><!-- /.modal-header -->
            <div class="modal-body">
                <p>Hace unos días que gracias a ti, a tu propuesta y a los <strong>876 apoyos</strong> que <strong>Fulanito de Tal</strong> se comprometió contigo y con 876 personas a llevar tu propuesta al congreso.</p>
                <p>Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor <a href="#">#leyaborto</a></p>
            </div>
            <div class="modal-footer">
                <p><g:message code="modalVictory.argument"/> </p>
                <a href="#" class="btn btn-grey btn-lg modalVictoryAction" data-notificationId="XX" data-victoryOk="true"><g:message code="modalVictory.yes" encodeAs="raw"/> </a>
                <a href="#" class="btn btn-grey btn-lg modalVictoryAction" data-notificationId="XX" data-victoryOk="false"><g:message code="modalVictory.no" encodeAs="raw"/> </a>
                <a class="cancel modalVictoryClose" href="#"><g:message code="modalVictory.notYet" encodeAs="raw"/></a>
            </div>
        </div>
    </div>
</aside>

<!-- Modal apadrina-propuesta -->
<div class="modal fade" id="apadrina-propuesta" tabindex="-1" role="dialog" aria-labelledby="apadrinarPropuesta" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="modal-title" id="apadrinarPropuesta">Apadrinar esta propuesta</h4>
            </div>
            <div class="modal-body clearfix">
                <p>Al apadrinar esta propuesta te estas comprometiendo a defenderla y, si es posible, llevarla a cabo. Si el autor de la propuesta considera que has cumplido tu promesa obtendrás una victoria.</p>
                <formUtil:validateForm command="kuorum.web.commands.post.PostDefendCommand" form="apadrinar"/>
                <g:form mapping="postAddDefender" name="apadrinar">
                    <input type="hidden" name="postId" value=""/>
                    <div class="form-group">
                        <label for="explica-apadrinar" class="sr-only">Añade explicación:</label>
                        <textarea id="explica-apadrinar" name="text" placeholder="Explica cómo piensas llevarla a cabo" rows="5" class="form-control"></textarea>
                    </div>
                    <div class="form-group btns clearfix">
                        <input type="submit" class="btn btn-grey pull-right" value="Apadrinar">
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</div>

<!-- Modal registro/login -->
<div class="modal fade" id="registro" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>
            </div>
            <div class="modal-body">
                <!-- Formulario de Entrar -->
                <g:include controller="login" action="loginForm"/>
                <!-- Formulario de Registro -->
                <g:render template="/layouts/registerForm" model="[registerCommand:new KuorumRegisterCommand(), formId:'sign-modal']"/>
            </div>
        </div>
    </div>
</div>

%{--<!-- MODAL CONTACT -->--}%
%{--<div class="modal fade in" id="contact-modal" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="false" style="display: block;">--}%
    %{--<div class="modal-dialog ">--}%
        %{--<div class="modal-content">--}%
            %{--<div class="modal-header">--}%
                %{--<button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>--}%
                %{--<h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>--}%
                %{--<h5>Message to Barack Obama</h5>--}%
            %{--</div>--}%
            %{--<div class="modal-body">--}%
                %{--<!-- email subscription form -->--}%
                %{--<form action="https://kuorum.org/j_spring_security_check" method="post" name="login-header" id="login-modal" role="form" class="login">--}%
                    %{--<div class="form-group">--}%
                        %{--<label for="message-text" class="control-label">Which cause is this message related to?</label>--}%
                        %{--<select class="form-control">--}%
                            %{--<option>#Economía</option>--}%
                            %{--<option>#Innovación</option>--}%
                            %{--<option>#Transparencia</option>--}%
                            %{--<option>#EstadodelBienestar</option>--}%
                            %{--<option>#Cooperación</option>--}%
                            %{--<option>#Igualdad</option>--}%
                        %{--</select>--}%
                    %{--</div>--}%
                    %{--<div class="form-group">--}%
                        %{--<label for="message-text" class="control-label">Message:</label>--}%
                        %{--<textarea class="form-control" id="message-text" placeholder="Type your message here..."></textarea>--}%
                    %{--</div>--}%
                    %{--<div class="form-group">--}%
                        %{--<label for="recipient-name" class="control-label">Leave us your email to get updates on this message!</label>--}%
                        %{--<input type="text" name="name" class="form-control counted " id="name" required="" placeholder="Tell us your name" value="" aria-required="true">--}%
                    %{--</div>--}%
                    %{--<div class="form-group">--}%
                        %{--<input type="email" name="email" class="form-control center-block" id="email" required="" placeholder="Introduce your email" value="" aria-required="true">--}%
                    %{--</div>--}%

                    %{--<div class="form-group">--}%
                        %{--<input type="submit" class="btn" value="Send Message!">--}%
                    %{--</div>--}%
                    %{--<div class="form-group">--}%
                        %{--You are accepting the <a href="https://kuorum.org/kuorum/politica-privacidad" target="_blank">service conditions</a>--}%
                    %{--</div>--}%
                %{--</form>--}%
                %{--<script type="text/javascript">--}%
                    %{--$(function (){--}%
                        %{--$("#sign-modal").validate({--}%
                            %{--errorClass:'error',--}%
                            %{--errorPlacement: function(error, element) {--}%
                                %{--if(element.attr('id') == 'deadline')--}%
                                    %{--error.appendTo(element.parent("div").parent("div"));--}%
                                %{--else if(element.attr('id') == 'JUSTICE')--}%
                                    %{--error.appendTo(element.parent("div").parent("div").parent("div").parent("div"));--}%
                                %{--else--}%
                                    %{--error.insertAfter(element);--}%
                            %{--},--}%
                            %{--errorElement:'span',--}%
                            %{--rules: {'name':{required: true ,maxlength: 17},'email':{required: true ,email: true}} ,  messages: {'name':{required: 'We need a name to address you',maxlength: 'The username must have a maximum of 17 characters'},'email':{required: 'We need an email to communicate with you',email: 'Wrong email format'}}--}%
                        %{--});--}%
                    %{--});--}%
                %{--</script>--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</div>--}%
<!-- fin modal -->
%{--<script type="text/javascript" src="http://jira.kuorum.org/s/d41d8cd98f00b204e9800998ecf8427e/es_ES-ce14fm-1988229788/6264/13/1.4.7/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?collectorId=132ad9a9"></script>--}%

</body>
</html>
