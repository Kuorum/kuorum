var captcha={
    dataRecaptcha: '',
    grecaptchaResponse: '',
    callback: undefined,
    isRecaptchaSolved: function () {
        return captcha.grecaptchaResponse != ''
    },
    showCaptcha: function (callback) {
        var submitButton = $('#validatePhoneDomain-modal-form-button-id')
        var dataRecaptcha = submitButton.attr('data-recaptcha');
        captcha.dataRecaptcha = dataRecaptcha
        grecaptcha.execute(dataRecaptcha);
        captcha.callback = callback
    },
    clearCaptcha : function () {
        grecaptcha.reset(captcha.dataRecaptcha);
        captcha.dataRecaptcha = '';
        captcha.grecaptchaResponse = '';
        captcha.callback = undefined;
    }
}

function captchaSolvedCallback(grecaptcha) {
    captcha.grecaptchaResponse = grecaptcha;
    captcha.callback();
}

var userValidatedByDomain={

    executable: undefined,
    binded: false,
    validated: false,
    allowAnonymousAction: false,
    modalNotifications: undefined,
    modal: undefined,
    loading: undefined,
    dataValidation: undefined,
    urlAnonymousValidation: undefined,
    successFunctionCallback: undefined,
    are2ndPhoneFieldsHidden: false,
    currentChannel : '',
    resendTimers: {
        whatsapp: undefined,
        sms: undefined
    },
    lastValidatedPhone: '',
    lastValidatedPrefix: '',
    lastValidatedHash: '',
    closeModalTimeoutId: undefined,
    initialVisualState: undefined,

    initVariables: function () {
        if (!userValidatedByDomain.binded) {
            $("#validateDomain-modal-form-button-id").on("click", userValidatedByDomain.handleSubmitValidationForm);
            $("#validatePhoneDomain-modal-form-button-id").on("click", userValidatedByDomain.sendSMSForPhoneValidation);
            $("#validatePhoneCodeDomain-modal-form-button-id").on("click", userValidatedByDomain.handleSubmitValidationPhone);
            $("#validatePhoneCodeDomain-modal-form-button-back").on("click", userValidatedByDomain.showPhoneValidationStep1);
            $("#validateCustomCodeDomain-modal-form-button-id").on("click", userValidatedByDomain.handleSubmitValidationCustomCode);
            $("#groupValidationCampaign-modal-button-id").on("click", function (e) {
                e.preventDefault();
                $("#domain-validation").modal("hide");
            });

            // Binds for the help and resend panel (WhatsApp and SMS)
            $(document).on("click", "#whatsAppNotReceived", userValidatedByDomain.toggleWhatsAppHelpPanel);
            $(document).on("click", "#btn-resend-whatsapp-action, #btn-resend-sms-action, #btn-resend-sms-action-fallback", userValidatedByDomain.handleResendCodeAction);

            userValidatedByDomain._snapshotInitialVisualState();
            userValidatedByDomain.binded = true;
            userValidatedByDomain.modal = $("#domain-validation");
            userValidatedByDomain.modalNotifications = $("#domain-validation .modal-domain-validation-notifications");
        }
    },

    // ==========================================
    // KIOSK MODE: STATE RESET BETWEEN VOTERS (contest application vote)
    // The modal lives in the DOM for the whole page life, so several people can vote one after another
    // from the same browser without reloading. Every voter must find a pristine modal and JS state.
    // ==========================================
    _selectorsWithInlineStyle: [
        ".form-group.form-group-phone.second-phone", ".modal-domain-validation-phone-step1-predefinedPhone",
        ".modal-domain-validation-phone-step1-inputPhone", "#kuorum-whatsapp-help-panel",
        "#ui-block-whatsapp", "#ui-block-sms-only", ".whatsapp-only-help"
    ],
    _resendButtonsSelector: "#btn-resend-whatsapp-action, #btn-resend-sms-action, #btn-resend-sms-action-fallback",

    _snapshotInitialVisualState: () => {
        const u = userValidatedByDomain;
        const $m = $("#domain-validation");
        u.initialVisualState = {
            fields: $m.find("input[id], select[id], textarea[id]").toArray().reduce((acc, el) => {
                acc[el.id] = $(el).is(":checkbox, :radio") ? $(el).prop("checked") : $(el).val();
                return acc;
            }, {}),
            styles: u._selectorsWithInlineStyle.reduce((acc, sel) => {
                acc[sel] = $(sel).map(function () { return $(this).attr("style") || null; }).get();
                return acc;
            }, {}),
            helpBlocks: $m.find(".modal-domain-validation-phone-step2 .help-block").map(function () {
                return {id: this.id || null, text: $(this).text()};
            }).get(),
            resendBtns: $(u._resendButtonsSelector).map(function () {
                const $b = $(this);
                return {id: this.id, originalText: $b.attr("data-original-text") || null, text: $b.find(".btn-text").text()};
            }).get(),
            waEnabled: $("#validatePhoneDomain-modal-form-button-id").attr("data-whatsapp-enabled"),
            notReceived: $("#whatsAppNotReceived").text(),
            phoneCodeDescribedBy: $("#phoneCode").attr("aria-describedby") || null,
            predefinedPhone: $(".modal-domain-validation-phone-step1-predefinedPhone-phone").html()
        };
    },

    // JS memory: anti-spam memory, timers, channel, callbacks and captcha. No visual impact, so it is safe to call it at any moment.
    _resetLogicalState: () => {
        const u = userValidatedByDomain;
        Object.keys(u.resendTimers).forEach(channel => u.resendTimers[channel] && clearInterval(u.resendTimers[channel]));
        if (u.closeModalTimeoutId) {
            clearTimeout(u.closeModalTimeoutId);
        }
        Object.assign(u, {
            resendTimers: {whatsapp: undefined, sms: undefined}, closeModalTimeoutId: undefined,
            lastValidatedPhone: '', lastValidatedPrefix: '', lastValidatedHash: '', currentChannel: '', validated: false,
            executable: undefined, successFunctionCallback: undefined, are2ndPhoneFieldsHidden: false
        });
        if (u.dataValidation) {
            u.dataValidation.predefinedPhone = false;
        }
        captcha.grecaptchaResponse = '';
        captcha.callback = undefined;
        if (captcha.dataRecaptcha) {
            captcha.clearCaptcha(); // It throws if the widget has not been executed yet, so it is only called when there is a widget id
        }
    },

    // DOM: fields, texts, buttons and step. It must run when the modal is hidden to avoid a visible flicker during the fade out.
    _resetVisualState: () => {
        const snap = userValidatedByDomain.initialVisualState;
        if (!snap) {
            return; // The modal has never been used, so it is still pristine
        }
        const $m = $("#domain-validation");

        $m.find("input[id], select[id], textarea[id]").each(function () {
            if (this.id in snap.fields) {
                $(this).is(":checkbox, :radio") ? $(this).prop("checked", snap.fields[this.id]) : $(this).val(snap.fields[this.id]);
            }
        });
        userValidatedByDomain._selectorsWithInlineStyle.forEach(sel => {
            $(sel).each((i, el) => snap.styles[sel][i] ? $(el).attr("style", snap.styles[sel][i]) : $(el).removeAttr("style"));
        });
        $m.find(".modal-domain-validation-phone-step2 .help-block").each((i, el) => {
            const hb = snap.helpBlocks[i];
            if (hb) {
                $(el).text(hb.text);
                hb.id ? $(el).attr("id", hb.id) : $(el).removeAttr("id");
            }
        });
        snap.phoneCodeDescribedBy ? $("#phoneCode").attr("aria-describedby", snap.phoneCodeDescribedBy) : $("#phoneCode").removeAttr("aria-describedby");
        $("#whatsAppNotReceived").text(snap.notReceived);
        $("#validatePhoneDomain-modal-form-button-id").attr("data-whatsapp-enabled", snap.waEnabled);
        $(".modal-domain-validation-phone-step1-predefinedPhone-phone").html(snap.predefinedPhone);
        snap.resendBtns.forEach(btn => {
            const $b = $("#" + btn.id);
            btn.originalText ? $b.attr("data-original-text", btn.originalText) : $b.removeAttr("data-original-text");
            $b.find(".btn-text").text(btn.text);
            $b.prop("disabled", false).removeClass("disabled").css("pointer-events", "");
        });
        $m.find("form").each(function () {
            const validator = $(this).data("validator");
            if (validator) {
                validator.resetForm();
            }
        });
        $m.find(".error").removeClass("error");
        userValidatedByDomain.showPhoneValidationStep1();
    },

    // Full reset. If the modal is visible, the DOM is cleaned once Bootstrap has finished hiding it.
    resetPhoneModal: () => {
        const u = userValidatedByDomain;
        const $modal = $("#domain-validation");
        u._resetLogicalState();
        $modal.off("hidden.bs.modal.kioskReset");
        if ($modal.is(":visible")) {
            $modal.one("hidden.bs.modal.kioskReset", () => u._resetVisualState());
            $modal.modal("hide");
        } else {
            u._resetVisualState();
        }
    },

    // The next click on a vote button must start from scratch. Never touches a modal that is currently open.
    prepareForNewVoter: () => {
        const u = userValidatedByDomain;
        if (!$("#domain-validation").is(":visible")) {
            u._resetLogicalState();
            u._resetVisualState();
            // A flow abandoned before voting leaves the previous anonymous user in the cookie
            u._removeAnonymousVoterCookie();
        }
    },

    _removeAnonymousVoterCookie: () => {
        if (typeof cookiesHelper !== 'undefined') {
            cookiesHelper.removeCookie("KUORUM_USER_UUID");
        }
    },

    // The anonymous (fake) user created to vote must not be inherited by the next voter
    silentLogout: () => {
        userValidatedByDomain.resetPhoneModal();
        userValidatedByDomain._removeAnonymousVoterCookie();
    },

    // ==========================================
    // ANTI-SPAM: RESEND COUNTDOWN
    // ==========================================
    startResendCountdown: (channel) => {
        const u = userValidatedByDomain;
        const isWa = channel === 'WHATSAPP';
        const $btn = isWa ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
        const $btnFallback = isWa ? null : $("#btn-resend-sms-action-fallback"); // Fallback only for SMS
        let timeLeft = 60; // Wait time in seconds

        if (u.resendTimers[channel]) {
            clearInterval(u.resendTimers[channel]);
        }
        $btn.prop('disabled', true).addClass('disabled');
        if ($btnFallback && $btnFallback.length) {
            $btnFallback.addClass('disabled').css('pointer-events', 'none');
        }

        const updateText = (time) => {
            const suffix = time > 0 ? ` (${time}s)` : "";
            $btn.find('.btn-text').text($btn.attr('data-original-text') + suffix);
            if ($btnFallback && $btnFallback.length) {
                $btnFallback.find('.btn-text').text($btnFallback.attr('data-original-text') + suffix);
            }
        };

        updateText(timeLeft);
        u.resendTimers[channel] = setInterval(() => {
            timeLeft--;
            updateText(timeLeft);
            if (timeLeft <= 0) {
                clearInterval(u.resendTimers[channel]);
                u.resendTimers[channel] = undefined;
                $btn.prop('disabled', false).removeClass('disabled');
                if ($btnFallback && $btnFallback.length) {
                    $btnFallback.removeClass('disabled').css('pointer-events', 'auto');
                }
            }
        }, 1000);
    },

    // ==========================================
    // PANEL UI LOGIC
    // ==========================================
    toggleWhatsAppHelpPanel: function(e) {
        if (e != undefined) { e.preventDefault(); }
        $("#kuorum-whatsapp-help-panel").slideToggle(250);
    },

    // ==========================================
    // COMMUNICATION STRATEGIES (SOLID: OCP)
    // ==========================================
    // Every strategy sends the same request injecting its own channel (SMS by default)
    communicationStrategies: ['SMS', 'WHATSAPP', 'AUTO'].reduce((strategies, channel) => {
        strategies[channel] = (requestData, formUrl) => $.ajax({type: "POST", url: formUrl, data: $.extend({}, requestData, {channel: channel})});
        return strategies;
    }, {}),

    // ==========================================
    // BUILD DATA (SOLID: SRP)
    // ==========================================
    buildValidationRequestData: function() {
        return {
            campaignId: userValidatedByDomain.dataValidation.campaignId,
            phoneNumberPrefix: $("#phoneNumberPrefix").val(),
            phoneNumber: $("#phoneNumber").val(),
            'g-recaptcha-response': captcha.grecaptchaResponse,
            'allowAnonymousAction': userValidatedByDomain.dataValidation.allowAnonymousAction
        };
    },

    // ==========================================
    // RESPONSE HANDLERS (SOLID: SRP)
    // ==========================================
    handleSendCodeSuccess: (res) => {
        const u = userValidatedByDomain;
        if (!res.success) {
            u.showErrorModal(res.msg);
            return;
        }
        captcha.clearCaptcha();
        $("#phoneHash").val(res.hash);
        $("#validationPhoneNumber").val(res.validationPhoneNumber);
        $("#validationPhoneNumberPrefix").val(res.validationPhoneNumberPrefix);
        u.lastValidatedHash = res.hash; // Every send generates a new code, so a new hash

        // Update HTML in real time if server provides global state
        const $waBtn = $("#validatePhoneDomain-modal-form-button-id");
        if (res.hasOwnProperty('isWhatsAppEnabled')) {
            $waBtn.attr('data-whatsapp-enabled', res.isWhatsAppEnabled ? 'true' : 'false');
        } else if (res.actualChannel === 'WHATSAPP') {
            $waBtn.attr('data-whatsapp-enabled', 'true');
        } else if (res.actualChannel === 'SMS' && u.currentChannel === 'WHATSAPP') {
            $waBtn.attr('data-whatsapp-enabled', 'false');
        }

        if (u.currentChannel === 'AUTO') {
            u.currentChannel = res.actualChannel;
        }
        if (res.actualChannel === 'WHATSAPP') {
            // If server sends a WhatsApp, it is definitively enabled
            u.currentChannel = 'WHATSAPP';
            $waBtn.attr('data-whatsapp-enabled', 'true');
        } else if (res.actualChannel === 'SMS' && u.currentChannel === 'WHATSAPP') {
            // Fallback detected
            u.currentChannel = 'SMS';
            $waBtn.attr('data-whatsapp-enabled', 'false');
        }

        const isWa = u.currentChannel === 'WHATSAPP';
        const $step2 = $(".modal-domain-validation-phone-step2");

        // Reset the anti-spam countdown when the phone changes
        if (res.validationPhoneNumber !== u.lastValidatedPhone || res.validationPhoneNumberPrefix !== u.lastValidatedPrefix) {
            ['WHATSAPP', 'SMS'].forEach(ch => {
                if (u.resendTimers[ch]) {
                    clearInterval(u.resendTimers[ch]);
                    u.resendTimers[ch] = undefined;
                    // Visually restore buttons to prevent them from locking
                    $(ch === 'WHATSAPP' ? "#btn-resend-whatsapp-action" : "#btn-resend-sms-action").prop('disabled', false).removeClass('disabled');
                    if (ch === 'SMS') {
                        $("#btn-resend-sms-action-fallback").removeClass('disabled').css('pointer-events', 'auto');
                    }
                }
            });
            u.lastValidatedPhone = res.validationPhoneNumber;
            u.lastValidatedPrefix = res.validationPhoneNumberPrefix;
        }

        // Localized texts generated by Grails in the GSP
        const type = isWa ? 'whatsapp' : 'sms';
        const errorText = $step2.attr(`data-msg-error-${type}`);
        $step2.find(".help-block").attr("id", "help-text-phoneCode").text($step2.attr(`data-msg-help-${type}`));
        $("#phoneCode").attr("aria-describedby", "help-text-phoneCode");
        $("#whatsAppNotReceived").text($step2.attr(`data-msg-link-${type}`));

        // Show/Hide panels based on absolute truth
        if (($waBtn.attr('data-whatsapp-enabled') || '').toLowerCase() === 'true') {
            $("#ui-block-whatsapp").show();
            $("#ui-block-sms-only").hide();
            $(".whatsapp-only-help").toggle(isWa); // Specific instructions only in WhatsApp mode
        } else {
            $("#ui-block-whatsapp").hide();
            $("#ui-block-sms-only").show();
        }

        // Change texts and save them as base
        const setBtnText = (selector, text) => $(selector).attr('data-original-text', text).find('.btn-text').text(text);
        setBtnText("#btn-resend-whatsapp-action", $step2.attr(`data-msg-${isWa ? 'resend' : 'send'}-whatsapp`));
        setBtnText("#btn-resend-sms-action", $step2.attr(`data-msg-${isWa ? 'send' : 'resend'}-sms`));
        const $fallback = $("#btn-resend-sms-action-fallback");
        if ($fallback.length && !$fallback.attr('data-original-text')) {
            $fallback.attr('data-original-text', $fallback.find('.btn-text').text());
        }

        // Start the timer or keep the blocked style of the buttons with an active countdown
        const channelUsed = isWa ? 'WHATSAPP' : 'SMS';
        const blockedBtn = ch => (ch === 'WHATSAPP' ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action")).prop('disabled', true).addClass('disabled');
        if (u.resendTimers[channelUsed]) {
            blockedBtn(channelUsed);
        } else {
            u.startResendCountdown(channelUsed);
        }
        const otherChannel = isWa ? 'SMS' : 'WHATSAPP';
        if (u.resendTimers[otherChannel]) {
            blockedBtn(otherChannel);
        }

        $("#phoneCode").rules("add", {messages: {required: errorText}});
        $("#phoneCode-error").text(errorText);

        if (res.validated) {
            u.nextValidationStep(res);
        } else {
            u.showPhoneValidationStep2();
        }
    },

    handleSendCodeError: function() {
        display.error("There was an error sending a validation code to your phone number");
    },

    // ==========================================
    // MAIN SEND ORCHESTRATOR
    // ==========================================
    sendValidationCode: ($button, channel) => {
        const u = userValidatedByDomain;
        const $form = $button.closest("form");

        captcha.showCaptcha(() => {
            if (!u.arePhonesAndPrefixEquals()) {
                u.showErrorModal(i18n.inputs.errors.nonMatchingPhones);
                captcha.clearCaptcha();
            } else if ($form.valid() && captcha.isRecaptchaSolved) {
                u.modalNotifications.find(".text-danger").hide();
                u.showModalLoading();
                (u.communicationStrategies[channel] || u.communicationStrategies.SMS)(u.buildValidationRequestData(), $form.attr("action"))
                    .done(u.handleSendCodeSuccess).fail(u.handleSendCodeError).always(() => pageLoadingOff());
            }
        });
    },

    // ==========================================
    // EVENT CONTROLLERS
    // ==========================================
    sendSMSForPhoneValidation: function (e) {
        e && e.preventDefault();
        const u = userValidatedByDomain;
        const clean = val => (val || '').replace(/\s/g, '');

        // Same phone as the last one and an active countdown: go back to step 2 instead of sending again
        if (clean($("#phoneNumber").val()) === clean(u.lastValidatedPhone) && clean($("#phoneNumberPrefix").val()) === clean(u.lastValidatedPrefix)
            && (u.resendTimers['WHATSAPP'] || u.resendTimers['SMS'])) {
            // Coming back from step 1 has cleared the hidden fields of the previous send: restore them or the code can not be validated
            $("#validationPhoneNumber").val(u.lastValidatedPhone);
            $("#validationPhoneNumberPrefix").val(u.lastValidatedPrefix);
            $("#phoneHash").val(u.lastValidatedHash);
            u.showPhoneValidationStep2();
            return;
        }
        u.currentChannel = 'AUTO';
        u.sendValidationCode($(this), 'AUTO');
    },

    handleResendCodeAction: function (e) {
        e && e.preventDefault();
        const u = userValidatedByDomain;
        u.currentChannel = this.id === 'btn-resend-whatsapp-action' ? 'WHATSAPP' : 'SMS';
        $("#kuorum-whatsapp-help-panel").slideUp(200);
        $("#phoneCode").val('').removeClass("error");
        // Clear the previous instruction text and the validation errors (jQuery Validate label included)
        $(".modal-domain-validation-phone-step2 .help-block").empty();
        $("label.error[for='phoneCode']").hide().empty();
        u.hideErrorModal();
        u.sendValidationCode($("#validatePhoneDomain-modal-form-button-id"), u.currentChannel);
    },

    // ==========================================
    // ORIGINAL METHODS
    // ==========================================
    handleLoginAndValidationUser: function ($button, callbackNameAfterLogin, clickButtonOnSuccess) {
        var loggedUserAlias = $button.attr("data-loggedUser");
        var noLoggedUser = loggedUserAlias == undefined || loggedUserAlias == "";
        var allowedAnonymousVote = $button.attr("data-allowAnonymousAction") == "true";
        if (noLoggedUser && allowedAnonymousVote) {
            console.log("CHECK DOMAIN VALIDATION");
            userValidatedByDomain.prepareForNewVoter();
            userValidatedByDomain.initDataValidation($button, clickButtonOnSuccess);
            userValidatedByDomain.checkNoUserValidations($button, clickButtonOnSuccess)
        } else if (noLoggedUser) {
            // NOT LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", callbackNameAfterLogin);
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        } else {
            userValidatedByDomain.executeClickButtonHandlingValidations($button, clickButtonOnSuccess);
        }
    },

    openAndPrepareValidationModal: function () {
        if (!userValidatedByDomain.dataValidation.allowAnonymousAction){
            this.hide2ndPhoneFields();
        }

        // Open validation modal
        $("#domain-validation").modal({
            backdrop: 'static',
            keyboard: false
        });
        userValidatedByDomain.closeRegisterModal(true);

        console.log("Anonymous vote is active: " + userValidatedByDomain.dataValidation.allowAnonymousAction)
        if (!isUserLogged() && !userValidatedByDomain.dataValidation.allowAnonymousAction) {
            // User is logged but the page is not reloaded and the anonymous action is not allowed.
            $('#domain-validation').on('hidden.bs.modal', function () {
                if (!userValidatedByDomain.validated) {
                    // Delay reload to show the error message
                    display.error(i18n.kuorum.web.commands.profile.DomainValidationCommand.closeWithoutValidation);
                }
                noLoggedCallbacks.reloadPage("Validation :: User logged :: Close validation modal", 1000);
            })
        }
    },

    closeRegisterModal: function (reloadPageOnCloseValidation) {
        if (($("#registro").data('bs.modal') || {}).isShown) {
            $("#registro").modal("hide");
            if (reloadPageOnCloseValidation) {
                $('#domain-validation').on('hidden.bs.modal', function () {
                    noLoggedCallbacks.reloadPage("Validation :: Close login modal :: Close validation modal", 500);
                });
            }
        }
    },
    hide2ndPhoneFields: function () {
        $(".form-group.form-group-phone.second-phone").hide();
        userValidatedByDomain.are2ndPhoneFieldsHidden = true;
    },
    comparePhones: function () {
        var phoneNo1 = $("#phoneNumber").val();
        var phoneNo2 = $("#phoneNumber2").val();
        return phoneNo1 === phoneNo2;
    },
    comparePhonesPrefix: function () {
        var phonePrefixNo1 = $("#phoneNumberPrefix").val();
        var phonePrefixNo2 = $("#phoneNumberPrefix2").val();
        return phonePrefixNo1 === phonePrefixNo2;
    },
    arePhonesAndPrefixEquals: function (){
        var samePhoneData;
        if (!userValidatedByDomain.are2ndPhoneFieldsHidden) {
            var samePhones = userValidatedByDomain.comparePhones();
            var samePhonePrefix = userValidatedByDomain.comparePhonesPrefix();
            samePhoneData = samePhones && samePhonePrefix;
        } else {
            samePhoneData = true;
        }
        return samePhoneData;
    },
    executeClickButtonHandlingValidations: function ($button, executableFunctionCallback) {
        userValidatedByDomain.initDataValidation($button, executableFunctionCallback);
        if (userValidatedByDomain.dataValidation.validationActive == "true") {
            userValidatedByDomain.checkUserValid(executableFunctionCallback);
        } else {
            userValidatedByDomain.checkGroupValidation(executableFunctionCallback);
        }
    },

    initDataValidation: function ($button, callbackSuccess) {
        userValidatedByDomain.dataValidation = {
            loggedUser: $button.attr('data-useralias'), // No needed
            validationActive: $button.attr('data-campaignValidationActive'),
            groupValidation: $button.attr('data-campaignGroupValidationActive'),
            campaignId: $button.attr('data-campaignId'),
            allowAnonymousAction: $button.attr('data-allowAnonymousAction'),
            urlAnonymousValidation: $button.attr('data-ajaxAnonymousValidationChecker'),
            predefinedPhone: false,
        };
        userValidatedByDomain.successFunctionCallback = callbackSuccess;
    },

    checkNoUserValidations: function ($button) {
        var url = userValidatedByDomain.dataValidation.urlAnonymousValidation;
        var data = userValidatedByDomain.dataValidation;
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                userValidatedByDomain.initVariables();
                userValidatedByDomain.nextValidationStep(dataLogin);
                if (!dataLogin.validated) {
                    userValidatedByDomain.openAndPrepareValidationModal();
                } else {
                    userValidatedByDomain.validated = true;
                }
            },
            error: function () {
                // User is not logged in or validated. Showing modal validation process
                pageLoadingOff();
                display.error(i18n.kuorum.session.validation.error);
                userValidatedByDomain.silentLogout();
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    checkUserValid: function (executableFunctionCallback) {
        var url = kuorumUrls.domainValidationChecker;
        var data = userValidatedByDomain.dataValidation;
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                userValidatedByDomain.initVariables();
                userValidatedByDomain.nextValidationStep(dataLogin, executableFunctionCallback);
                if (!dataLogin.validated) {
                    userValidatedByDomain.openAndPrepareValidationModal();
                }else{
                    userValidatedByDomain.validated = true;
                }
            },
            error:function(){
                // User is not logged in or validated. Showing modal validation process
                pageLoadingOff();
                display.error(i18n.kuorum.session.validation.error);
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    checkGroupValidation:function(executableFunctionCallback, closeCallbackModal){
        var joinCallbacks = new userValidatedByDomain.ExcutableFunctionCallback(function(params) {
            if (executableFunctionCallback != undefined) {
                executableFunctionCallback.exec();
            }
            if (closeCallbackModal != undefined) {
                closeCallbackModal();
            }
        }, "Joined functions -> No need params")
        if (userValidatedByDomain.dataValidation.groupValidation != undefined && userValidatedByDomain.dataValidation.groupValidation != ''){
            userValidatedByDomain._ajaxRemoteCheckGroupValidation(joinCallbacks);
        }else{
            joinCallbacks.exec();
        }
    },
    _ajaxRemoteCheckGroupValidation:function(executableFunctionCallback){
        var url =userValidatedByDomain.dataValidation.groupValidation;
        $.ajax({
            type: "POST",
            url: url,
            data: userValidatedByDomain.dataValidation,
            success: function (dataCheckGroupValidation) {
                if (dataCheckGroupValidation.belongsToCampaignGroup){
                    executableFunctionCallback.exec();
                }else{
                    userValidatedByDomain.initVariables();
                    userValidatedByDomain.openAndPrepareValidationModal();
                    userValidatedByDomain.showWarnGroupValidation();
                }
            },
            error:function(){
                display.error(i18n.kuorum.session.validation.groupError);
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    showCensusValidation:function(){
        $("#domain-validation .modal-domain-validation").hide();
        $("#domain-validation .modal-domain-validation-census").show();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active");
        $("#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-census").addClass("active");
    },

    showCodeValidation:function(){
        $("#domain-validation .modal-domain-validation").hide();
        $("#domain-validation .modal-domain-validation-customCode").show();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active");
        $("#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-customCode").addClass("active");
        userValidatedByDomain.hideErrorModal();
    },
    showPhoneValidation: function () {
        $("#domain-validation .modal-domain-validation").hide();
        $("#domain-validation .modal-domain-validation-phone").show();
        if ($("phoneHash").val() == "" || $("phoneHash").val() == undefined) {
            userValidatedByDomain.showPhoneValidationStep1();
        } else {
            userValidatedByDomain.showPhoneValidationStep2();
        }
    },
    showTokenMailValidation: function () {
        $("#domain-validation .modal-domain-validation").hide();
        $("#domain-validation .modal-domain-validation-tokenMail").show();
    },
    showPhoneValidationStep1: (e) => {
        e && e.preventDefault();
        const u = userValidatedByDomain;
        const $phone = $("#domain-validation .modal-domain-validation-phone");
        $("#validationPhoneNumber, #phoneCode, #phoneHash").val('');
        $("#kuorum-whatsapp-help-panel").hide();

        if (u.dataValidation.predefinedPhone) {
            $phone.find(".modal-domain-validation-phone-step1 .modal-domain-validation-phone-step1-predefinedPhone").show();
            $phone.find(".modal-domain-validation-phone-step1 .modal-domain-validation-phone-step1-inputPhone").hide();
        }
        $phone.find(".modal-domain-validation-phone-step1").show();
        $phone.find(".modal-domain-validation-phone-step2").hide();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active")
            .filter(".modal-domain-validation-step-tabs-phoneNumber").addClass("active");
        u.hideModalLoading();
        u.hideErrorModal();
    },
    showPhoneValidationStep2: (e) => {
        e && e.preventDefault();
        const u = userValidatedByDomain;
        const $phone = $("#domain-validation .modal-domain-validation-phone");
        $phone.find(".modal-domain-validation-phone-step1").hide();
        $phone.find(".modal-domain-validation-phone-step2").show();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active")
            .filter(".modal-domain-validation-step-tabs-phoneCode").addClass("active");

        const $phoneCodeInput = $("#phoneCode").val("");
        u.hideModalLoading();
        u.hideErrorModal();
        setTimeout(() => $phoneCodeInput.focus(), 100);
    },

    showWarnGroupValidation:function(){
        $("#domain-validation .modal-domain-validation").hide();
        userValidatedByDomain.hideErrorModal();
        $("#domain-validation .modal-domain-validation-groupCampaign").show();
    },

    handleSubmitValidationPhone: function (e) {
        e.preventDefault();
        const u = userValidatedByDomain;
        const $form = $(this).closest("form");
        if ($form.valid()) {
            u.showModalLoading();
            $.ajax({
                type: "POST", url: $form.attr("action"),
                data: {
                    campaignId: u.dataValidation.campaignId,
                    validationPhoneNumber: $("#validationPhoneNumber").val(),
                    validationPhoneNumberPrefix: $("#validationPhoneNumberPrefix").val(),
                    phoneHash: $("#phoneHash").val(),
                    phoneCode: $("#phoneCode").val(),
                    channel: u.currentChannel || 'SMS' // Read directly from memory (with SMS fallback)
                },
                success: (data) => u.nextValidationStep(data),
                error: () => display.error("Error validating the sms"),
                complete: () => u.hideModalLoading()
            });
        }
    },
    handleSubmitValidationCustomCode:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        if ($form.valid()) {
            userValidatedByDomain.showModalLoading();
            var url = $form.attr("action");
            var data = {
                campaignId:userValidatedByDomain.dataValidation.campaignId,
                customCode: $("#customCode").val()
            };
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (dataSmsValidation) {
                    userValidatedByDomain.nextValidationStep(dataSmsValidation);
                },
                error: function (dataError) {
                    display.error("Error validating the sms");
                },
                complete: function () {
                    userValidatedByDomain.hideModalLoading();
                }
            });
        }
    },

    ExcutableFunctionCallback: function (excutable, params){
        this.exec = function(){
            excutable(params);
        }
    },

    handleSubmitValidationForm:function (e) {
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        var url = $form.attr("action");
        var data = $form.serialize()+"&campaignId="+userValidatedByDomain.dataValidation.campaignId;
        if ($form.valid()){
            userValidatedByDomain.showModalLoading();

            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (data) {
                    userValidatedByDomain.nextValidationStep(data);
                },
                error:function(){
                    // Wrong user validation
                    display.error("Error validating user");
                },
                complete: function () {
                    userValidatedByDomain.hideModalLoading();
                }
            });
        }
    },

    showModalLoading:function(){
        userValidatedByDomain.modalNotifications.show();
        userValidatedByDomain.modalNotifications.find(".loading").show();
        userValidatedByDomain.modal.find(".modal-login-action-buttons").hide();
    },
    hideModalLoading:function(){
        if (!userValidatedByDomain.modalNotifications.find(".text-danger").is(":visible")){
            userValidatedByDomain.modalNotifications.hide();
        }
        userValidatedByDomain.modalNotifications.find(".loading").hide();
        userValidatedByDomain.modal.find(".modal-login-action-buttons").show();
    },
    showErrorModal:function(msg){
        userValidatedByDomain.hideModalLoading();
        userValidatedByDomain.modalNotifications.show();
        userValidatedByDomain.modalNotifications.find(".text-danger .text-error-data").html(msg);
        var idError = userValidatedByDomain.modal.find("input:visible").attr("aria-errormessage");
        if (idError != undefined) {
            userValidatedByDomain.modalNotifications.find(".text-danger .text-error-data").attr("id", idError);
        }
        userValidatedByDomain.modalNotifications.find(".text-danger").show();
        userValidatedByDomain.modal.find(".modal-login-action-buttons").show();
        captcha.clearCaptcha();
    },

    hideErrorModal:function(){
        userValidatedByDomain.modalNotifications.hide();
        userValidatedByDomain.modalNotifications.find(".text-danger").hide();
        userValidatedByDomain.modal.find(".modal-login-action-buttons").show();
    },

    nextValidationStep: function(callbackData){
        console.log("Next step :: init");
        if (callbackData.validated) {
            console.log("Next step :: Validated")
            userValidatedByDomain.hideErrorModal();
            userValidatedByDomain.validated = true;
            $("#validateDomain-modal-form-button-id").find(".text-success").show();
            userValidatedByDomain.checkGroupValidation(userValidatedByDomain.successFunctionCallback, function () {
                $("#validationPhoneNumber").val('');
                $("#validationPhoneNumberPrefix").val('');
                // The id is kept so the kiosk reset can cancel the pending close
                userValidatedByDomain.closeModalTimeoutId = setTimeout(function () {
                    userValidatedByDomain.closeModalTimeoutId = undefined;
                    $("#domain-validation").modal("hide");
                }, 1000);
            });
        }else if (!callbackData.success){
            console.log("Next step :: Not validation success");
            userValidatedByDomain.showErrorModal(callbackData.msg)
        }else {
            if (!callbackData.pendingValidations.tokenMailValidation.success) {
                console.log("Next step :: Show Token Mail Validation")
                userValidatedByDomain.showTokenMailValidation();
            } else if (!callbackData.pendingValidations.censusValidation.success) {
                console.log("Next step :: Show Census validation")
                userValidatedByDomain.showCensusValidation();
            } else if (!callbackData.pendingValidations.codeValidation.success) {
                console.log("Next step :: Show Code validation")
                userValidatedByDomain.showCodeValidation();
            } else if (!callbackData.pendingValidations.phoneValidation.success) {
                console.log("Next step :: Show phone validation");
                if (callbackData.pendingValidations.phoneValidation.data.predefinedPhone) {
                    userValidatedByDomain.dataValidation.predefinedPhone = true;
                    $(".modal-domain-validation-phone-step1-predefinedPhone-phone").html(callbackData.pendingValidations.phoneValidation.data.phone);
                }
                userValidatedByDomain.showPhoneValidation();
            }
        }
    }
};