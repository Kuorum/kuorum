<!DOCTYPE html>

<html class="no-js" lang="es">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="HandheldFriendly" content="True">
    <meta name="MobileOptimized" content="320">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title><g:layoutTitle default="${g.message(code:"layout.head.title.default")}"/></title>
    <meta name="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta name="Keywords" content="${g.message(code:"layout.head.meta.keywords")}">
    <meta name="dcterms.rightsHolder" content="Kuorum Social S.L.">
    <meta name="dcterms.dateCopyrighted" content="2013">

    <meta name="robots" content="all">

    <link rel="stylesheet" href="${resource(dir: 'css', file: 'bootstrap.min.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/font-awesome', file: 'styles.css')}">
    <link rel="stylesheet" href="${resource(dir: 'fonts/icomoon', file: 'styles.css')}">
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
                        <img itemprop="image" class="user-img big" alt="nombre" src="images/user.jpg">
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
                <a href="#" class="btn btn-grey btn-lg">SI <small>lo conseguimos</small></a>
                <a href="#" class="btn btn-grey btn-lg">NO <small>lo conseguimos</small></a>
                <a class="cancel" href="#">todavía es pronto <small>para saberlo</small></a>
            </div>
        </div>
    </div>
</aside>


</body>
</html>
