<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, newsletter" />

<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/steps/threeSteps" model="[editReference: 'politicianMassMailingSettings']"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.form.genericError')}">
        <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>
        <input type="hidden" name="campaignId" value="${campaignId}"/>

        <fieldset class="form-group">
            <label for="campaignName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.campaignName.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <formUtil:input
                        command="${command}"
                        field="campaignName"
                        placeHolder="${g.message(code: 'kuorum.web.commands.payment.massMailing.MassMailingCommand.campaignName.placeHolder')}"
                />
            </div>
        </fieldset>

        <g:render template="/newsletter/filter" model="[command: command, filters: filters, currentFilter: campaign?.newsletter?.filter,totalContacts:totalContacts]"/>

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
        <g:render template="/newsletter/form/formGroupCampaignTags" model="[command:command, events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK, TrackingMailStatusRSDTO.TRACK_LINK, TrackingMailStatusRSDTO.ANSWERED]]"/>
    </div>
        %{--<g:render template="/newsletter/form/formGroupCampaignTags" model="[command:command, events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK]]"/>--}%
        <fieldset class="form-group">
            <div class="col-sm-10 col-sm-offset-2 col-md-4 col-md-offset-8 form-control-campaign">
                <ul class="form-final-options">
                    <li>
                        <a href="#" id="save-draft-campaign" data-redirectLink="politicianCampaigns" class="btn btn-grey-light">
                            <g:message code="tools.massMailing.saveDraft"/>
                        </a>
                    </li>
                    <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="politicianMassMailingTemplate"><g:message code="tools.massMailing.next"/></a></li>
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
            </div>
        </div>
    </div>
</div>