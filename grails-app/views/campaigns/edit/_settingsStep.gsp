<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, postForm, debateForm" />

<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/steps/twoSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <label for="campaignName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.debate.DebateSettingsCommand.campaignName.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <formUtil:input command="${command}" field="campaignName"/>
            </div>
        </fieldset>

        <g:render template="/newsletter/filter" model="[command: command, filters: filters,anonymousFilter: anonymousFilter, totalContacts: totalContacts, hideSendTestButton: true]"/>


        <fieldset class="form-group" id="advanced-features-section">
            %{--<label for="advanced-feature" class="col-sm-2 col-md-1 control-label">Advanced features:</label>--}%
            <div class="col-sm-offset-1 col-sm-10">
                <a href=""><g:message code="tools.massMailing.advanced-features.title"/><span class="fa fa-angle-down"></span></a>
                <hr/>
            </div>
        </fieldset>

        <div id="advanced-features">
            <g:render template="/newsletter/form/formGroupCampaignTags" model="[command:command, events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK,TrackingMailStatusRSDTO.POST_LIKE]]"/>
            <g:render template="/newsletter/form/formGroupCampaignCauses" model="[command:command]"/>
        </div>
        <fieldset class="buttons">
            <div class="text-right">
                <ul class="form-final-options">
                    <li>
                        <a href="#" id="save-draft-debate" data-redirectLink="politicianCampaigns">
                            <g:message code="tools.massMailing.saveDraft"/>
                        </a>
                    </li>
                    <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="${mappings.next}"><g:message code="tools.massMailing.next"/></a></li>
                </ul>
            </div>
        </fieldset>
    </form>
</div>

<!-- WARN USING ANONYMOUS FILTER -->
<div class="modal fade in" id="campaignWarnFilterEdited" tabindex="-1" role="dialog" aria-labelledby="campaignWarnFilterEditedTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
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
