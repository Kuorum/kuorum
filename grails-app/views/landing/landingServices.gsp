<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingServices.head.title"/></title>
    <meta name="layout" content="landingServicesLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingServices.head.title'),
                      kuorumDescription:g.message(code:'landingServices.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/servicesModules/leadersCarousel" model="[msgPrefix:'landingServices']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="howItWorks">
    <g:render template="/landing/servicesModules/services" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="caseStudy">
    <g:render template="/landing/caseStudies/modules/landingCaseStudy" model="[msgPrefix:'landingServices', caseStudyId:'001']"/>
</content>

<content tag="statistics">
    <g:render template="/landing/servicesModules/statistics" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="trustUs">
    <g:render template="/landing/servicesModules/trustUs" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="solutions">
    <g:render template="/landing/servicesModules/solutions" model="[msgPrefix:'landingServices']"/>
</content>