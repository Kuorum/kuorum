<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingCaseStudy.${caseStudyId}.content.title"/></title>
    <meta name="layout" content="individualCaseStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/landing/caseStudies/modules/caseStudyMetas" model="[caseStudyId:caseStudyId]"/>
</head>

<content tag="breadcrumb">
    <g:render template="/landing/caseStudies/modules/caseStudyBreadCrumb"/>
</content>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'caseStudy'+caseStudyId]"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="caseStudyBody">
    <h1><g:message code="landingCaseStudy.${caseStudyId}.content.title"/> </h1>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text.1"/></p>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text.2"/></p>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text.3"/></p>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text.4"/></p>
    <p><img data-src="${resource(dir: 'images', file: 'case-study-3-img.jpg')}" alt="inception-session" title="inception-session"></p>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text.5"/></p>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text.6"/></p>
    %{--<g:render template="/landing/caseStudies/modules/requestCaseStudy" model="[caseStudyId:caseStudyId]"/>--}%
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/otherCases" model="[otherCases:suggestedCaseStudies, sectionName:'landingCaseStudy']"/>
    <g:render template="/landing/caseStudies/modules/otherCasesRequestDemo"/>
</content>