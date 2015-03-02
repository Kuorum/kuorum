<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.information"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuPress" model="[activeMapping:'footerInformation']"/>
</content>

<content tag="mainContent">
    <div class="box-ppal">
        <h1><g:message code="layout.footer.information"/></h1>
        <p>
            <g:message code="footer.menu.footerInformation.description1"/>
        </p>
        <p>
            <g:message code="footer.menu.footerInformation.description2"/>
        </p>
        <p>
            <g:message code="footer.menu.footerInformation.description3"/>
        </p>
        %{--<img src="${resource(dir: 'images', file: 'info-quekuorum.jpg')}" alt="foto-debate" itemprop="image">--}%
    </div>
</content>
