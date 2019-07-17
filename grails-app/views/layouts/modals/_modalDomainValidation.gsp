<%@ page import="kuorum.web.commands.profile.DomainValidationCommand;" %>
<g:set var="validateCommand" value="${new kuorum.web.commands.profile.DomainValidationCommand()}"/>
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

                <formUtil:validateForm bean="${validateCommand}" form="modal-form-validate-user-domain"/>
                <g:form mapping="profileValidByDomainValidate" method="POST" name="modal-form-validate-user-domain">
                    <div class="form-group">
                        <formUtil:input
                                command="${validateCommand}"
                                field="ndi"
                                cssClass="form-control input-lg"
                                showLabel="false"
                                showCharCounter="false"
                                required="true"/>
                    </div>

                    <div class="form-group">
                        <formUtil:input
                                command="${validateCommand}"
                                field="postalCode"
                                showLabel="false"
                                cssClass="form-control input-lg"
                                required="true"/>
                    </div>
                    <div class="form-group">
                        <formUtil:date
                                command="${validateCommand}"
                                field="birthDate"
                                showLabel="true"
                                datePickerType="birthDate"/>
                    </div>
                    <div class="form-group center">
                        <button id="validateDomain-modal-form-button-id" class="btn btn-orange btn-lg">
                            <g:message code="kuorum.web.commands.profile.DomainValidationCommand.modal.submit"/>
                        </button>
                        <p class="loading" style="display: none"></p>
                        <p class="text-success" style="display: none">
                            <g:message code="kuorum.web.commands.profile.DomainValidationCommand.modal.success"/>
                            <span class="fal fa-check-circle"></span>
                        </p>
                    </div>

                </g:form>

            </div>
        </div>
    </div>
</div>