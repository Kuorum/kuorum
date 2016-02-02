<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/></title>
    <meta name="layout" content="landingLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home-video.png')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <!-- for Facebook -->
    <meta property="og:title" content="${g.message(code:"kuorum.name")}" />
    <meta property="og:type" content="article" />
    <meta property="og:image" content="${resource(dir: 'images', file: 'home-video.png')}" />
    <meta property="og:image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <meta property="og:url" content="Kuorum.org" />
    <meta property="og:description" content="${g.message(code:"layout.head.meta.description")}" />
    <!-- for Google -->
    <meta name="description" content="${g.message(code:"layout.head.meta.description")}" />
    <meta name="keywords" content="${g.message(code:"layout.head.meta.keywords")}" />
    <meta name="application-name" content="${g.message(code:"kuorum.name")}" />
    <!-- for Twitter -->
    <meta name="twitter:card" content="summary" />
    <meta name="twitter:title" content="${g.message(code:"kuorum.name")}" />
    <meta name="twitter:description" content="${g.message(code:"layout.head.meta.description")}" />
    <meta name="twitter:image" content="${resource(dir: 'images', file: 'home-video.png')}" />
    <meta name="twitter:image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    %{--<parameter name="showDefaultPreFooter" value="true"/>--}%
</head>

<content tag="videoAndRegister">
    <g:render template="landingPageModules/videoAndRegister" model="[command:command]"/>
</content>

<content tag="logos">
    <g:render template="landingPageModules/landingLogos"/>
</content>

<content tag="special">
    <g:render template="landingPageModules/landingKuorumFeautres"/>
</content>

<content tag="mainContent">
    <g:render template="landingPageModules/landingTestimonies"/>
</content>

<content tag="pressKit">
    <g:render template="landingPageModules/pressKit"/>
</content>


