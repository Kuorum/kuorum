<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/></title>
    <meta name="layout" content="homeLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    %{--<parameter name="showDefaultPreFooter" value="true"/>--}%
</head>
<content tag="mainContent">
    <g:render template="landingPageModules/videoAndRegister" model="[command:command]"/>
</content>


<content tag="subHome">
    <g:include controller="modules" action="recommendedProjects"/>
    <g:render template="landingPageModules/relevantPoliticians"/>
    <g:render template="landingPageModules/relevantPosts"/>
    <g:render template="landingPageModules/pressAndNotices"/>
</content>

