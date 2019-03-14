<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:if test="${campaign?.name}">
            ${campaign.name}
        </g:if>
        <g:else>
            <g:message code="tools.campaign.new.districtProposal"/>
        </g:else>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <li><g:link mapping="politicianCampaignsNew"><g:message code="tools.campaign.new.title"/></g:link></li>
        <li><g:link mapping="participatoryBudgetShow" params="${participatoryBudget.encodeAsLinkProperties()}">${participatoryBudget.title}</g:link></li>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <g:render template="/campaigns/edit/contentStep" model="[
            attachEvent:false,
            command: command,
            numberRecipients:numberRecipients,
            status: status,
            customPlaceHolderBody:g.message(code: 'kuorum.web.commands.payment.CampaignContentCommand.body.placeHolder.districtProposal'),
            mappings:[
                    saveAndSentButtons:true,
                    step:'content',
                    settings:'districtProposalEditSettings',
                    content:'districtProposalEditContent',
                    district:'districtProposalEditDistrict',
                    showResult: 'districtProposalShow',
                    next: 'districtProposalShow']]"/>

    <r:script >
        $(function(){
            formHelper.dirtyFormControl.dirty($("#politicianMassMailingForm"))
        })
    </r:script>
</content>