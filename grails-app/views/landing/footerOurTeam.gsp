<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingFooterLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[command:command, msgPrefix:'footerOurTeam']"/>
</content>

<content tag="footerLeftColumn">
    <g:render template="/landing/footerModules/leftColumn" model="[command:command, msgPrefix:'footerOurTeam']"/>
</content>

<content tag="footerSection">
    <g:render template="/landing/footerModules/team" model="[command:command, msgPrefix:'footerOurTeam']"/>
</content>