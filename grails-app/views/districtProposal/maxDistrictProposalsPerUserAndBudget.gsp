<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="admin.createDistrictProposal.title"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="centerColumn1Layout">

</head>

<content tag="mainContent">

    <div class="box-ppal center">
        <h1><g:message code="districtProposal.create.limitPerUser.title" /> </h1>
        <p><g:message code="districtProposal.create.limitPerUser.subtitle" args="[participatoryBudgetRSDTO.maxDistrictProposalsPerUser]"/> </p>
            <h4><g:message code="districtProposal.create.limitPerUser.body" args="[participatoryBudgetRSDTO.maxDistrictProposalsPerUser]"/> </h4>
        <br/>
        <g:link mapping="contestListApplications" class="btn btn-lg btn-blue"><g:message code="districtProposal.create.limitPerUser.campaignList"/></g:link>
    </div>
</content>