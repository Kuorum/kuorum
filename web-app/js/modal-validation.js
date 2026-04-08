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

            userValidatedByDomain.binded = true;
            userValidatedByDomain.modal = $("#domain-validation");
            userValidatedByDomain.modalNotifications = $("#domain-validation .modal-domain-validation-notifications");
        }
    },

    // ==========================================
    // ANTI-SPAM: RESEND COUNTDOWN
    // ==========================================
    startResendCountdown: function(channel) {
        var isWa = (channel === 'WHATSAPP');
        var $btn = isWa ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
        var $btnFallback = isWa ? null : $("#btn-resend-sms-action-fallback"); // Fallback only for SMS
        var TIMELEFT = 60; // Wait time in seconds

        // 1. Clear existing timer for this channel
        if (userValidatedByDomain.resendTimers[channel]) {
            clearInterval(userValidatedByDomain.resendTimers[channel]);
        }

        // 2. Disable ONLY the button for this channel
        $btn.prop('disabled', true).addClass('disabled');
        if ($btnFallback && $btnFallback.length) {
            $btnFallback.addClass('disabled').css('pointer-events', 'none');
        }

        // 3. Internal function to render seconds
        function updateText(time) {
            var suffix = time > 0 ? " (" + time + "s)" : "";
            $btn.find('.btn-text').text($btn.attr('data-original-text') + suffix);

            if ($btnFallback && $btnFallback.length) {
                $btnFallback.find('.btn-text').text($btnFallback.attr('data-original-text') + suffix);
            }
        }

        // 4. Start countdown
        updateText(TIMELEFT);
        userValidatedByDomain.resendTimers[channel] = setInterval(function() {
            TIMELEFT--;
            updateText(TIMELEFT);

            if (TIMELEFT <= 0) {
                clearInterval(userValidatedByDomain.resendTimers[channel]);
                userValidatedByDomain.resendTimers[channel] = undefined;
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
    communicationStrategies: {
        SMS: function(requestData, formUrl) {
            // Explicitly inject SMS channel
            var smsData = $.extend({}, requestData, { channel: 'SMS' });
            return $.ajax({
                type: "POST",
                url: formUrl,
                data: smsData
            });
        },
        WHATSAPP: function(requestData, formUrl) {
            // Explicitly inject WHATSAPP channel
            var whatsappData = $.extend({}, requestData, { channel: 'WHATSAPP' });
            return $.ajax({
                type: "POST",
                url: formUrl,
                data: whatsappData
            });
        },
        AUTO: function(requestData, formUrl) {
            var autoData = $.extend({}, requestData, { channel: 'AUTO' });
            return $.ajax({ type: "POST", url: formUrl, data: autoData });
        }
    },

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
    handleSendCodeSuccess: function(dataResponse) {
        if (dataResponse.success) {
            captcha.clearCaptcha();
            $("#phoneHash").val(dataResponse.hash);
            $("#validationPhoneNumber").val(dataResponse.validationPhoneNumber);
            $("#validationPhoneNumberPrefix").val(dataResponse.validationPhoneNumberPrefix);

            // Update HTML in real time if server provides global state
            if (dataResponse.hasOwnProperty('isWhatsAppEnabled')) {
                var isEnabledStr = dataResponse.isWhatsAppEnabled ? 'true' : 'false';
                $("#validatePhoneDomain-modal-form-button-id").attr('data-whatsapp-enabled', isEnabledStr);
            } else {
                // Deductive fallback
                if (dataResponse.actualChannel === 'WHATSAPP') {
                    $("#validatePhoneDomain-modal-form-button-id").attr('data-whatsapp-enabled', 'true');
                } else if (dataResponse.actualChannel === 'SMS' && userValidatedByDomain.currentChannel === 'WHATSAPP') {
                    $("#validatePhoneDomain-modal-form-button-id").attr('data-whatsapp-enabled', 'false');
                }
            }

            if (userValidatedByDomain.currentChannel === 'AUTO') {
                userValidatedByDomain.currentChannel = dataResponse.actualChannel;
            }

            if (dataResponse.actualChannel === 'WHATSAPP') {
                // If server sends a WhatsApp, it is definitively enabled
                userValidatedByDomain.currentChannel = 'WHATSAPP';
                $("#validatePhoneDomain-modal-form-button-id").attr('data-whatsapp-enabled', 'true');
            } else if (dataResponse.actualChannel === 'SMS' && userValidatedByDomain.currentChannel === 'WHATSAPP') {
                // Fallback detected
                userValidatedByDomain.currentChannel = 'SMS';
                $("#validatePhoneDomain-modal-form-button-id").attr('data-whatsapp-enabled', 'false');
            }

            var isWhatsApp = userValidatedByDomain.currentChannel === 'WHATSAPP';
            var $step2Container = $(".modal-domain-validation-phone-step2");

            // --- RESET LOGIC ON PHONE CHANGE ---
            var currentPhone = dataResponse.validationPhoneNumber;
            var currentPrefix = dataResponse.validationPhoneNumberPrefix;

            var isSamePhone = (currentPhone === userValidatedByDomain.lastValidatedPhone &&
                currentPrefix === userValidatedByDomain.lastValidatedPrefix);

            if (!isSamePhone) {
                ['WHATSAPP', 'SMS'].forEach(function(ch) {
                    if (userValidatedByDomain.resendTimers[ch]) {
                        clearInterval(userValidatedByDomain.resendTimers[ch]);
                        userValidatedByDomain.resendTimers[ch] = undefined;

                        // Visually restore buttons to prevent them from locking
                        var isWa = (ch === 'WHATSAPP');
                        var $btn = isWa ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
                        var $btnFallback = isWa ? null : $("#btn-resend-sms-action-fallback");

                        $btn.prop('disabled', false).removeClass('disabled');
                        if ($btnFallback && $btnFallback.length) {
                            $btnFallback.removeClass('disabled').css('pointer-events', 'auto');
                        }
                    }
                });

                // Save new phone in memory
                userValidatedByDomain.lastValidatedPhone = currentPhone;
                userValidatedByDomain.lastValidatedPrefix = currentPrefix;
            }

            // 1. Read localized texts generated by Grails in GSP
            var helpText = isWhatsApp ? $step2Container.attr("data-msg-help-whatsapp") : $step2Container.attr("data-msg-help-sms");
            var linkText = isWhatsApp ? $step2Container.attr("data-msg-link-whatsapp") : $step2Container.attr("data-msg-link-sms");
            var errorText = isWhatsApp ? $step2Container.attr("data-msg-error-whatsapp") : $step2Container.attr("data-msg-error-sms");

            var txtSendWa = $step2Container.attr("data-msg-send-whatsapp");
            var txtResendWa = $step2Container.attr("data-msg-resend-whatsapp");
            var txtSendSms = $step2Container.attr("data-msg-send-sms");
            var txtResendSms = $step2Container.attr("data-msg-resend-sms");

            // 2 & 3. Apply general texts (A11Y FIX)
            var $helpBlock = $step2Container.find(".help-block");
            var helpBlockId = "help-text-phoneCode";
            $helpBlock.attr("id", helpBlockId).text(helpText);

            // Bind input with help text
            $("#phoneCode").attr("aria-describedby", helpBlockId);
            $("#whatsAppNotReceived").text(linkText);

            if (isWhatsApp) {
                $(".whatsapp-only-help").show();
            } else {
                $(".whatsapp-only-help").hide();
            }

            // 4. Change texts and save them as base
            if (isWhatsApp) {
                $("#btn-resend-whatsapp-action").attr('data-original-text', txtResendWa).find('.btn-text').text(txtResendWa);
                $("#btn-resend-sms-action").attr('data-original-text', txtSendSms).find('.btn-text').text(txtSendSms);
            } else {
                $("#btn-resend-whatsapp-action").attr('data-original-text', txtSendWa).find('.btn-text').text(txtSendWa);
                $("#btn-resend-sms-action").attr('data-original-text', txtResendSms).find('.btn-text').text(txtResendSms);
            }

            var $fallback = $("#btn-resend-sms-action-fallback");
            if ($fallback.length && !$fallback.attr('data-original-text')) {
                $fallback.attr('data-original-text', $fallback.find('.btn-text').text());
            }

            // 5. START OR KEEP TIMER
            var channelUsed = isWhatsApp ? 'WHATSAPP' : 'SMS';

            if (!userValidatedByDomain.resendTimers[channelUsed]) {
                // No active timer, start fresh
                userValidatedByDomain.startResendCountdown(channelUsed);
            } else {
                // Timer was already active. Keep disabled state visually.
                var $activeBtn = isWhatsApp ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
                $activeBtn.prop('disabled', true).addClass('disabled');
            }

            // (Optional) If the OTHER channel has an active countdown, keep its blocked style
            var otherChannel = isWhatsApp ? 'SMS' : 'WHATSAPP';
            if (userValidatedByDomain.resendTimers[otherChannel]) {
                var $otherBtn = isWhatsApp ? $("#btn-resend-sms-action") : $("#btn-resend-whatsapp-action");
                $otherBtn.prop('disabled', true).addClass('disabled');
            }

            // 6. UPDATE VALIDATOR ERROR MESSAGE
            $("#phoneCode").rules("add", {
                messages: { required: errorText }
            });

            if ($("#phoneCode-error").length) {
                $("#phoneCode-error").text(errorText);
            }

            if (dataResponse.validated) {
                userValidatedByDomain.nextValidationStep(dataResponse);
            } else {
                userValidatedByDomain.showPhoneValidationStep2();
            }
        } else {
            userValidatedByDomain.showErrorModal(dataResponse.msg);
        }
    },

    handleSendCodeError: function() {
        display.error("There was an error sending a validation code to your phone number");
    },

    // ==========================================
    // MAIN SEND ORCHESTRATOR
    // ==========================================
    sendValidationCode: function($button, channel) {
        var $form = $button.closest("form");

        function successCaptchaCallback() {
            if (!userValidatedByDomain.arePhonesAndPrefixEquals()) {
                userValidatedByDomain.showErrorModal(i18n.inputs.errors.nonMatchingPhones);
                captcha.clearCaptcha();
            } else if ($form.valid() && captcha.isRecaptchaSolved) {
                userValidatedByDomain.modalNotifications.find(".text-danger").hide();
                userValidatedByDomain.showModalLoading();

                var requestData = userValidatedByDomain.buildValidationRequestData();
                var formUrl = $form.attr("action");

                // Dependency injection: Select strategy (SMS by default)
                var senderStrategy = userValidatedByDomain.communicationStrategies[channel] || userValidatedByDomain.communicationStrategies.SMS;

                // Execute promise delegating responsibilities
                senderStrategy(requestData, formUrl)
                    .done(userValidatedByDomain.handleSendCodeSuccess)
                    .fail(userValidatedByDomain.handleSendCodeError)
                    .always(function() {
                        pageLoadingOff();
                    });
            }
        }

        captcha.showCaptcha(successCaptchaCallback);
    },

    // ==========================================
    // EVENT CONTROLLERS
    // ==========================================
    sendSMSForPhoneValidation: function(e) {
        if (e != undefined) { e.preventDefault(); }
        var $button = $(this);

        // --- ANTI-SPAM PREVENTION FROM STEP 1 ---
        // 1. Clean spaces in case user writes the number differently
        var currentPhone = ($("#phoneNumber").val() || '').replace(/\s/g, '');
        var currentPrefix = ($("#phoneNumberPrefix").val() || '').replace(/\s/g, '');
        var lastPhone = (userValidatedByDomain.lastValidatedPhone || '').replace(/\s/g, '');
        var lastPrefix = (userValidatedByDomain.lastValidatedPrefix || '').replace(/\s/g, '');

        // 2. Check if it's the exact same phone we just verified
        var isSamePhone = (currentPhone === lastPhone && currentPrefix === lastPrefix);

        // 3. Check if there's any active timer
        var isTimerRunning = (userValidatedByDomain.resendTimers['WHATSAPP'] !== undefined ||
            userValidatedByDomain.resendTimers['SMS'] !== undefined);

        // 4. If same number and timer is running, prevent request
        if (isSamePhone && isTimerRunning) {
            userValidatedByDomain.showPhoneValidationStep2(); // Return user to Step 2
            return; // Stop execution here to prevent sending
        }

        userValidatedByDomain.currentChannel = 'AUTO';
        userValidatedByDomain.sendValidationCode($button, 'AUTO');
    },

    handleResendCodeAction: function(e) {
        if (e != undefined) { e.preventDefault(); }
        var $clickedButton = $(this);

        // 1. Identify which button was clicked by ID
        var channel = $clickedButton.attr('id') === 'btn-resend-whatsapp-action' ? 'WHATSAPP' : 'SMS';

        // 2. Update memory channel
        userValidatedByDomain.currentChannel = channel;

        // 3. Hide help panel smoothly
        if ($("#kuorum-whatsapp-help-panel").length) {
            $("#kuorum-whatsapp-help-panel").slideUp(200);
        }

        // 4. Clear previous code input
        $("#phoneCode").val('');

        // =========================================================
        // 5. UX: Clear previous instruction text to avoid confusion.
        // =========================================================
        $(".modal-domain-validation-phone-step2 .help-block").empty();

        // =========================================================
        // 6. UX: Clear validation error messages and global error.
        // =========================================================
        $("#phoneCode").removeClass("error");

        // Target specific jQuery Validate label avoiding conflicting ID
        $("label.error[for='phoneCode']").hide().empty();

        // Clear global modal error
        userValidatedByDomain.hideErrorModal();

        // 7. REUSE MAGIC: Use original Step 1 button as anchor
        var $originalButton = $("#validatePhoneDomain-modal-form-button-id");
        userValidatedByDomain.sendValidationCode($originalButton, channel);
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
    showPhoneValidationStep1: function (e) {
        if (e != undefined) {
            e.preventDefault();
        }

        $("#validationPhoneNumber").val('');
        $("#phoneCode").val('');
        $("#phoneHash").val('');

        if ($("#kuorum-whatsapp-help-panel").length) {
            $("#kuorum-whatsapp-help-panel").hide();
        }

        if (userValidatedByDomain.dataValidation.predefinedPhone) {
            $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step1 .modal-domain-validation-phone-step1-predefinedPhone").show();
            $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step1 .modal-domain-validation-phone-step1-inputPhone").hide();
        }
        $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step1").show();
        $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step2").hide();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active");
        $("#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-phoneNumber").addClass("active");
        userValidatedByDomain.hideModalLoading();
        userValidatedByDomain.hideErrorModal();
    },
    showPhoneValidationStep2: function(e) {
        if (e != undefined) { e.preventDefault(); }
        $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step1").hide();
        $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step2").show();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active");
        $("#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-phoneCode").addClass("active");

        var $phoneCodeInput = $("#phoneCode");
        $phoneCodeInput.val("");

        userValidatedByDomain.hideModalLoading();
        userValidatedByDomain.hideErrorModal();

        setTimeout(function() {
            $phoneCodeInput.focus();
        }, 100);
    },

    showWarnGroupValidation:function(){
        $("#domain-validation .modal-domain-validation").hide();
        userValidatedByDomain.hideErrorModal();
        $("#domain-validation .modal-domain-validation-groupCampaign").show();
    },

    handleSubmitValidationPhone:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        if ($form.valid()) {
            userValidatedByDomain.showModalLoading();
            var url = $form.attr("action");

            // Clean DOM and read directly from memory (with SMS fallback)
            var data = {
                campaignId:userValidatedByDomain.dataValidation.campaignId,
                validationPhoneNumber: $("#validationPhoneNumber").val(),
                validationPhoneNumberPrefix: $("#validationPhoneNumberPrefix").val(),
                phoneHash: $("#phoneHash").val(),
                phoneCode: $("#phoneCode").val(),
                channel: userValidatedByDomain.currentChannel || 'SMS'
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
                setTimeout(function () {
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