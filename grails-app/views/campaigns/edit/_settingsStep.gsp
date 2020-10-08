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
            <g:if test="${options.debatable}">
                <fieldset class="form-group fieldset-check-box">
                    %{--<label for="campaignName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.debate.CampaignSettingsCommand.campaignName.label"/>:</label>--}%
                    <div class="col-md-offset-1 col-sm-8 col-md-7">
                        <formUtil:checkBox command="${command}" field="debatable" disabled="${command.debatable!=null}"/>
                    </div>
                </fieldset>
            </g:if>
            <g:if test="${options.showCampaignDateLimits}">
                <fieldset class="form-group" id="campaign-date-range">
                    <label for="startDate" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.startDate.label"/>:</label>
                    <div class="col-sm-4 col-md-4">
                        <formUtil:date command="${command}" field="startDate" time="true"/>
                    </div>
                    <label for="endDate" class="col-sm-1 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.endDate.label"/>:</label>
                    <div class="col-sm-4 col-md-4">
                        <formUtil:date command="${command}" field="endDate" time="true"/>
                    </div>
                    <r:script>
                        $(function(){
                            $("#endDate").rules('add', { greaterThan: "#startDate" , messages:{greaterThan:"${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.endDate.greaterThan')}"} });
                        });
                    </r:script>
                </fieldset>
            </g:if>
            <g:if test="${!options.hideValidateOption}">
                <fieldset class="form-group fieldset-check-box">
                    <label for="validationType" class="col-sm-2 col-md-1 control-label">
                        <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.validationType.label.info')}"></span>
                        <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.validationType.label.left"/>:
                    </label>
                    <div class="col-sm-4 col-md-4">
                        <formUtil:selectEnum command="${command}" field="validationType" disabled="${!domainValidation}" showLabel="false"/>
                    </div>
                </fieldset>
            </g:if>
            <g:if test="${options.showSurveyCustomFields}">
                <div class="campaign-super-admin">
                    <fieldset class="form-group fieldset-check-box">
                        <label for="anonymous" class="col-sm-2 col-md-1 control-label">
                            <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.anonymous.label.info')}"></span>
                            <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.anonymous.label.left"/>:
                        </label>
                        <div class="col-sm-8 col-md-7">
                            <formUtil:checkBox command="${command}" field="anonymous" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
                        </div>
                    </fieldset>
                    <fieldset class="form-group fieldset-check-box">
                        <label for="signVotes" class="col-sm-2 col-md-1 control-label">
                            <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.signVotes.label.info')}"></span>
                            <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.signVotes.label.left"/>:
                        </label>
                        <div class="col-sm-8 col-md-7">
                            <formUtil:checkBox command="${command}" field="signVotes" disabled="${!grails.plugin.springsecurity.SpringSecurityUtils.ifAllGranted("ROLE_SUPER_ADMIN")}"/>
                        </div>
                    </fieldset>
                </div>
            </g:if>

            <g:if test="${options.hiddeVotesFlag}">
                <fieldset class="form-group fieldset-check-box">
                    <label for="hideResultsFlag" class="col-sm-2 col-md-1 control-label">
                        <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.hideResults.label.info')}"></span>
                        <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.hideResults.label.left"/>:
                    </label>
                    <div class="col-sm-8 col-md-7">
                        <formUtil:checkBox command="${command}" field="hideResultsFlag" label="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.hideResults.label')}" />
                    </div>
                </fieldset>
            </g:if>
            <g:elseif test="${options.hiddeCommentsFlag}">
                <fieldset class="form-group fieldset-check-box">
                    <label for="hideResultsFlag" class="col-sm-2 col-md-1 control-label">
                        <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.hideComments.label.left"/>:
                    </label>
                    <div class="col-sm-8 col-md-7">
                        <formUtil:checkBox command="${command}" field="hideResultsFlag" label="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.hideComments.label')}"/>
                    </div>
                </fieldset>
            </g:elseif>
            <fieldset class="form-group fieldset-check-box">
                <label for="newsletterCommunication" class="col-sm-2 col-md-1 control-label">
                    <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.newsletterCommunication.label.left"/>:
                </label>
                <div class="col-sm-8 col-md-7">
                    <formUtil:checkBox command="${command}" field="newsletterCommunication" disabled="${campaign != null && campaign.published}"/>
%{--                    <formUtil:checkBox command="${command}" field="newsletterCommunication"/>--}%
                </div>
            </fieldset>
            <fieldset class="form-group fieldset-check-box">
                <label for="groupValidation" class="col-sm-2 col-md-1 control-label">
                    <g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.groupValidation.label.left"/>:
                </label>
                <div class="col-sm-8 col-md-7">
                    <formUtil:checkBox command="${command}" field="groupValidation"/>
                </div>
            </fieldset>
            <div id="filter-contact-selector" style="display: none">
                <g:render template="/newsletter/filter" model="[command: command, filters: filters,anonymousFilter: anonymousFilter, totalContacts: totalContacts, hideSendTestButton: true, showOnly:campaign?.newsletter?.status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT]"/>
            </div>
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
