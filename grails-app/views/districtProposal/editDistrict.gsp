<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">${campaign.name}</g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew">Nombre presupuesto participativo</g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <g:set var="mappings" value="${
        [
                saveAndSentButtons:false,
                step:'district',
                settings:'districtProposalEditSettings',
                content:'districtProposalEditContent',
                district:'districtProposalEditDistrict',
                showResult: 'campaignShow',
                next: 'districtProposalEditContent'
        ]}"/>

    <g:render template="/districtProposal/editModules/editDistrict" model="[
            command: command,
            campaign:campaign,
            participatoryBudget:participatoryBudget,
            numberRecipients:numberRecipients,
            mappings:mappings
    ]"/>

</content>