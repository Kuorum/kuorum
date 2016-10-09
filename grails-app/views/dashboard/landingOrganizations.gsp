<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.landing.organizations"/></title>
    <meta name="layout" content="landingLayout">
    <parameter name="transparentHead" value="true"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.landing.organizations'),
                      kuorumDescription:g.message(code:'page.title.landing.organizations.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images', file:'background-capitol.png')
              ]"/>
</head>

<content tag="videoAndRegister">
    <g:render template="landingOrganizationsModules/videoAndRegister" model="[command:command]"/>
</content>

<content tag="logos">
    <g:render template="landingPageModules/landingLogos"/>
</content>

<content tag="special">
    <g:render template="landingOrganizationsModules/landingKuorumFeautres"/>
</content>

<content tag="mainContent">
    <g:render template="landingOrganizationsModules/landingTestimonies"/>
</content>

<content tag="pressKit">
    <g:render template="landingPageModules/pressKit"/>
</content>


