<%@ page import="kuorum.core.model.ContactSectorType; kuorum.core.model.Sector" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingSectorsLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingOrganization.head.title'),
                      kuorumDescription:g.message(code:'landingOrganization.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderCallToAction" model="[msgPrefix:'landingOrganization']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="pain">
    <g:render template="/landing/organizationModules/painBullets" model="[msgPrefix:'landingOrganization']"/>
</content>

<content tag="gain">
    <g:render template="/landing/organizationModules/gainBullets" model="[msgPrefix:'landingOrganization']"/>
</content>

<content tag="caseStudy">
    <g:render template="/landing/caseStudies/modules/landingCaseStudy" model="[msgPrefix:'landingOrganization', caseStudyId:'003']"/>
</content>

<content tag="contactUs">
    <g:render template="/landing/commonModules/contactUs" model="[msgPrefix:'landingOrganization', sectorDefault: kuorum.core.model.ContactSectorType.ORGANIZATION]"/>
</content>