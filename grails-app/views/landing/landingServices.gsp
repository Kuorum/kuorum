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
    <g:render template="/landing/servicesModules/leadersCarousel" model="[msgPrefix:'landingServices', slogan:slogan, subtitle:subtitle, command: command]"/>
</content>

<content tag="howItWorks">
    <g:render template="/landing/servicesModules/services" model="[msgPrefix:'landingServices']"/>
</content>

<content tag="latestActivities">
    <div class="section-header">
        <h1><g:message code="landingServices.latestActivities.title"/></h1>
        <h3 class="hidden-xs"><g:message code="landingServices.latestActivities.subtitle"/></h3>
    </div>
    <ul class="search-list latestActivities clearfix">
        <g:render template="/campaigns/cards/campaignsList" model="[campaigns:campaigns, numColumns:3]"/>
    </ul>

    <g:link mapping="register" id="register-submit" class="btn btn-orange btn-lg"> <g:message code="landingPage.register.form.submit"/></g:link>

</content>