<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingCaseStudy.page.head.title"/></title>
    <meta name="layout" content="landingCasesStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingCaseStudy.page.head.title'),
                      kuorumDescription:g.message(code:'landingCaseStudy.page.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'case-studies.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderNoCallToAction" model="[msgPrefix:'landingCaseStudy']"/>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/caseStudiesGrid" model="[msgPrefix:'landingCaseStudy', caseStudiesIds:['001','002','003','004','005']]"/>
</content>