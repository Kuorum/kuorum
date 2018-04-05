<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingServices.head.title"/></title>
    <meta name="layout" content="landingServicesLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingServices.head.title'),
                      kuorumDescription:g.message(code:'landingServices.head.description'),
                      kuorumImage:r.resource(dir:'images', file:'landing-kuorum.jpg', absolute:true)
              ]"/>
</head>

<content tag="main">
    <g:render template="/landing/servicesModules/leadersCarousel" model="[msgPrefix:'landingServices']"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="howItWorks">
    <g:render template="/landing/servicesModules/services" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="customWebSite">
    <g:render template="/landing/technologyModules/featureBulletsRight" model="[msgPrefix:'landingTechnology', msgSection: 'customWebSite', imgBackground:'landing-webSite.png']"/>
</content>

<content tag="featuresCrm">
    <g:render template="/landing/technologyModules/featureBulletsLeft" model="[msgPrefix:'landingTechnology', msgSection: 'featuresCrm', imgBackground:'landing-contacts.png']"/>
</content>

<content tag="featuresUser">
    <g:render template="/landing/technologyModules/featureBulletsRight" model="[msgPrefix:'landingTechnology', msgSection: 'featuresUser', imgBackground:'landing-debates.png']"/>
</content>

<content tag="metrics">
    <g:render template="/landing/technologyModules/featureBulletsLeft" model="[msgPrefix:'landingTechnology', msgSection: 'metrics', imgBackground:'landing-campaigns.png']"/>
</content>

<content tag="caseStudy">
    <g:render template="/landing/caseStudies/modules/landingCaseStudy" model="[msgPrefix:'landingServices', caseStudyId:'006', sectionName:'landingCaseStudy']"/>
</content>

<content tag="statistics">
    <g:render template="/landing/servicesModules/statistics" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="trustUs">
    <g:render template="/landing/servicesModules/trustUs" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="solutions">
    <g:render template="/landing/servicesModules/solutions" model="[msgPrefix:'landingServices']"/>
</content>