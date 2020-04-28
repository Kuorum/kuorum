<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<h2 class="sr-only"><g:message code="tools.massMailing.view.report"/></h2>
<div class="pag-list-contacts clearfix">

    <div class="actions">
        <g:if test="${newsletter}">
            <g:link mapping="politicianMassMailingTrackEventsReport" params="[newsletterId:newsletter.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-campaignEvents-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.list.recipients.export.csv"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
            <g:link mapping="politicianMassMailingDebateStatsReport" params="[campaignId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-debateStats-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.stats.debate.report"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.survey.SurveyRSDTO}">
            <g:link mapping="politicianMassMailingSurveyStatsReport" params="[campaignId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-surveyStats-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.stats.survey.report"/>
            </g:link>
        </g:if>

        <g:if test="${campaign?.event}">
            <g:link mapping="eventAssistanceReport" params="[campaignId:campaign.id, checkList:true]" class="btn btn-blue inverted export-modal-button" data-modalId="export-eventAssistants-modal">
                <span class="fal fa-file-pdf"></span>
                <g:message code="tools.massMailing.list.event.assistants.checkList"/>
            </g:link>
            <g:link mapping="eventAssistanceReport" params="[campaignId:campaign.id, checkList: false]" class="btn btn-blue inverted export-modal-button" data-modalId="export-eventAssistants-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.list.event.assistants.report"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
            <g:link mapping="politicianMassMailingParticipatoryBudgetReport" params="[campaignId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-proposalsList-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.participatoryBudget.report"/>
            </g:link>
        </g:if>
    </div>
</div>
<div id="report-files">
    <div class="report-files-list-actions">
        <g:set var="link" value="#"/>
        <g:if test="${campaign}">
            <g:set var="link" value="${g.createLink(mapping: 'politicianCampaignRefreshReport', params:[campaignId:campaign.id])}"/>
        </g:if>
        <g:else>
            <g:set var="link" value="${g.createLink(mapping: 'politicianNewsletterRefreshReport', params:[newsletterId:newsletter.id])}"/>
        </g:else>
        <a href="${link}" class="btn btn-grey inverted files-list-btn-refresh">
            <span class="fal fa-sync"></span> <g:message code="tools.massMailing.view.reports.reload"/>
        </a>
    </div>
    <div id="report-files-list">
        <g:render template="/newsletter/campaignTabs/campaignReportsList" model="[reportsList:reportsList]"/>
    </div>
</div>