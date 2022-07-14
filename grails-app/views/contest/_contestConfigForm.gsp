<formUtil:validateForm bean="${command}" form="contestForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="contestForm" method="POST"
      data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType"
           value="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT ? 'SEND' : 'DRAFT'}"
           id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>
    <fieldset aria-live="polite" class="form-group">
        <div class=" col-sm-offset-1 col-sm-4">
            <formUtil:input command="${command}" field="numWinnerApplications" showLabel="true"/>
        </div>
    </fieldset>


    <g:render template="/campaigns/edit/stepButtons" model="[
            mappings: mappings,
            status  : status,
            command : command]"/>
</form>