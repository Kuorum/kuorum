<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">${campaign.name}</g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">

    <r:require modules="datepicker, participatoryBudgetForm" />
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <div class="box-steps container-fluid campaign-steps">
        <g:set var="mappings" value="${
            [
                    saveAndSentButtons:true,
                    step:'districts',
                    settings:'participatoryBudgetEditSettings',
                    content:'participatoryBudgetEditContent',
                    districts:'participatoryBudgetEditDistricts',
                    deadlines:'participatoryBudgetEditDeadlines',
                    showResult: 'campaignShow',
                    next: 'participatoryBudgetEditContent'
            ]}"/>
        <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
    </div>

    <div class="box-ppal campaign-new">
        <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

        <g:render template="/participatoryBudget/districts/districtsForm" model="[
                command: command,
                status:campaign.campaignStatusRSDTO,
                campaign:campaign,
                mappings:mappings,
                numberRecipients:numberRecipients]"/>
    </div>

</content>