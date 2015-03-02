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
    <div class="box-ppal">
        <h1><g:message code="layout.footer.vision"/></h1>
        <p>
            <g:message code="footer.menu.footerVision.description1"/>
        </p>
        <p>
            <g:message code="footer.menu.footerVision.description2"/>
        </p>
        <p>
            <g:message code="footer.menu.footerVision.description3"/>
        </p>
        %{--<img src="${resource(dir: 'images', file: 'info-quekuorum.jpg')}" alt="foto-debate" itemprop="image">--}%
    </div>
</content>
