<formUtil:validateForm bean="${command}" form="contestForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="contestForm" method="POST"
      data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType"
           value="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT ? 'SEND' : 'DRAFT'}"
           id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineApplications" class="col-sm-4 control-label"><g:message
                code="kuorum.web.commands.payment.contest.ContestDeadlinesCommand.deadLineApplications.label"/>:</label>

        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineApplications"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineReview" class="col-sm-4 control-label"><g:message
                code="kuorum.web.commands.payment.contest.ContestDeadlinesCommand.deadLineTechnicalReview.label"/>:</label>

        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineReview"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineVotes" class="col-sm-4 control-label"><g:message
                code="kuorum.web.commands.payment.contest.ContestDeadlinesCommand.deadLineVotes.label"/>:</label>

        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineVotes"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineResults" class="col-sm-4 control-label"><g:message
                code="kuorum.web.commands.payment.contest.ContestDeadlinesCommand.deadLineFinalReview.label"/>:</label>

        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineResults"/>
        </div>
    </fieldset>

    <g:render template="/campaigns/edit/stepButtons" model="[
            mappings: mappings,
            status  : status,
            command : command]"/>
</form>