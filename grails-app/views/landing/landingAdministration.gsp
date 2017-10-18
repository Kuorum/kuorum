<%@ page import="kuorum.core.model.ContactSectorType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingAdministration.head.title"/></title>
    <meta name="layout" content="landingSectorsLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingAdministration.head.title'),
                      kuorumDescription:g.message(code:'landingAdministration.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderCallToAction" model="[msgPrefix:'landingAdministration']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="pain">
    <g:render template="/landing/administrationModules/painBullets" model="[msgPrefix:'landingAdministration']"/>
</content>

<content tag="gain">
    <g:render template="/landing/administrationModules/gainBullets" model="[msgPrefix:'landingAdministration']"/>
</content>

<content tag="caseStudy">
    <g:render template="/landing/caseStudies/modules/landingCaseStudy" model="[msgPrefix:'landingAdministration', caseStudyId:'001', sectionName:'landingCaseStudy']"/>
</content>

<content tag="contactUs">
    <g:render template="/landing/commonModules/contactUs" model="[msgPrefix:'landingAdministration', sectorDefault: kuorum.core.model.ContactSectorType.GOVERNMENT]"/>
</content>