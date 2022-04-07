<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
<g:set var="breadCrumbName">
    <g:message code="head.logged.account.tools.massMailing.show" args="[campaign ? campaign.name : newsletter.name]"/>
</g:set>

<title>${breadCrumbName}</title>
<meta name="layout" content="basicPlainLayout">
<!-- Schema.org markup for Google+ -->
<meta itemprop="name" content="${_domainName}">
<meta itemprop="description"
      content="${g.message(code: "layout.head.meta.description", args: [kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
<r:require modules="campaignList, participatoryBudgetEditableTable"/>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message
        code="head.logged.account.tools.massMailing"/></g:link></li>
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
        <li role="presentation" class="${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO ? '' : 'active'}"><a
        href="#reportsLists" data-toggle="tab"><g:message code="tools.massMailing.view.report"/></a></li>
<g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.bulletin.BulletinRSDTO}">
    <li role="presentation"><a href="#stats" data-toggle="tab"><g:message code="tools.massMailing.view.stats"/></a></li>
    <li role="presentation"><a href="#recipients" data-toggle="tab"><g:message
            code="tools.massMailing.list.recipients"/></a></li>
</g:if>
<g:if test="${newsletter.htmlBody}">
    <li role="presentation"><a href="#viewemail" data-toggle="tab"><g:message
            code="tools.massMailing.view.viewMail"/></a></li>
</g:if>
<g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
    <li role="presentation" class="active"><a href="#proposalLists" data-toggle="tab"><g:message
            code="tools.massMailing.view.participatoryBudget.proposalList"/></a></li>
</g:if>
</ul>
<div id="tabs-stats-campaign" class="tab-content">
<g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.bulletin.BulletinRSDTO}">
    <div class="tab-pane ${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO ? '' : 'active'}"
         id="reportsLists">
        <g:render template="/newsletter/campaignTabs/campaignReports" model="[reportsList: reportsList]"/>
    </div>
</g:if>

    <div class="tab-pane" id="stats">
        <g:render template="/newsletter/campaignTabs/campaignStats"
                  model="[newsletter: newsletter, campaign: campaign]"/>
    </div>

    <div class="tab-pane" id="recipients">
        <g:render template="/newsletter/campaignTabs/campaignRecipeints"
                  model="[trackingPage: trackingPage, newsletterId: newsletter.id]"/>
    </div>
    <g:if test="${newsletter.htmlBody}">
        <div class="tab-pane" id="viewemail">
            <g:render template="/newsletter/campaignTabs/campaignViewMail" model="[campaign: campaign]"/>
        </div>
    </g:if>
    <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
        <div class="tab-pane active" id="proposalLists">
            <g:render template="/newsletter/campaignTabs/participatoryBudgetProposalsList"
                      model="[participatoryBudget: campaign]"/>
        </div>
    </g:if>
    </div>

    <!-- MODALS -->
    <div class="show-campaign-modals">

    <!-- MODAL DEBATE -->
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
            <div id="export-debateStats-modal" class="modal fade in" tabindex="-1" role="dialog"
                 aria-labelledby="exportDebateStatsTitle" aria-hidden="true">
                <div class="modal-dialog ">
                    <div class="modal-content">
                        <div class="modal-header"><h4><g:message code="modal.exportedDebateStats.title"/></h4></div>

                        <div class="modal-body">
                            <p>
                                <g:message code="modal.exportedDebateStats.explanation"/>
                                <g:message code="modal.exported.explanation"/>
                            </p>
                        </div>

                        <div class="modal-footer">
                            <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                    code="modal.exportedDebateStats.close"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </g:if>

    <!-- MODAL SURVEY STATS -->
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.survey.SurveyRSDTO}">
            <div id="export-surveyStats-modal" class="modal fade in" tabindex="-1" role="dialog"
                 aria-labelledby="exportSurveyStatsTitle" aria-hidden="true">
                <div class="modal-dialog ">
                    <div class="modal-content">
                        <div class="modal-header"><h4><g:message code="modal.exportedSurveyStats.title"/></h4></div>

                        <div class="modal-body">
                            <p>
                                <g:message code="modal.exportedSurveyStats.explanation"/>
                                <g:message code="modal.exported.explanation"/>
                            </p>
                        </div>

                        <div class="modal-footer">
                            <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                    code="modal.exportedSurveyStats.close"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </g:if>

        <div id="export-campaignEvents-modal" class="modal fade in" tabindex="-1" role="dialog"
             aria-labelledby="exportTagsTitle" aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header"><h4><g:message code="modal.exportedTrackingEvents.title"/></h4></div>

                    <div class="modal-body">
                        <p>
                            <g:message code="modal.exportedTrackingEvents.explanation"/>
                            <g:message code="modal.exported.explanation"/>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                code="modal.exportedTrackingEvents.close"/></a>
                    </div>
                </div>
            </div>
        </div>

    <!-- MODAL EVENT ASSISTANS -->
        <g:if test="${campaign && campaign.event}">
            <div id="export-eventAssistants-modal" class="modal fade in" tabindex="-1" role="dialog"
                 aria-labelledby="exportAssistantsTitle" aria-hidden="true">
                <div class="modal-dialog ">
                    <div class="modal-content">
                        <div class="modal-header"><h4><g:message code="modal.exportEventAssistants.title"/></h4></div>

                        <div class="modal-body">
                            <p>
                                <g:message code="modal.exportEventAssistants.explanation"/>
                                <g:message code="modal.exported.explanation"/>
                            </p>
                        </div>

                        <div class="modal-footer">
                            <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                    code="modal.exportEventAssistants.close"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </g:if>

    <!-- MODAL PARTICIPATORY BUDGET -->
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
            <div id="export-proposalsList-modal" class="modal fade in" tabindex="-1" role="dialog"
                 aria-labelledby="exportDebateStatsTitle" aria-hidden="true">
                <div class="modal-dialog ">
                    <div class="modal-content">
                        <div class="modal-header"><h4><g:message
                                code="modal.exportedParticipatoryBudgetProposals.title"/></h4></div>

                        <div class="modal-body">
                            <p>
                                <g:message code="modal.exportedParticipatoryBudgetProposals.explanation"
                                           args="[campaign.title]"/>
                            </p>
                        </div>

                        <div class="modal-footer">
                            <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                    code="modal.exportedDebateStats.close"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </g:if>

    </div>
    <!-- END MODALS -->
</div>
</content>