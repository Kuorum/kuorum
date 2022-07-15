<r:require modules="datepicker, campaignForm"/>


<div class="box-steps container-fluid campaign-steps">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

    <formUtil:validateForm bean="${command}" form="contestApplicationEditEnvironment" dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="contestApplicationEditEnvironment" method="POST"
          data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <g:if test="${!campaign}">
            <fieldset aria-live="polite" class="form-group">
                <div class="col-sm-offset-1 col-sm-8">
                    <formUtil:input command="${command}" field="name" showLabel="true"/>
                </div>
            </fieldset>
        </g:if>
        <g:else>
            <input type="hidden" name="name" value="${campaign.title}"/>
        </g:else>
        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-offset-1 col-sm-8">
                <label for="cause"><g:message
                        code="kuorum.web.commands.payment.contest.contestApplicationEditEnvironmentCommand.cause.label"/>:</label>
                <select class="form-control input-lg" name="cause">
                    <option value="">---</option>
                    <g:each in="${contest.causes.sort { it.toLowerCase() }}" var="cause">
                        <option value="${cause}" ${cause == command.cause ? 'selected' : ''}>${cause}</option>
                    </g:each>
                </select>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="form-group">
            <div class="col-sm-offset-1 col-sm-4 col-xs-12">
                <formUtil:selectEnum command="${command}" field="activityType" showLabel="true" defaultEmpty="true"/>
            </div>

            <div class="col-sm-4 col-xs-12">
                <formUtil:selectEnum command="${command}" field="focusType" showLabel="true" defaultEmpty="true"/>
            </div>
        </fieldset>
        <g:render template="/campaigns/edit/stepButtons"
                  model="[mappings: mappings, status: status, command: command, numberRecipients: numberRecipients]"/>
    </form>

</div>