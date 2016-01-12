<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.projects"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerTechnology']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.tech"/></h1>
    <p>
        <g:message code="footer.menu.tech.description1"/>
    </p>
    %{--<img src="${resource(dir: 'images', file: 'info1.png')}" alt="foto-tribuna-eu" itemprop="image">--}%
</content>
