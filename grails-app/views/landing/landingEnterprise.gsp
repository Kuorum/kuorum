<%@ page import="kuorum.core.model.ContactSectorType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingEnterprise.head.title"/></title>
    <meta name="layout" content="landingSectorsLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingEnterprise.head.title'),
                      kuorumDescription:g.message(code:'landingEnterprise.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'corporations.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderCallToAction" model="[msgPrefix:'landingEnterprise']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="pain">
    <g:render template="/landing/enterpriseModules/painBullets" model="[msgPrefix:'landingEnterprise']"/>
</content>

<content tag="gain">
    <g:render template="/landing/enterpriseModules/gainBullets" model="[msgPrefix:'landingEnterprise']"/>
</content>

<content tag="caseStudy">
    <g:render template="/landing/caseStudies/modules/landingCaseStudy" model="[msgPrefix:'landingEnterprise', caseStudyId:'002', sectionName:'landingCaseStudy']"/>
</content>

<content tag="contactUs">
    <g:render template="/landing/commonModules/contactUs" model="[msgPrefix:'landingEnterprise', sectorDefault: kuorum.core.model.ContactSectorType.CORPORATION]"/>
</content>