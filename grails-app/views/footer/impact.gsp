<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.impact"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerImpact']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.impact"/></h1>
    <p><g:message code="footer.menu.footerVision.impact.description1"/></p>
    <p><g:message code="footer.menu.footerVision.impact.description2"/></p>
    <p><g:message code="footer.menu.footerVision.impact.description3"/></p>
    %{--<p><img src="${resource(dir: 'images', file: 'info3.png')}" alt="foto-activistas" itemprop="image"></p>--}%
</content>
