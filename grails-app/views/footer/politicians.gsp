<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.politicians"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerPoliticians']"/>
</content>

<content tag="mainContent">
    <div class="box-ppal">
        <h1><g:message code="layout.footer.politicians"/></h1>
        <p>
            <g:message code="footer.menu.footerPoliticians.description1"/>
        </p>
        <p>
            <g:set var="linkNewPolitics" value="${createLink(mapping:'funnelSuccessfulStories')}"/>
            <g:message code="footer.menu.footerPoliticians.description2" args="[linkNewPolitics]" encodeAs="raw"/>
        </p>
        <img src="${resource(dir: 'images', file: 'info5.png')}" alt="foto-madiba" itemprop="image">
    </div>
</content>
