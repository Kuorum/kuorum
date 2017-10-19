<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerUserGuides.head.title"/></title>
    <meta name="layout" content="landingFooterLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerUserGuides.head.title'),
                      kuorumDescription:g.message(code:'footerUserGuides.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'user-guides.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'footerUserGuides']"/>
</content>

<content tag="footerLeftColumn">
    <g:render template="/footer/footerModules/leftColumn" model="[msgPrefix:'footerUserGuides']"/>
</content>

<content tag="footerSection">
    <g:render template="/footer/footerModules/userGuides" model="[msgPrefix:'footerUserGuides']"/>
</content>