<%@ page import="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand" %>
<g:set var="validateCustomCodeCommand" value="${new kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand()}"/>
<g:render template="/layouts/modals/modalDomainValidation_tabs"/>
<formUtil:validateForm bean="${validateCustomCodeCommand}" form="modal-form-validate-customCode-user-domain"/>
<g:form mapping="profileDomainValidationByCodeValidate" method="POST" name="modal-form-validate-customCode-user-domain">
    <div class="form-group">
        <div class="form-group">
            <formUtil:input
                    command="${validateCustomCodeCommand}"
                    field="customCode"
                    showLabel="true"
                    placeHolder=""
                    cssClass="form-control input-lg"
                    required="true"
                    />
        </div>
    </div>

    <fieldset class="center modal-login-action-buttons">
        <button id="validateCustomCodeDomain-modal-form-button-id" class="btn btn-orange">
            <g:message code="kuorum.web.commands.profile.DomainUserCustomCodeValidationCommand.modal.submit"/>
        </button>
    </fieldset>
</g:form>
