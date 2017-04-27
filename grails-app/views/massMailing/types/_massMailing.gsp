<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, newsletter" />

<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/steps/threeSteps" model="[editReference: 'politicianMassMailingSettings']"/>
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
                        field="campaignName"
                        placeHolder="${g.message(code: 'kuorum.web.commands.payment.massMailing.MassMailingCommand.campaignName.placeHolder')}"
                />
            </div>
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
                    <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="politicianMassMailingTemplate"><g:message code="tools.massMailing.next"/></a></li>
                </ul>
            </div>
        </fieldset>
    </form>
</div>

<!-- MODAL TEST ADVISE -->
<div class="modal fade in" id="sendTestModal" tabindex="-1" role="dialog" aria-labelledby="sendTestModalTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="sendTestModalTitle">
                    <g:message code="tools.massMailing.sendTestModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.sendTestModal.text"/></p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="sendTestModalButonOk">
                    <g:message code="tools.massMailing.sendTestModal.button"/>
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