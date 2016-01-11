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
    <p><g:message code="footer.menu.footerWhatIsKuorum.description" encodeAs="raw"/></p>
    <h4><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians" encodeAs="raw"/></h4>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.description" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features" encodeAs="raw"/></p>
    <ul>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature1" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature2" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature3" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.features.feature4" encodeAs="raw"/></li>
    </ul>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.brief" encodeAs="raw" args="[g.createLink(mapping:'register'),g.createLink(mapping:'register')]"/></p>

    <h4><g:message code="footer.menu.footerWhatIsKuorum.forCitizen" encodeAs="raw"/></h4>
    <ul>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature1" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature2" encodeAs="raw"/></li>
        <li><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature3" encodeAs="raw"/></li>
    </ul>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.brief" encodeAs="raw" args="[g.createLink(mapping:'register')]"/></p>
    <h4><g:message code="footer.menu.footerWhatIsKuorum.forEditors" encodeAs="raw"/></h4>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forEditors.description1" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forEditors.description2" encodeAs="raw" args="['mailto:info@kuorum.org']"/></p>

    <img src="${resource(dir: 'images', file: 'info4.png')}" alt="foto-debate" itemprop="image">
</content>


