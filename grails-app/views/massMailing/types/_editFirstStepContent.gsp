<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, newsletter" />

<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/creationSteps/threeSteps" model="[editReference: 'politicianMassMailingEdit1']"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.form.genericError')}">
        <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <label for="campaignName" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.campaignName.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <formUtil:input
                        command="${command}"
                        field="campaignName"/>
            </div>

            %{--<label for="subject" class="col-sm-2 col-md-1 control-label">Subject:</label>--}%
            %{--<div class="col-sm-8 col-md-7">--}%
            %{--<input type="text" class="form-control input-lg" id="subject" placeholder="It’s time to build a better country for everybody" equired aria-required="true">--}%
            %{--</div>--}%
        </fieldset>

        <g:render template="/massMailing/filter" model="[command: command, filters: filters, anonymousFilter: anonymousFilter]"/>

        <g:render template="/massMailing/form/formGroupCampaignTags" model="[command:command, events:[TrackingMailStatusRSDTO.OPEN,TrackingMailStatusRSDTO.CLICK]]"/>
        <fieldset class="form-group">
            <div class="col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-8 form-control-campaign">
                <ul class="form-final-options">
                    <li>
                        <a href="#" id="save-draft-campaign">
                            <g:message code="tools.massMailing.saveDraft"/>
                        </a>
                    </li>
                    <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="politicianMassMailingEdit2"><g:message code="tools.massMailing.next"/></a></li>
                </ul>
            </div>
        </fieldset>
    </form>
</div>

<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignConfirmTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="campaignConfirmTitle">
                    <g:message code="tools.massMailing.confirmationModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <a href="#" class="btn btn-blue inverted btn-lg" id="saveCampaignBtn">
                    <g:message code="tools.massMailing.confirmationModal.button"/>
                </a>
            </div>
        </div>
    </div>
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
            </div>
        </div>
    </div>
</div>