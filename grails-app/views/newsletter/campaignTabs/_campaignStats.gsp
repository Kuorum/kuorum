<h2 class="sr-only"><g:message code="tools.massMailing.view.stats"/></h2>
<div class="actions">
    <g:if test="${campaign && campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
        <g:link mapping="politicianMassMailingDebateStatsReport" params="[debateId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-debateStats-modal">
            <span class="fa fa-file-excel-o"></span>
            <g:message code="tools.massMailing.view.stats.debate.report"/>
        </g:link>
    </g:if>
</div>
<ul class="activity">
    <li class="posts">
        <span class='recip-number'><campaignUtil:campaignsSent campaign="${newsletter}"/></span>
        <g:message code="tools.massMailing.list.recipients"/>
    </li>
    <li class="posts">
        <campaignUtil:openRate campaign="${newsletter}"/>
        <g:message code="tools.massMailing.list.opens"/>
    </li>
    <li class="posts">
        <campaignUtil:clickRate campaign="${newsletter}"/>
        <g:message code="tools.massMailing.list.click"/>
    </li>
    <li class="posts">
        <campaignUtil:unsubscribeRate campaign="${newsletter}"/>
        <g:message code="tools.massMailing.list.unsubscribe"/>
    </li>
</ul>



<div id="export-debateStats-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="exportDebateStatsTitle" aria-hidden="true">
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
                <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message code="modal.exportedDebateStats.close"/></a>
            </div>
        </div>
    </div>
</div>