
<formUtil:validateForm bean="${command}" form="districtParticipatoryBudgetForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="districtParticipatoryBudgetForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType" value="${status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT?'SEND':'DRAFT'}" id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineProposals" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.ParticipatoryBudgetDeadlinesCommand.deadLineProposals.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineProposals"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineTechnicalReview" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.ParticipatoryBudgetDeadlinesCommand.deadLineTechnicalReview.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineTechnicalReview"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineVotes" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.ParticipatoryBudgetDeadlinesCommand.deadLineVotes.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineVotes"/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group">
        <label for="deadLineFinalReview" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.ParticipatoryBudgetDeadlinesCommand.deadLineFinalReview.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineFinalReview"/>
        </div>
    </fieldset>

    <g:render template="/campaigns/edit/stepButtons" model="[
            saveAndSentButtons:false,
            mappings:mappings,
            status:status,
            command: command]"/>
</form>