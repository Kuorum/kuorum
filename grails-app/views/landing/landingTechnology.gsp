<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingTechnology.head.title"/></title>
    <meta name="layout" content="landingTechnologyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingTechnology.head.title'),
                      kuorumDescription:g.message(code:'landingTechnology.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/commonModules/sliderCallToAction" model="[msgPrefix:'landingTechnology']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="howItWorks">
    <g:render template="/landing/technologyModules/bullets" model="[msgPrefix:'landingTechnology']"/>
</content>

<content tag="featuresCrm">
    <g:render template="/landing/technologyModules/featureBulletsLeft" model="[msgPrefix:'landingTechnology', msgSection: 'featuresCrm', imgBackground:'features-leaders.png']"/>
</content>

<content tag="featuresUser">
    <g:render template="/landing/technologyModules/featureBulletsRight" model="[msgPrefix:'landingTechnology', msgSection: 'featuresUser', imgBackground:'features-corporations.png']"/>
</content>

<content tag="metrics">
    <g:render template="/landing/technologyModules/featureBulletsLeft" model="[msgPrefix:'landingTechnology', msgSection: 'metrics', imgBackground:'features-organizations.png']"/>
</content>

<content tag="contactUs">
    <g:render template="/landing/commonModules/contactUs" model="[msgPrefix:'landingTechnology', sectorDefault: null]"/>
</content>