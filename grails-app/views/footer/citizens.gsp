<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.citizens"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.citizens'),
                      kuorumDescription:g.message(code:'page.title.footer.citizens.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
    <link rel="canonical" href="https://${request.serverName}${g.createLink(mapping: 'footerCitizens')}" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerCitizens']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.citizens"/></h1>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature1" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature2" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature3" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature4" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.features.feature5" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forCitizen.brief" encodeAs="raw" args="[g.createLink(mapping:'register')]"/></p>
    %{--<p><img src="${resource(dir: 'images', file: 'ipdb-kuorum.png')}" alt="we-build-transparency" itemprop="image"></p>--}%
</content>
