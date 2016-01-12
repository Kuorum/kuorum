<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.aboutUs"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerAboutUs']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.aboutUs" encodeAs="raw"/></h1>
    <p><g:message code="footer.menu.aboutUs.description1" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.aboutUs.description2" encodeAs="raw"/></p>
    %{--<p><img src="${resource(dir: 'images', file: 'info4.png')}" alt="foto-debate" itemprop="image"></p>--}%
</content>


