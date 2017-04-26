<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, newsletter" />
<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/creationSteps/threeSteps" model="[editReference: 'politicianMassMailingEdit2']"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <h4 for="contentType" class="col-sm-3 col-md-2 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.contentType.label"/>:</h4>
            <div class="col-sm-6 col-md-5">
                <formUtil:radioEnum command="${command}" field="contentType" labelCssClass="hide" deleteOptions="[org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO.DEBATE, org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO.POST]"/>
            </div>
        </fieldset>

        <fieldset class="form-group">
            <div class="col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-8 form-control-campaign">
                <ul class="form-final-options">
                    <li>
                        <a href="#" id="save-draft-campaign">
                            <g:message code="tools.massMailing.saveDraft"/>
                        </a>
                    </li>
                    <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="politicianMassMailingEdit3"><g:message code="tools.massMailing.next"/></a></li>
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