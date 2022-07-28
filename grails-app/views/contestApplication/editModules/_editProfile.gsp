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
            <h2 class="col-sm-offset-1"><g:message code="tools.campaign.new.steps.profile.org-data.title"/></h2>

            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:input command="${command}" field="name" showLabel="true"/>
            </div>

            <div class="col-sm-4 col-xs-12">
                <formUtil:input command="${command}" field="nid" showLabel="true"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-offset-1 col-sm-10  col-xs-12">
                <formUtil:textArea command="${command}" field="bio" showLabel="true"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <h2 class="col-sm-offset-1"><g:message code="tools.campaign.new.steps.profile.contact.title"/></h2>

            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:input command="${command}" field="phone" showLabel="true"/>
            </div>

            <div class="col-sm-4 col-xs-12">
                <formUtil:input command="${command}" field="email" showLabel="true" disabled="true"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <h2 class="col-sm-offset-1"><g:message code="tools.campaign.new.steps.profile.files.title"/></h2>

            <div class="col-sm-offset-1"><p><g:message code="tools.campaign.new.steps.profile.files.description"/></p>
            </div>

            <div class="col-sm-offset-1 col-sm-10 col-xs-12">
                <formUtil:uploadContactFiles contact="${contact}"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <h2 class="col-sm-offset-1"><g:message code="page.title.profile.socialNetworks"/></h2>

            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="facebook" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>

            <div class="col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="twitter" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>

            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="linkedIn" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>

            <div class="col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="youtube" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>

            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="blog" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>

            <div class="col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="instagram" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>

            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:input command="${command.social}" field="officialWebSite" showLabel="true" defaultEmpty="true"
                                prefixFieldName="social."/>
            </div>
        </fieldset>
        <g:render template="/campaigns/edit/stepButtons"
                  model="[mappings: mappings, status: status, command: command, numberRecipients: numberRecipients]"/>
    </form>

</div>