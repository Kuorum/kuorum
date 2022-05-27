<!-- MODAL AUTH PROFILE CHANGES -->
<div class="modal fade in" id="modalEditScheduled" tabindex="-1" role="dialog" aria-labelledby="modalEditScheduledTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4>
                    <g:message code="tools.massMailing.editCampaignModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.editCampaignModal.text"/></p>
                <fieldset aria-live="polite" class="text-right">
                    <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal"
                       id="modalEditScheduledButtonClose">
                        <g:message code="tools.massMailing.editCampaignModal.cancel"/>
                    </a>
                    <a href="#" class="btn btn-blue inverted btn-lg" id="modalEditScheduledButtonOk">
                        <g:message code="tools.massMailing.editCampaignModal.pause"/>
                    </a>
                </fieldset>

            </div>
        </div>
    </div>
</div>