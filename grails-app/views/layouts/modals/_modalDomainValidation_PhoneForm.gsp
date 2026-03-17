<%@ page import="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand" %>
<g:set var="validatePhoneCommand" value="${new kuorum.web.commands.profile.DomainUserPhoneValidationCommand()}"/>
<g:set var="validatePhoneCodeCommand" value="${new kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand()}"/>
<g:set var="isWhatsAppSendingEnabled"  value="${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.getValidationWhatsApp()}"/>


<g:render template="/layouts/modals/modalDomainValidation_tabs"/>
<div class="modal-domain-validation-phone-step1">
    <formUtil:validateForm bean="${validatePhoneCommand}" form="modal-form-validate-phone-user-domain"/>
    <g:form mapping="domainValidationByPhoneSendSms" method="POST" name="modal-form-validate-phone-user-domain">
        <div class="form-group modal-domain-validation-phone-step1-predefinedPhone">
            <p class="modal-domain-validation-phone-step1-predefinedPhone-text">
                <g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.modal.predefinedPhone"/>:
                <span class="modal-domain-validation-phone-step1-predefinedPhone-phone">*****</span>
            </p>
        </div>

        <div class="form-group modal-domain-validation-phone-step1-inputPhone">
            <div class="form-group form-group-phone">
                <formUtil:selectPhonePrefix
                        command="${validatePhoneCommand}"
                        field="phoneNumberPrefix"
                        showLabel="true"
                        ariaLabelCustom="${message(code:'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumberPrefix.ariaLabel')}"
                        placeHolder=""
                        cssClass="form-control input-lg"
                        required="true"
                />
                <formUtil:input
                        command="${validatePhoneCommand}"
                        field="phoneNumber"
                        showLabel="false"
                        ariaLabelCustom="${message(code:'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber.ariaLabel')}"
                        placeHolder=""
                        cssClass="form-control input-lg"
                        type="tel"
                        required="true"/>
            </div>
            <div class="form-group form-group-phone second-phone">
                <formUtil:selectPhonePrefix
                        command="${validatePhoneCommand}"
                        field="phoneNumberPrefix2"
                        showLabel="true"
                        ariaLabelCustom="${message(code:'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumberPrefix2.ariaLabel')}"
                        placeHolder=""
                        cssClass="form-control input-lg"
                        required="true"
                />
                <formUtil:input
                        command="${validatePhoneCommand}"
                        field="phoneNumber2"
                        showLabel="true"
                        ariaLabelCustom="${message(code:'kuorum.web.commands.profile.DomainUserPhoneValidationCommand.phoneNumber2.ariaLabel')}"
                        placeHolder=""
                        cssClass="form-control input-lg"
                        type="tel"
                        required="false"/>
            </div>
        </div>


        <fieldset aria-live="polite" class="center modal-login-action-buttons">
            <button id="validatePhoneDomain-modal-form-button-id" data-recaptcha="" data-callback="captchaSolvedCallback" data-whatsapp-enabled="${isWhatsAppSendingEnabled}" class="btn btn-orange g-recaptcha">
                <g:message code="kuorum.web.commands.profile.DomainUserPhoneValidationCommand.modal.submit"/>
            </button>
        </fieldset>
    </g:form>
</div>

<div class="modal-domain-validation-phone-step2"
     data-msg-help-whatsapp="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.WhatsApp.helpBlock')}"
     data-msg-help-sms="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.helpBlock')}"
     data-msg-link-whatsapp="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.whatsAppNotReceived')}"
     data-msg-link-sms="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.smsNotReceived')}"
     data-msg-error-whatsapp="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.WhatsApp.nullable')}"
     data-msg-error-sms="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.nullable')}"

     data-msg-send-whatsapp="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.sendWhatsApp')}"
     data-msg-resend-whatsapp="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.resendWhatsApp')}"
     data-msg-send-sms="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.sendSms')}"
     data-msg-resend-sms="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.resendSms')}">

    <formUtil:validateForm bean="${validatePhoneCodeCommand}" form="modal-form-validate-phone-code-user-domain"/>
    <g:form mapping="domainValidationByPhoneValidate" method="POST" name="modal-form-validate-phone-code-user-domain">
        <input type="hidden" name="phoneHash" id="phoneHash" required="" value="">
        <input type="hidden" name="validationPhoneNumber" id="validationPhoneNumber" required="" value="">
        <input type="hidden" name="validationPhoneNumberPrefix" id="validationPhoneNumberPrefix" required="" value="">

        <div class="form-group">
            <g:set var="helpBlockTextContent" value="${isWhatsAppSendingEnabled ? 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.WhatsApp.helpBlock' : 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.phoneCode.helpBlock'}" />
            <formUtil:input
                    command="${validatePhoneCodeCommand}"
                    field="phoneCode"
                    showLabel="true"
                    cssClass="form-control input-lg"
                    helpBlock="${message(code: helpBlockTextContent.toString())}"
                    required="true"/>
        </div>

        <fieldset aria-live="polite" class="modal-login-action-buttons">
            <a href="#" id="validatePhoneCodeDomain-modal-form-button-back" role="button" class="btn btn-grey-light col-xs-6"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.back"/></a>
            <button id="validatePhoneCodeDomain-modal-form-button-id" class="btn btn-orange col-xs-6">
                <g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.submit"/>
            </button>
        </fieldset>

        <g:if test="${isWhatsAppSendingEnabled}">
            <fieldset aria-live="polite" class="center modal-login-action-buttons" style="clear: both;">
                
                <a href="#" id="whatsAppNotReceived" role="button" aria-controls="kuorum-whatsapp-help-panel" aria-expanded="false" class="grey"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.whatsAppNotReceived"/></a>
            </fieldset>

            <div id="kuorum-whatsapp-help-panel" style="display: none;">
                <p><strong><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.wrongPhone"/>:</strong> <g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.wrongPhoneDesc"/></p>
                <p><strong><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.coverage"/>:</strong> <g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.coverageDesc"/></p>

                <p class="whatsapp-only-help"><strong><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.reviewWhatsApp"/>:</strong> <g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.reviewWhatsAppDesc"/></p>

                <div class="kuorum-whatsapp-help-actions">
                    <%-- Botón WhatsApp con icono --%>
                    <button type="button" class="btn btn-block" id="btn-resend-whatsapp-action" data-original-text="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.resendWhatsApp')}">
                        <span class="fab fa-whatsapp fa-inverse" aria-hidden="true" style="margin-right: 5px; font-size: 1.2em; position: relative; top: 2px;"></span>
                        <span class="btn-text"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.resendWhatsApp"/></span>
                    </button>

                    <%-- Botón SMS con icono --%>
                    <button type="button" class="btn btn-block" id="btn-resend-sms-action" data-original-text="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.sendSms')}">
                        <span class="fas fa-envelope-open fa-inverse" aria-hidden="true" style="margin-right: 5px; font-size: 1.2em; position: relative; top: 2px;"></span>
                        <span class="btn-text"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.sendSms"/></span>
                    </button>
                </div>
            </div>
        </g:if>

        <g:else>
            <fieldset aria-live="polite" class="center modal-login-action-buttons" style="clear: both; margin-top: 15px;">
                <a href="#" id="btn-resend-sms-action-fallback" role="button" data-original-text="${message(code: 'kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.resendSms')}" class="grey" style="text-decoration: underline;">
                    <span class="fas fa-envelope-open" aria-hidden="true" style="margin-right: 3px; position: relative; top: 1px;"></span>
                    <span class="btn-text"><g:message code="kuorum.web.commands.profile.DomainUserPhoneCodeValidationCommand.modal.resendSms"/></span>
                </a>
            </fieldset>
        </g:else>

    </g:form>
</div>