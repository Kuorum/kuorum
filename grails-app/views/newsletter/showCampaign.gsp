<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
            <g:message code="head.logged.account.tools.massMailing.show" args="[campaign?campaign.name:newsletter.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${_domainName}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description", args:[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
    <r:require modules="campaignList, participatoryBudgetEditableTable"/>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message code="head.logged.account.tools.massMailing"/></g:link></li>
        <g:if test="${campaign}">
            <li><g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}">${campaign.title}</g:link></li>
        </g:if>
        <g:else>
            <li>${newsletter.name}</li>
        </g:else>
        <li class="active">${breadCrumbName}</li>
    </ol>
    <div class="box-ppal campaign-stats">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation" class="${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO?'':'active'}"><a href="#stats" data-toggle="tab"><g:message code="tools.massMailing.view.stats"/></a></li>
            <li role="presentation"><a href="#recipients" data-toggle="tab"><g:message code="tools.massMailing.list.recipients"/></a></li>
            <li role="presentation"><a href="#viewemail" data-toggle="tab"><g:message code="tools.massMailing.view.viewMail"/></a></li>
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
                <li role="presentation" class="active"><a href="#proposalLists" data-toggle="tab"><g:message code="tools.massMailing.view.participatoryBudget.proposalList"/></a></li>
            </g:if>
        </ul>
        <div id="tabs-stats-campaign" class="tab-content">
            <div class="tab-pane ${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO?'':'active'}" id="stats">
                <g:render template="/newsletter/campaignTabs/campaignStats" model="[newsletter:newsletter,campaign:campaign]"/>
            </div>
            <div class="tab-pane" id="recipients">
                <g:render template="/newsletter/campaignTabs/campaignRecipeints" model="[trackingPage:trackingPage, newsletterId:newsletter.id]"/>
            </div>
            <div class="tab-pane" id="viewemail">
                <g:render template="campaignTabs/campaignViewMail" model="[newsletter: newsletter]"/>
            </div>
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
                <div class="tab-pane active" id="proposalLists">
                    <g:render template="campaignTabs/participatoryBudgetProposalsList" model="[participatoryBudget: campaign]"/>
                </div>
            </g:if>
        </div>
    </div>
</content>