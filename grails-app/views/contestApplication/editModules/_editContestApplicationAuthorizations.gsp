<r:require modules="datepicker, campaignForm"/>


<div class="box-steps container-fluid campaign-steps">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

    <formUtil:validateForm bean="${command}" form="contestApplicationEditScope" dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="contestApplicationEditScope" method="POST"
          data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="sendType"
               value="${kuorum.web.commands.payment.CampaignContentCommand.CAMPAIGN_SEND_TYPE_DRAFT}"
               id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>


        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-offset-1 col-xs-10">
                <formUtil:checkBox command="${command}" field="authorizedAgent" showLabel="true" defaultEmpty="true"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-offset-1 col-xs-10">
                <formUtil:checkBox command="${command}" field="acceptedLegalBases" showLabel="true" defaultEmpty="true"
                                   label="${g.message(code: 'kuorum.web.commands.payment.contest.ContestApplicationAuthorizationsCommand.acceptedLegalBases.label', args: [contest.getTitle()])}"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-offset-1 col-xs-10">
                <formUtil:checkBox command="${command}" field="imageRights" showLabel="true" defaultEmpty="true"
                                   label="${g.message(code: 'kuorum.web.commands.payment.contest.ContestApplicationAuthorizationsCommand.imageRights.label', args: [_domainName, contest.getTitle()])}"/>
            </div>
        </fieldset>
        <g:render template="/campaigns/edit/stepButtons"
                  model="[mappings: mappings, status: status, command: command, numberRecipients: numberRecipients]"/>
    </form>

</div>