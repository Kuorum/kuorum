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
    <h1><g:message code="layout.footer.politicians"/></h1>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.description" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features" encodeAs="raw"/></p>
    <ul>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature1" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature2" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature3" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature4" encodeAs="raw"/></li>
    </ul>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.brief" encodeAs="raw" args="[g.createLink(mapping:'register'),g.createLink(mapping:'register')]"/></p>

    %{--<img src="${resource(dir: 'images', file: 'info5.png')}" alt="foto-madiba" itemprop="image">--}%
</content>
