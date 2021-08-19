<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="campaignForm" />
<div class="box-steps container-fluid campaign-steps">
    <g:set var="mappings" value="${[step:'template',
        settings:'politicianMassMailingSettings',
        template:'politicianMassMailingTemplate',
        content:'politicianMassMailingContent',
        showResult: 'politicianCampaigns',
        next: 'politicianMassMailingContent']}"/>
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>
    <formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <fieldset class="form-group">
            <h4 for="contentType" class="col-sm-3 col-md-2 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.contentType.label"/>:</h4>
            <div class="col-sm-6 col-md-5">
                <formUtil:radioEnum command="${command}" field="contentType" labelCssClass="hide" values="[
                        org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO.NEWSLETTER,
                        org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO.HTML,
                        org.kuorum.rest.model.notification.campaign.NewsletterTemplateDTO.PLAIN_TEXT
                ]"/>
            </div>
        </fieldset>
        <g:render template="/campaigns/edit/stepButtons" model="[mappings:mappings, status:status, command: command]"/>
    </form>
</div>

<g:if test="${campaign.body}">
%{--SHOW MODAL WARN LOOSING DATES--}%
    <r:script>
        $(function(){
            $("input[name=contentType]").on("change",function(e){
                var originalValue = '${campaign.newsletter.template}';
                var newValue = $(this).val();
                if (originalValue != newValue){
                    $("#newsletterTemplateEdited").modal("show")
                }
            })

            $("#newsletterTemplateEditedButtonOk").on("click", function(e){
                e.preventDefault();
                $("#newsletterTemplateEdited").modal("hide")
            })
            $("#newsletterTemplateEditedButtonCancel").on("click", function(e){
                e.preventDefault();
                var originalValue = '${campaign.newsletter.template}';
                $('input[name="contentType"][value="' + originalValue + '"]').prop('checked', true);
                $("#newsletterTemplateEdited").modal("hide")
            })
        });
    </r:script>
    <div class="modal fade in" id="newsletterTemplateEdited" tabindex="-1" role="dialog" aria-labelledby="newsletterTemplateEditedTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                    </button>
                    <h4>
                        <g:message code="tools.massMailing.newsletterTemplateEdited.title"/>
                    </h4>
                </div>
                <div class="modal-body">
                    <p><g:message code="tools.massMailing.newsletterTemplateEdited.text"/> </p>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-grey-light btn-lg" id="newsletterTemplateEditedButtonCancel">
                        <g:message code="tools.massMailing.newsletterTemplateEdited.cancel"/>
                    </a>
                    <a href="#" class="btn btn-blue inverted btn-lg" id="newsletterTemplateEditedButtonOk">
                        <g:message code="tools.massMailing.newsletterTemplateEdited.button"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</g:if>
