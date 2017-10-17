<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="individualCaseStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="caseStudyId" value="${caseStudyId}"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'caseStudy'+caseStudyId]"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="caseStudyBody">
    <g:render template="/landing/caseStudies/modules/caseStudyBody" model="[caseStudyId:caseStudyId]"/>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/otherCases" model="[otherCases:suggestedCaseStudies]"/>
</content>