<!-- Modal registro/validation -->
<div class="modal fade modal-domain-validation" id="domain-validation" tabindex="-1" role="dialog" aria-labelledby="modal-domain-validation-title" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <h4><g:message code="kuorum.web.commands.profile.DomainValidationCommand.modal.title"/></h4>
                <h4 class="sr-only" id="modal-domain-validation-title">Registro / Validar usuario</h4>
            </div>
            <div class="modal-body">
                <div class="modal-domain-validation-census">
                    <g:render template="/layouts/modals/modalDomainValidation_CesusForm"/>
                </div>
                <div class="modal-domain-validation-phone">
                    <g:render template="/layouts/modals/modalDomainValidation_PhoneForm"/>
                </div>
                <fieldset class="center modal-domain-validation-notifications">
                    <p class="loading" style="display: none"><span class="fas fa-spinner fa-spin"></span> </p>
                    <p class="text-success" style="display: none">
                        <g:message code="kuorum.web.commands.profile.DomainValidationCommand.modal.success"/>
                        <span class="fal fa-check-circle"></span>
                    </p>
                    <p class="text-danger" style="display: none">
                        <span class="fal fa-exclamation-circle"></span>
                        <span class="text-error-data">error</span>
                    </p>
                </fieldset>
            </div>
        </div>
    </div>
</div>