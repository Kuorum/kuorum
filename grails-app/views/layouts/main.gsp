<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html class="no-js" lang="es">
<head>
    <meta charset="UTF8-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><g:layoutTitle default="${g.message(code:"layout.head.title.default")}"/></title>
    <meta name="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta name="Keywords" content="${g.message(code:"layout.head.meta.keywords")}">
    <meta name="dcterms.rightsHolder" content="Kuorum.org">
    %{--<meta name="dcterms.dateCopyrighted" content="2013">--}%

    <meta name="robots" content="all">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/font-awesome', file: 'styles.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/icomoon', file: 'styles.css')}">
    %{--<link rel="stylesheet" href="css/bootstrap-tour.min.css">--}%
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

    <g:layoutHead/>
    <g:javascript library="jquery" plugin="jquery"/>
    <g:javascript library="application"/>
    <r:layoutResources />
    <g:render template="/layouts/googleAnalyticsRemarketing"/>
    <g:render template="/layouts/facebookRemarketing"/>
    <g:render template="/layouts/twitterRemarketing"/>
    <meta property="twitter:account_id" content="4503599627910348" />
</head>

<body itemscope itemtype="http://schema.org/WebPage">

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
        $(function(){
            display.success('${flash.message}')
        })
    </g:if>
    <g:if test="${flash.error}">
    $(function(){
        display.warn('${flash.error}')
    })
    </g:if>
</script>

<!-- Modal -->
<aside class="modal fade" id="modalVictory" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
                    <div itemtype="http://schema.org/Person" itemscope class="user">
                        <img itemprop="image" class="user-img big" alt="nombre" src="">
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
                <a href="#" class="btn btn-grey btn-lg"><g:message code="modalVictory.yes" encodeAs="raw"/> </a>
                <a href="#" class="btn btn-grey btn-lg"><g:message code="modalVictory.no" encodeAs="raw"/> </a>
                <a class="cancel modalVictoryClose" href="#"><g:message code="modalVictory.notYet" encodeAs="raw"/></a>
            </div>
        </div>
    </div>
</aside>

<aside class="modal fade" id="modalSponsor" tabindex="-1" role="dialog" aria-labelledby="sponsorLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header clearfix">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/> </span></button>
                <div class="col" id="modalDefenderPolitician">
                    <img class="user-img big" alt="nombre" src="">
                    <h1>Nombre del político</h1>
                    <h2>Tú puedes darles voz</h2>
                    <h3 id="sponsorLabel">Apadrina esta historia</h3>
                </div>
                <div class="col" id="modalDefenderOwner">
                    <span class="fdoDiagonal"></span>
                    <div itemtype="http://schema.org/Person" itemscope class="user">
                        <img itemprop="image" class="user-img big" alt="nombre" src="">
                        <span class="name" itemprop="name">Nombreeeee usuuuario</span>
                        <span class="what">escribió esta historia</span>
                        <span class="action">y <span>1245</span> personas la impulsaron</span>
                    </div>
                </div>
            </div><!-- /.modal-header -->
            <div class="modal-body">
                <p><strong>Fulanito de tal</strong> escribió esta historia y <strong>876 personas</strong> la impulsaron. El compromiso con los electores es tu mayor activo político, elige una de las siguientes opciones para ayudar a tus representados.</p>
                <div>
                    <a href="#" class="btn btn-grey btn-lg">Llévala <br>a tu institución</a>
                    <p>Te comprometes a ...hora te toca a ti decir si ese compromiso se ha llevado a fin o no. Si lo tienes claro da la victoria a Fulanito porque nos ha escuchado y se lo merece en caso contrario puedes esperar un tiempo mas o rechazar la victoria. Porque tú sabes si se hizo realidad.</p>
                </div>
                <div>
                    <a href="#" class="btn btn-grey btn-lg">Invita al autor<br> a comparecer</a>
                    <p>Te comprometes a ...hora te toca a ti decir si ese compromiso se ha llevado a fin o no. Si lo tienes claro da la victoria a Fulanito porque nos ha escuchado y se lo merece en caso contrario puedes esperar un tiempo mas o rechazar la victoria. Porque tú sabes si se hizo realidad..</p>
                </div>
                <div>
                    <a href="#" class="btn btn-grey btn-lg">Cita al autor<br> para una audiencia</a>
                    <p>Te comprometes a ...hora te toca a ti decir si ese compromiso se ha llevado a fin o no. Si lo tienes claro da la victoria a Fulanito porque nos ha escuchado y se lo merece en caso contrario puedes esperar un tiempo mas o rechazar la victoria. Porque tú sabes si se hizo realidad.</p>
                </div>
            </div>
        </div>
    </div>
</aside>

%{--<script type="text/javascript" src="http://jira.kuorum.org/s/d41d8cd98f00b204e9800998ecf8427e/es_ES-ce14fm-1988229788/6264/13/1.4.7/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?collectorId=132ad9a9"></script>--}%

</body>
</html>
