<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.vision"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerVision']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.vision"/></h1>
    <h4><g:message code="footer.menu.footerVision.about"/></h4>
    <p><g:message code="footer.menu.footerVision.about.description1"/></p>
    <p><g:message code="footer.menu.footerVision.about.description2"/></p>
    <h4><g:message code="footer.menu.footerVision.vision"/></h4>
    <p><g:message code="footer.menu.footerVision.vision.description"/></p>
    <ul>
        <li><g:message code="footer.menu.footerVision.vision.description.outcome1"/></li>
        <li><g:message code="footer.menu.footerVision.vision.description.outcome2"/></li>
        <li><g:message code="footer.menu.footerVision.vision.description.outcome3"/></li>
    </ul>
    <h4><g:message code="footer.menu.footerVision.impact"/></h4>
    <p><g:message code="footer.menu.footerVision.impact.description1"/></p>
    <p><g:message code="footer.menu.footerVision.impact.description2"/></p>
    <img src="${resource(dir: 'images', file: 'info2.png')}" alt="foto-multitud" itemprop="image">
</content>
