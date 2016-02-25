<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/></title>
    <meta name="layout" content="landingLayout">
    <parameter name="transparentHead" value="true"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingPage.videoAndRegister.title'),
                      kuorumDescription:g.message(code:'landingPage.videoAndRegister.subtitle'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images', file:'background.png')
              ]"/>
</head>

<content tag="videoAndRegister">
    <g:render template="landingPageModules/videoAndRegister" model="[command:command]"/>
</content>

<content tag="logos">
    <g:render template="landingPageModules/landingLogos"/>
</content>

<content tag="special">
    <g:render template="landingPageModules/landingKuorumFeautres"/>
</content>

<content tag="mainContent">
    <g:render template="landingPageModules/landingTestimonies"/>
</content>

<content tag="pressKit">
    <g:render template="landingPageModules/pressKit"/>
</content>


