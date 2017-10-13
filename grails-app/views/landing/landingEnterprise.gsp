<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="landingSectorsLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
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
    <g:render template="/landing/enterpriseModules/caseStudy" model="[msgPrefix:'landingEnterprise', msgSection: 'featuresUser', imgBackground:'features-leaders.png']"/>
</content>

<content tag="contactUs">
    <g:render template="/landing/commonModules/contactUs" model="[msgPrefix:'landingEnterprise']"/>
</content>