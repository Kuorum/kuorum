<formUtil:validateForm bean="${command}" form="contestForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="contestForm" method="POST"
      data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType"
           value="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT ? 'SEND' : 'DRAFT'}"
           id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>
    <fieldset aria-live="polite" class="form-group">
        <h2 class="col-sm-offset-1"><g:message
                code="kuorum.web.commands.payment.contest.ContestDeadlinesCommand.dates.config"/></h2>

        <div class=" col-sm-offset-1 col-sm-4">
            <formUtil:input command="${command}" field="numWinnerApplications" showLabel="true"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <h2 class="col-sm-offset-1"><g:message
                code="kuorum.web.commands.payment.contest.ContestDeadlinesCommand.dates.title"/></h2>

        <div class="col-sm-offset-1 col-sm-4">
            <formUtil:date command="${command}" field="deadLineApplications" showLabel="true"/>
        </div>

        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineReview" showLabel="true"/>
        </div>

        <div class="col-sm-offset-1 col-sm-4">
            <formUtil:date command="${command}" field="deadLineVotes" showLabel="true"/>
        </div>

        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineResults" showLabel="true"/>
        </div>
    </fieldset>

    <g:render template="/campaigns/edit/stepButtons" model="[
            mappings: mappings,
            status  : status,
            command : command]"/>
</form>