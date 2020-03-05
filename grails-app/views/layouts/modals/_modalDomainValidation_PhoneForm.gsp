<%@ page import="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand" %>
<g:set var="validatePhoneCommand" value="${new kuorum.web.commands.profile.DomainUserPhoneValidationCommand()}"/>
<g:set var="validatePhoneCodeCommand" value="${new kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand()}"/>
<div class="modal-domain-validation-phone-step1">
    <formUtil:validateForm bean="${validatePhoneCommand}" form="modal-form-validate-phone-user-domain"/>
    <g:form mapping="profileValidByDomainValidate" method="POST" name="modal-form-validate-phone-user-domain">
        <div class="form-group">
            <div class="form-group">
                <formUtil:input
                        command="${validatePhoneCommand}"
                        field="phoneNumber"
                        showLabel="true"
                        cssClass="form-control input-lg"
                        required="true"/>
            </div>
        </div>


        <div class="form-group center">
            <button id="validatePhoneDomain-modal-form-button-id" class="btn btn-orange btn-lg">
                <g:message code="kuorum.web.commands.profile.DomainValidationCommand.modal.submit"/>
            </button>
            <p class="loading" style="display: none"></p>
        </div>
    </g:form>
</div>

<div class="modal-domain-validation-phone-step2">
    <formUtil:validateForm bean="${validatePhoneCodeCommand}" form="modal-form-validate-phone-code-user-domain"/>
    <g:form mapping="profileValidByDomainValidate" method="POST" name="modal-form-validate-phone-code-user-domain">
        <input type="hidden" name="phoneHash" id="phoneHash" required="" value="">
        <div class="form-group">
            <formUtil:input
                    command="${validatePhoneCodeCommand}"
                    field="phoneCode"
                    showLabel="true"
                    cssClass="form-control input-lg"
                    required="true"/>
        </div>
        <div class="form-group center">
            <button id="validatePhoneCodeDomain-modal-form-button-id" class="btn btn-orange btn-lg">
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