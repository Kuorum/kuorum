<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.developers"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerDevelopers']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.editors"/></h1>
    <p>
        <g:message code="footer.menu.footerWhatIsKuorum.forEditors.description1"/>
    </p>
    <p>
        <g:message code="footer.menu.footerWhatIsKuorum.forEditors.description2"/>
    </p>
    <p><img src="${resource(dir: 'images', file: 'ipdb-kuorum.png')}" alt="we-build-transparency" itemprop="image"></p>
</content>
