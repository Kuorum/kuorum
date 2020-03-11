<g:set var="validateCommand" value="${new kuorum.web.commands.profile.DomainValidationCommand()}"/>
<formUtil:validateForm bean="${validateCommand}" form="modal-form-validate-user-domain"/>
<g:render template="/layouts/modals/modalDomainValidation_tabs"/>
<g:form mapping="profileDomainValidationByCensusValidate" method="POST" name="modal-form-validate-user-domain">
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
    <fieldset class="center modal-login-action-buttons">
        <button id="validateDomain-modal-form-button-id" class="btn btn-orange btn-lg">
            <g:message code="kuorum.web.commands.profile.DomainValidationCommand.modal.submit"/>
        </button>
    </fieldset>

</g:form>