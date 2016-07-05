<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.vision"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.vision'),
                      kuorumDescription:g.message(code:'page.title.footer.vision.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
    <link rel="canonical" href="https://${request.serverName}${g.link(mapping: 'footerVision')}" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerVision']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.mision"/></h1>
    <p><g:message code="footer.menu.footerVision.vision.description"/></p>
    <ul>
        <li><g:message code="footer.menu.footerVision.vision.description.outcome1"/></li>
        <li><g:message code="footer.menu.footerVision.vision.description.outcome2"/></li>
        <li><g:message code="footer.menu.footerVision.vision.description.outcome3"/></li>
    </ul>
    %{--<p><img src="${resource(dir: 'images', file: 'info2.png')}" alt="foto-multitud" itemprop="image"></p>--}%
</content>
