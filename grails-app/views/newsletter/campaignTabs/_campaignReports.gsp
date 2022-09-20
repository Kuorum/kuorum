<%@ page import="kuorum.web.commands.payment.survey.SurveyReportType; org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<h2 class="sr-only"><g:message code="tools.massMailing.view.report"/></h2>
<div class="pag-list-contacts clearfix">

    <div class="actions">
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.bulletin.BulletinRSDTO}">

            <g:link mapping="politicianMassMailingTrackEventsReport" params="[campaignId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-campaignEvents-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.list.recipients.export.csv"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign.exportableData && campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
            <g:link mapping="politicianMassMailingDebateStatsReport" params="[campaignId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-debateStats-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.stats.debate.report"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign.exportableData &&  campaign instanceof org.kuorum.rest.model.communication.survey.SurveyRSDTO}">
            <g:link mapping="politicianMassMailingSurveyReport" params="[campaignId:campaign.id, surveyReportType:kuorum.web.commands.payment.survey.SurveyReportType.SURVEY_STATS ]" class="btn btn-blue inverted export-modal-button" data-modalId="export-surveyStats-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.stats.survey.report"/>
            </g:link>
            <g:link mapping="politicianMassMailingSurveyReport"
                    params="[campaignId: campaign.id, surveyReportType: kuorum.web.commands.payment.survey.SurveyReportType.SURVEY_STATS, pdfFormat: true]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-surveyStats-modal">
                <span class="fal fa-file-pdf"></span>
                <g:message code="tools.massMailing.view.stats.survey.report.certificate"/>
            </g:link>
            <g:link mapping="politicianMassMailingSurveyReport"
                    params="[campaignId: campaign.id, surveyReportType: kuorum.web.commands.payment.survey.SurveyReportType.SURVEY_RAW_DATA]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-surveyStats-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.stats.survey.report.rawData"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.survey.SurveyRSDTO && (campaign.endDate == null || campaign.endDate.after(new Date()))}">
            <g:link mapping="surveyCloseNow" params="[campaignId: campaign.id]"
                    class="btn btn-blue inverted close-survey-modal-button" data-modalId="close-survey-modal"
                    elementId="close-survey-modal-button">
                <span class="fal fa-flag-checkered"></span>
                <g:message code="tools.massMailing.view.stats.survey.close"/>
            </g:link>
        </g:if>

        <g:if test="${campaign && campaign?.event && campaign.exportableData}">
            <g:link mapping="eventAssistanceReport" params="[campaignId: campaign.id, checkList: true]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-eventAssistants-modal">
                <span class="fal fa-file-pdf"></span>
                <g:message code="tools.massMailing.list.event.assistants.checkList"/>
            </g:link>
            <g:link mapping="eventAssistanceReport" params="[campaignId: campaign.id, checkList: false]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-eventAssistants-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.list.event.assistants.report"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign.exportableData && campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
            <g:link mapping="politicianMassMailingParticipatoryBudgetReport" params="[campaignId: campaign.id]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-proposalsList-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.participatoryBudget.report"/>
            </g:link>
        </g:if>
        <g:if test="${campaign && campaign.exportableData && campaign instanceof org.kuorum.rest.model.communication.contest.ContestRSDTO}">
            <g:link mapping="contestApplicationsReport" params="[campaignId: campaign.id]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-proposalsList-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.contest.report"/>
            </g:link>
            <g:link mapping="contestVotesReport" params="[campaignId: campaign.id]"
                    class="btn btn-blue inverted export-modal-button" data-modalId="export-proposalsList-modal">
                <span class="fal fa-file-excel"></span>
                <g:message code="tools.massMailing.view.contest.votes.report"/>
            </g:link>
        </g:if>
    </div>
</div>

<div id="report-files">
    <div class="report-files-list-actions">
        <g:set var="link" value="#"/>
        <g:if test="${newsletter}">
            <g:set var="link"
                   value="${g.createLink(mapping: 'politicianNewsletterRefreshReport', params: [campaignId: campaign.id])}"/>
        </g:if>
        <g:else>
            <g:set var="link"
                   value="${g.createLink(mapping: 'politicianCampaignRefreshReport', params: [campaignId: campaign.id])}"/>
        </g:else>
        <a href="${link}" class="files-list-btn-refresh">
            <span class="fal fa-sync"></span><g:message code="tools.massMailing.view.reports.reload"/>
        </a>
    </div>
    <div id="report-files-list">
        <g:render template="/newsletter/campaignTabs/campaignReportsList" model="[reportsList:reportsList]"/>
    </div>
</div>