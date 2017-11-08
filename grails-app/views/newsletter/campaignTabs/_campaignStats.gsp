<h2 class="sr-only"><g:message code="tools.massMailing.view.stats"/></h2>
<div class="actions">
    <g:if test="${campaign.template == org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO.DEBATE}">
        <g:link mapping="politicianMassMailingTrackEventsReport" params="[campaignId:21]" class="btn btn-blue inverted" elementId="exportDebateReport">
            <span class="fa fa-file-excel-o"></span>
            <g:message code="tools.massMailing.view.stats.debate.report"/>
        </g:link>
    </g:if>
</div>
<ul class="activity">
    <li class="posts">
        <span class='recip-number'><campaignUtil:campaignsSent campaign="${campaign}"/></span>
        <g:message code="tools.massMailing.list.recipients"/>
    </li>
    <li class="posts">
        <campaignUtil:openRate campaign="${campaign}"/>
        <g:message code="tools.massMailing.list.opens"/>
    </li>
    <li class="posts">
        <campaignUtil:clickRate campaign="${campaign}"/>
        <g:message code="tools.massMailing.list.click"/>
    </li>
</ul>