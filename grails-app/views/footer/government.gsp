<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.government"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.government'),
                      kuorumDescription:g.message(code:'page.title.footer.government.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenuGuide" model="[activeMapping:'footerGovernment']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.government"/></h1>
    <p><g:message code="footer.menu.footerWhatIsKuorum.forPoliticians.administration.description1" encodeAs="raw"/></p>
</content>
