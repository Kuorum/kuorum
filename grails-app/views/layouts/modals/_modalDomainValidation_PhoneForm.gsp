<%@ page import="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand" %>
<g:set var="validatePhoneCommand" value="${new kuorum.web.commands.profile.DomainUserPhoneValidationCommand()}"/>
<g:set var="validatePhoneCodeCommand" value="${new kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand()}"/>
<g:render template="/layouts/modals/modalDomainValidation_tabs"/>
<div class="modal-domain-validation-phone-step1">
    <formUtil:validateForm bean="${validatePhoneCommand}" form="modal-form-validate-phone-user-domain"/>
    <g:form mapping="profileValidByDomainValidate" method="POST" name="modal-form-validate-phone-user-domain">
        <div class="form-group">
            <div class="form-group form-group-phone">
                <formUtil:selectPhonePrefix
                        command="${validatePhoneCommand}"
                        field="phoneNumberPrefix"
                        showLabel="false"
                        placeHolder=""
                        cssClass="form-control input-lg"
                        required="true"
                        />
                <formUtil:input
                        command="${validatePhoneCommand}"
                        field="phoneNumber"
                        showLabel="false"
                        placeHolder=""
                        cssClass="form-control input-lg"
                        required="true"/>
            </div>
        </div>


        <fieldset class="center modal-login-action-buttons">
            <button id="validatePhoneDomain-modal-form-button-id" class="btn btn-orange">
                <g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.modal.submit"/>
            </button>
        </fieldset>
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
        <fieldset class="modal-login-action-buttons">
            <a href="#" id="validatePhoneCodeDomain-modal-form-button-back" class="btn btn-grey-light col-xs-6"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.back"/></a>
            <button id="validatePhoneCodeDomain-modal-form-button-id" class="btn btn-orange col-xs-6">
                <g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.submit"/>
            </button>
        </fieldset>

    </g:form>
</div>