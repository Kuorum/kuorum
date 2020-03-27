<%@ page import="kuorum.core.customDomain.CustomDomainResolver; org.springframework.util.CollectionUtils" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${subtitle}</title>
    <meta name="layout" content="landingLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="showLatestActivities" value="${org.apache.commons.collections.CollectionUtils.isNotEmpty(campaigns)}"/>
    <parameter name="showHowItWorks" value="${org.apache.commons.collections.CollectionUtils.isNotEmpty(landingVisibleRoles)}"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:slogan,
                      kuorumDescription:subtitle,
                      kuorumImage:CustomDomainResolver.domainRSDTO.basicRootUrlStaticResources+'/landingSlider/landing-1.jpg'
              ]"/>
</head>

<g:if test="${starredCampaign}">
    <content tag="starredCampaign-section">
        <g:render template="/landing/commonModules/starredCampaign" model="[starredCampaign:starredCampaign]"/>
    </content>
</g:if>

<content tag="main">
    <g:render template="/landing/servicesModules/leadersCarousel" model="[msgPrefix:'landingServices', slogan:slogan, subtitle:subtitle, command: command]"/>
    <g:if test="${domainDescription}">
        <div id="domain-description-landing-main">
            ${domainDescription}
        </div>
    </g:if>
</content>

<content tag="howItWorks">
    <g:if test="${landingVisibleRoles}">
        <g:render template="/landing/servicesModules/services" model="[landingVisibleRoles:landingVisibleRoles]"/>
    </g:if>
</content>

<content tag="latestActivities">
    <div class="section-header">
        <h1><g:message code="landingServices.latestActivities.title"/></h1>
        <h3 class="hidden-xs"><g:message code="landingServices.latestActivities.subtitle"/></h3>
    </div>
    <ul class="search-list latestActivities clearfix">
        <g:render template="/campaigns/cards/campaignsList" model="[campaigns:campaigns, center:true]"/>
    </ul>

    <g:link mapping="register" id="register-submit" class="btn btn-orange btn-lg"> <g:message code="landingPage.register.form.submit"/></g:link>
</content>
