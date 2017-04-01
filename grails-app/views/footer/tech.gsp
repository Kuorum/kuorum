<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.whatIsKuorum"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.whatIsKuorum'),
                      kuorumDescription:g.message(code:'page.title.footer.whatIsKuorum.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerTechnology']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.technology"/></h1>
    <p><g:message code="footer.menu.tech.description1"/></p>
    <p><g:message code="footer.menu.tech.description2"/></p>
    %{--<p><img src="${resource(dir: 'images', file: 'screens.png')}" alt="kuorum-screens" itemprop="image"></p>--}%
</content>
