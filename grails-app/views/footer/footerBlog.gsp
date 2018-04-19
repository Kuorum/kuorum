<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="footerBlog.head.title"/></title>
    <meta name="layout" content="landingCasesStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'footerBlog.head.title'),
                      kuorumDescription:g.message(code:'footerBlog.head.description'),
                      kuorumImage:r.resource(dir:'images/landing', file:'blog.jpg', absolute:true)
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderNoCallToAction" model="[msgPrefix:'footerBlog']"/>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/caseStudiesGrid" model="[msgPrefix:'footerBlog', caseStudiesIds:['006','005','004','003','002','001']]"/>
</content>