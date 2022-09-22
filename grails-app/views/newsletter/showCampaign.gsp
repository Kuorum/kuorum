<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="breadCrumbName">
        <g:message code="head.logged.account.tools.massMailing.show"
                   args="[campaign ? campaign.name : newsletter.name]"/>
    </g:set>

    <title>${breadCrumbName}</title>
    <meta name="layout" content="basicPlainLayout">
    <!-- Schema.org markup for Google -->
    <meta itemprop="name" content="${_domainName}">
    <meta itemprop="description"
          content="${g.message(code: "layout.head.meta.description", args: [kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name])}">
    <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
        <r:require modules="campaignList, participatoryBudgetEditableTable"/>
    </g:if>
    <g:elseif test="${campaign && campaign instanceof org.kuorum.rest.model.communication.contest.ContestRSDTO}">
        <r:require modules="campaignList, contestApplicationEditableTable"/>
    </g:elseif>
    <g:else>
        <r:require modules="campaignList, participatoryBudgetEditableTable"/>
    </g:else>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li><g:link mapping="politicianCampaigns"><g:message
                code="head.logged.account.tools.massMailing"/></g:link></li>
        <g:if test="${!(campaign instanceof org.kuorum.rest.model.communication.bulletin.BulletinRSDTO)}">
            <li><g:link mapping="campaignShow"
                        params="${campaign.encodeAsLinkProperties()}">${campaign.name}</g:link></li>
        </g:if>
        <li class="active">${breadCrumbName}</li>
    </ol>

    <div class="box-ppal campaign-stats">
        <ul class="nav nav-tabs simple" data-tabs="tabs">
            <li role="presentation"><a href="#reportsLists" data-toggle="tab"><g:message
                    code="tools.massMailing.view.report"/></a></li>
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.bulletin.BulletinRSDTO}">
                <li role="presentation"><a href="#stats" data-toggle="tab"><g:message
                        code="tools.massMailing.view.stats"/></a></li>
                <g:if test="${newsletter.htmlBody}">
                    <li role="presentation"><a href="#viewemail" data-toggle="tab"><g:message
                            code="tools.massMailing.view.viewMail"/></a></li>
                </g:if>
            </g:if>
            <g:if test="${campaignStatusesValid}">
                <li role="presentation"><a href="#recipients" data-toggle="tab"><g:message
                        code="tools.massMailing.list.recipients"/></a></li>
            </g:if>
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
                <li role="presentation" class="active"><a href="#proposalLists" data-toggle="tab"><g:message
                        code="tools.massMailing.view.participatoryBudget.proposalList"/></a></li>
            </g:if>
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.contest.ContestRSDTO}">
                <li role="presentation" class="active"><a href="#applicationLists" data-toggle="tab"><g:message
                        code="tools.massMailing.view.contest.applicationsList"/></a></li>
            </g:if>
        </ul>

        <div id="tabs-stats-campaign" class="tab-content">
            <div class="tab-pane"
                 id="reportsLists">
                <g:render template="/newsletter/campaignTabs/campaignReports" model="[reportsList: reportsList]"/>
            </div>


            <div class="tab-pane" id="stats">
                <g:render template="/newsletter/campaignTabs/campaignStats"
                          model="[newsletter: newsletter, campaign: campaign]"/>
            </div>

            <div class="tab-pane" id="recipients">
                <g:render template="/newsletter/campaignTabs/campaignRecipeints"
                          model="[trackingPage: trackingPage, campaignId: campaign.id, campaignStatusesValid: campaignStatusesValid]"/>
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
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.contest.ContestRSDTO}">
                <div class="tab-pane active" id="applicationLists">
                    <g:render template="/newsletter/campaignTabs/contestApplicationList" model="[contest: campaign]"/>
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

                <div id="close-survey-modal" class="modal fade in" tabindex="-1" role="dialog" aria-hidden="true">
                    <div class="modal-dialog ">
                        <div class="modal-content">
                            <div class="modal-header"><h4><g:message code="modal.closeSurvey.title"/></h4></div>

                            <div class="modal-body">
                                <p>
                                    <g:message code="modal.closeSurvey.explanation"/>
                                </p>
                            </div>

                            <div class="modal-footer">
                                <a href="#" class="btn btn-grey-light" data-dismiss="modal"
                                   aria-label="Close"><g:message
                                        code="profile.modal.cropImage.cancel"/></a>
                                <a href="#" class="btn submit-close-survey-button"><g:message
                                        code="tools.massMailing.view.stats.survey.close"/></a>
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
                            <div class="modal-header"><h4><g:message code="modal.exportEventAssistants.title"/></h4>
                            </div>

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
            <g:if test="${campaign
                    && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO
                    || campaign instanceof org.kuorum.rest.model.communication.contest.ContestRSDTO}">
                <g:if test="${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
                    <g:set var="title"><g:message code="modal.exportedParticipatoryBudgetProposals.title"/></g:set>
                    <g:set var="explanation"><g:message code="modal.exportedParticipatoryBudgetProposals.explanation"
                                                        args="[campaign.title]"/></g:set>
                </g:if>
                <g:else>
                    <g:set var="title"><g:message code="modal.exportedApplications.title"/></g:set>
                    <g:set var="explanation"><g:message code="modal.exportedApplications.explanation"
                                                        args="[campaign.title]"/></g:set>
                </g:else>
                <div id="export-proposalsList-modal" class="modal fade in" tabindex="-1" role="dialog"
                     aria-labelledby="exportDebateStatsTitle" aria-hidden="true">
                    <div class="modal-dialog ">
                        <div class="modal-content">
                            <div class="modal-header"><h4>${title}</h4></div>

                            <div class="modal-body"><p>${explanation}</p></div>

                            <div class="modal-footer">
                                <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                        code="modal.exportedDebateStats.close"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>

    <!-- MODAL CONTEST -->
            <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.contest.ContestRSDTO}">
                <div id="export-votesList-modal" class="modal fade in" tabindex="-1" role="dialog"
                     aria-labelledby="exportContestStatsTitle" aria-hidden="true">
                    <div class="modal-dialog ">
                        <div class="modal-content">
                            <div class="modal-header"><h4><g:message
                                    code="modal.exportedContestVotes.title"/></h4></div>

                            <div class="modal-body">
                                <p>
                                    <g:message code="modal.exportedContestVotes.explanation"
                                               args="[campaign.title]"/>
                                </p>
                            </div>

                            <div class="modal-footer">
                                <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message
                                        code="modal.exportedContestVotes.close"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>

        </div>
        <!-- END MODALS -->
    </div>
</content>