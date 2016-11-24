<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.politicians"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.politicians'),
                      kuorumDescription:g.message(code:'page.title.footer.politicians.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerPoliticians']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.politicians"/></h1>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.politicians.description1" encodeAs="raw"/></p>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.brief" encodeAs="raw" args="[g.createLink(mapping:'landingPrices'),g.createLink(mapping:'register')]"/></p>
</content>
