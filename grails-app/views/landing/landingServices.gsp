<%@ page import="kuorum.core.customDomain.CustomDomainResolver; org.springframework.util.CollectionUtils" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${subtitle}</title>
    <meta name="layout" content="landingLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="showLatestActivities" value="${!org.springframework.util.CollectionUtils.isEmpty(campaigns)}"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:slogan,
                      kuorumDescription:subtitle,
                      kuorumImage:CustomDomainResolver.domainRSDTO.basicRootUrlStaticResources+'/landingSlider/landing-1.jpg'
              ]"/>
</head>

<content tag="main">
    <g:if test="${starredCampaign}">
        <div id="starredCampaign">
            <div class="starredCampaign-title col-xs-10">
                <campaignUtil:showIcon campaign="${starredCampaign}"/>
                <span class="starredCampaing-title-text">
                    <span class="hidden-xs"><g:message code="landingPage.starredCampaign.whatsGoingOn"/>:</span>
                    ${starredCampaign.title}
                </span>
            </div>
            <div class="starredCampaign-btn col-xs-2">
                <g:link mapping="campaignShow" params="${starredCampaign.encodeAsLinkProperties()}" class="btn btn-lg">Participa</g:link>
            </div>
        </div>
    </g:if>
    <g:render template="/landing/servicesModules/leadersCarousel" model="[msgPrefix:'landingServices', slogan:slogan, subtitle:subtitle, command: command]"/>
    <g:if test="${domainDescription}">
        <div id="domain-description-landing-main">
            ${domainDescription}
        </div>
    </g:if>
</content>

<content tag="howItWorks">
    <g:render template="/landing/servicesModules/services" model="[landingVisibleRoles:landingVisibleRoles]"/>
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
