<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>
<r:require modules="datepicker, postForm, debateForm" />

<div class="box-steps container-fluid campaign-steps">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings, attachEvent:attachEvent]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <label for="campaignName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.campaignName.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <formUtil:input command="${command}" field="campaignName"/>
            </div>
        </fieldset>

        <g:render template="/newsletter/filter" model="[command: command, filters: filters,anonymousFilter: anonymousFilter, totalContacts: totalContacts, hideSendTestButton: true, showOnly:campaign?.newsletter?.status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT]"/>

        <g:if test="${options.debatable}">
            <fieldset class="form-group fieldset-check-box">
                %{--<label for="campaignName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.debate.CampaignSettingsCommand.campaignName.label"/>:</label>--}%
                <div class="col-md-offset-1 col-sm-8 col-md-7">
                    <formUtil:checkBox command="${command}" field="debatable" disabled="${command.debatable!=null}"/>
                </div>
            </fieldset>
        </g:if>
        <g:render template="/newsletter/form/formGroupCampaignCauses" model="[command:command, options:options]"/>

        <fieldset class="form-group" id="advanced-features-section">
            %{--<label for="advanced-feature" class="col-sm-2 col-md-1 control-label">Advanced features:</label>--}%
            <div class="col-sm-offset-1 col-sm-10">
                <a href="">
                    <g:message code="tools.massMailing.advanced-features.title"/>
                    <span class="go-up stack">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fas fa-circle fa-stack-1x"></span>
                            <span class="fal fa-angle-down fa-stack-1x fa-inverse"></span>
                        </span>
                    </span>
                </a>

            </div>
        </fieldset>

        <div id="advanced-features">
            <g:if test="${!options.hideValidateOption}">
                <fieldset class="form-group fieldset-check-box">
                    <label for="checkValidation" class="col-sm-2 col-md-1 control-label">
                        <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.checkValidation.label.info')}"></span>
                        <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.checkValidation.label.left"/>:
                    </label>
                    <div class="col-sm-8 col-md-7">
                        <formUtil:checkBox command="${command}" field="checkValidation" disabled="${!domainValidation}"/>
                    </div>
                </fieldset>
            </g:if>
            <fieldset class="form-group fieldset-check-box">
                <label for="hideResults" class="col-sm-2 col-md-1 control-label">
                    <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.hideResults.label.info')}"></span>
                    <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.hideResults.label.left"/>:
                </label>
                <div class="col-sm-8 col-md-7">
                    <formUtil:checkBox command="${command}" field="hideResults"/>
                </div>
            </fieldset>
            <g:render template="/newsletter/form/formGroupCampaignTags" model="[command:command, events:events, editable:campaign== null || !campaign.published]"/>
        </div>
        <g:render template="/campaigns/edit/stepButtons" model="[mappings:mappings, status:status, command: command]"/>
    </form>
</div>

<!-- WARN USING ANONYMOUS FILTER -->
<div class="modal fade in" id="campaignWarnFilterEdited" tabindex="-1" role="dialog" aria-labelledby="campaignWarnFilterEditedTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4>
                    <g:message code="tools.massMailing.warnFilterEdited.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.warnFilterEdited.text"/> </p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="campaignWarnFilterEditedButtonOk">
                    <g:message code="tools.massMailing.warnFilterEdited.button"/>
                </a>
                <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal" id="campaignWarnFilterEditedButtonClose">
                    <g:message code="tools.massMailing.warnFilterEdited.cancel"/>
                </a>

            </div>
        </div>
    </div>
</div>
