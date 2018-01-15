<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="newsletter" />
<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/steps/threeSteps" model="[editReference: 'politicianMassMailingTemplate']"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <h4 for="contentType" class="col-sm-3 col-md-2 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.contentType.label"/>:</h4>
            <div class="col-sm-6 col-md-5">
                <formUtil:radioEnum command="${command}" field="contentType" labelCssClass="hide" deleteOptions="[
                        org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO.EVENT,
                        org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO.DEBATE,
                        org.kuorum.rest.model.notification.campaign.CampaignTemplateDTO.POST
                ]"/>
            </div>
        </fieldset>

        <fieldset class="form-group">
            <div class="col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-8 form-control-campaign">
                <ul class="form-final-options">
                    <li>
                        <a href="#" id="save-draft-campaign" data-redirectLink="politicianCampaigns">
                            <g:message code="tools.massMailing.saveDraft"/>
                        </a>
                    </li>
                    <li><a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="politicianMassMailingContent"><g:message code="tools.massMailing.next"/></a></li>
                </ul>
            </div>
        </fieldset>
    </form>
</div>

<g:if test="${campaign.body}">
%{--SHOW MODAL WARN LOOSING DATES--}%
    <r:script>
        $(function(){
            $("input[name=contentType]").on("change",function(e){
                var originalValue = '${campaign.template}';
                var newValue = $(this).val();
                if (originalValue != newValue){
                    $("#campaignTemplateEdited").modal("show")
                }
            })

            $("#campaignTemplateEditedButtonOk").on("click", function(e){
                e.preventDefault();
                $("#campaignTemplateEdited").modal("hide")
            })
            $("#campaignTemplateEditedButtonCancel").on("click", function(e){
                e.preventDefault();
                var originalValue = '${campaign.template}';
                $('input[name="contentType"][value="' + originalValue + '"]').prop('checked', true);
                $("#campaignTemplateEdited").modal("hide")
            })
        });
    </r:script>
    <div class="modal fade in" id="campaignTemplateEdited" tabindex="-1" role="dialog" aria-labelledby="campaignTemplateEditedTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                    </button>
                    <h4>
                        <g:message code="tools.massMailing.campaignTemplateEdited.title"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <p><g:message code="tools.massMailing.campaignTemplateEdited.text"/> </p>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-grey-light btn-lg" id="campaignTemplateEditedButtonCancel">
                        <g:message code="tools.massMailing.campaignTemplateEdited.cancel"/>
                    </a>
                    <a href="#" class="btn btn-blue inverted btn-lg" id="campaignTemplateEditedButtonOk">
                        <g:message code="tools.massMailing.campaignTemplateEdited.button"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</g:if>
