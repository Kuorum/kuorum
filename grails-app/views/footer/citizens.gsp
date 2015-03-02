<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.citizens"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerCitizens']"/>
</content>

<content tag="mainContent">
    <div class="box-ppal">
        <h1><g:message code="layout.footer.citizens"/></h1>
        <p>
            <g:message code="footer.menu.footerCitizens.description1"/>
        </p>
        <p>
            <g:message code="footer.menu.footerCitizens.description2"/>
        </p>
        <img src="${resource(dir: 'images', file: 'info6.png')}" alt="foto-manos" itemprop="image">
    </div>
</content>
