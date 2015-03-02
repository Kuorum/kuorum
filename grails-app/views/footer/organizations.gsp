<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.organizations"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerOrganizations']"/>
</content>

<content tag="mainContent">
    <div class="box-ppal">
        <h1><g:message code="layout.footer.organizations"/></h1>
        <p>
            <g:message code="footer.menu.footerOrganizations.description1"/>
        </p>
        <p>
            <g:message code="footer.menu.footerOrganizations.description2"/>
        </p>
        <img src="${resource(dir: 'images', file: 'info3.png')}" alt="foto-activistas" itemprop="image">
    </div>
</content>
