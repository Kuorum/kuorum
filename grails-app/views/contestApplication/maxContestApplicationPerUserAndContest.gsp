<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.contestApplication.title"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="centerColumn1Layout">

</head>

<content tag="mainContent">

    <div class="box-ppal center">
        <h1><g:message code="contestApplication.create.limitPerUser.title" /> </h1>
        <p><g:message code="contestApplication.create.limitPerUser.subtitle" args="[contestRSDTO.maxApplicationsPerUser]"/> </p>
        <br/>
        <g:link mapping="politicianCampaigns" class="btn btn-lg btn-blue"><g:message code="districtProposal.create.limitPerUser.campaignList"/></g:link>
    </div>
</content>