<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingCasesStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingCasesStudy.head.title'),
                      kuorumDescription:g.message(code:'landingCasesStudy.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderNoCallToAction" model="[msgPrefix:'landingCasesStudy']"/>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/caseStudiesGrid" model="[msgPrefix:'landingCasesStudy', caseStudiesIds:['001','002','003','004']]"/>
</content>