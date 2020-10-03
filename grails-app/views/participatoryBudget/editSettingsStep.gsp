<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:if test="${campaign?.name}">
            ${campaign.name}
        </g:if>
        <g:else>
            <g:message code="tools.campaign.new.PARTICIPATORY_BUDGET"/>
        </g:else>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="/campaigns/edit/settingsStep"
              model="[
                      campaign:campaign,
                      attachEvent:false,
                      options:options,
                      command: command,
                      filters: filters,
                      totalContacts: totalContacts,
                      anonymousFilter: anonymousFilter,
                      events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK],
                      mappings:[
                              step:'settings',
                              settings:'participatoryBudgetEditSettings',
                              content:'participatoryBudgetEditContent',
                              districts:'participatoryBudgetEditDistricts',
                              deadlines:'participatoryBudgetEditDeadlines',
                              showResult: 'campaignShow',
                              next: 'participatoryBudgetEditContent']
              ]"/>
</content>