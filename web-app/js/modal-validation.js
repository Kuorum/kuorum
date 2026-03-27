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
        captcha.grecaptchaResponse = ''
        captcha.callback = undefined
    }
}
function captchaSolvedCallback(grecaptcha) {
    console.log("entrando al callback del captcha")
    captcha.grecaptchaResponse = grecaptcha;
    captcha.callback()
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
    lastValidatedPhone: '',   // <-- NUEVO
    lastValidatedPrefix: '',  // <-- NUEVO

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

            // BINDS PARA EL PANEL DE AYUDA Y REENVÍO (WhatsApp y SMS)
            $(document).on("click", "#whatsAppNotReceived", userValidatedByDomain.toggleWhatsAppHelpPanel);
            $(document).on("click", "#btn-resend-whatsapp-action, #btn-resend-sms-action, #btn-resend-sms-action-fallback", userValidatedByDomain.handleResendCodeAction);

            userValidatedByDomain.binded = true
            userValidatedByDomain.modal = $("#domain-validation")
            userValidatedByDomain.modalNotifications = $("#domain-validation .modal-domain-validation-notifications")
        }
    },
    // ==========================================
    // ANTI-SPAM: CUENTA ATRÁS DE REENVÍO
    // ==========================================
    startResendCountdown: function(channel) {
        var isWa = (channel === 'WHATSAPP');
        var $btn = isWa ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
        var $btnFallback = isWa ? null : $("#btn-resend-sms-action-fallback"); // Fallback solo para SMS
        var timeLeft = 60; // Segundos de espera

        // 1. Limpiamos si ya había un temporizador corriendo para este canal
        if (userValidatedByDomain.resendTimers[channel]) {
            clearInterval(userValidatedByDomain.resendTimers[channel]);
        }

        // 2. Deshabilitamos SOLO el botón de este canal
        $btn.prop('disabled', true).addClass('disabled');
        if ($btnFallback && $btnFallback.length) {
            $btnFallback.addClass('disabled').css('pointer-events', 'none');
        }

        // 3. Función interna para pintar los segundos
        function updateText(time) {
            var suffix = time > 0 ? " (" + time + "s)" : "";

            // Apuntamos al span interior usando .find('.btn-text')
            $btn.find('.btn-text').text($btn.attr('data-original-text') + suffix);

            if ($btnFallback && $btnFallback.length) {
                $btnFallback.find('.btn-text').text($btnFallback.attr('data-original-text') + suffix);
            }
        }

        // 4. Arrancamos el contador
        updateText(timeLeft);
        userValidatedByDomain.resendTimers[channel] = setInterval(function() {
            timeLeft--;
            updateText(timeLeft);

            if (timeLeft <= 0) {
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
    // LÓGICA DE INTERFAZ DEL PANEL
    // ==========================================
    toggleWhatsAppHelpPanel: function(e) {
        if (e != undefined) { e.preventDefault(); }
        $("#kuorum-whatsapp-help-panel").slideToggle(250);
    },

    // ==========================================
    // ESTRATEGIAS DE COMUNICACIÓN (SOLID: OCP)
    // ==========================================
    communicationStrategies: {
        SMS: function(requestData, formUrl) {
            console.log("-> Ejecutando estrategia: SMS CLÁSICO");
            return $.ajax({
                type: "POST",
                url: formUrl, // Usa la URL nativa del action del formulario
                data: requestData
            });
        },
        WHATSAPP: function(requestData, formUrl) {
            console.log("-> Ejecutando estrategia: WHATSAPP");

            // Usamos EXACTAMENTE LA MISMA URL (formUrl), pero le inyectamos el parámetro channel='WHATSAPP'
            var whatsappData = $.extend({}, requestData, { channel: 'WHATSAPP' });

            return $.ajax({
                type: "POST",
                url: formUrl,
                data: whatsappData
            });
        }
    },

    // ==========================================
    // CONSTRUCCIÓN DE DATOS (SOLID: SRP)
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
    // MANEJADORES DE RESPUESTA (SOLID: SRP)
    // ==========================================
    handleSendCodeSuccess: function(dataResponse) {
        console.log(dataResponse);
        if (dataResponse.success) {
            captcha.clearCaptcha();
            $("#phoneHash").val(dataResponse.hash);
            $("#validationPhoneNumber").val(dataResponse.validationPhoneNumber);
            $("#validationPhoneNumberPrefix").val(dataResponse.validationPhoneNumberPrefix);

            // =========================================================
            // LÓGICA DE UX: ACTUALIZAR TEXTOS Y ERRORES SEGÚN EL CANAL
            // =========================================================
            var isWhatsApp = userValidatedByDomain.currentChannel === 'WHATSAPP';
            var $step2Container = $(".modal-domain-validation-phone-step2");

            // --- NUEVO: LÓGICA DE REINICIO POR CAMBIO DE TELÉFONO ---
            var currentPhone = dataResponse.validationPhoneNumber;
            var currentPrefix = dataResponse.validationPhoneNumberPrefix;

            var isSamePhone = (currentPhone === userValidatedByDomain.lastValidatedPhone &&
                currentPrefix === userValidatedByDomain.lastValidatedPrefix);

            if (!isSamePhone) {
                console.log("-> Nuevo teléfono detectado. Reseteando temporizadores antiguos.");
                ['WHATSAPP', 'SMS'].forEach(function(ch) {
                    if (userValidatedByDomain.resendTimers[ch]) {
                        clearInterval(userValidatedByDomain.resendTimers[ch]);
                        userValidatedByDomain.resendTimers[ch] = undefined;

                        // Restaurar los botones visualmente para que no se queden bloqueados
                        var isWa = (ch === 'WHATSAPP');
                        var $btn = isWa ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
                        var $btnFallback = isWa ? null : $("#btn-resend-sms-action-fallback");

                        $btn.prop('disabled', false).removeClass('disabled');
                        if ($btnFallback && $btnFallback.length) {
                            $btnFallback.removeClass('disabled').css('pointer-events', 'auto');
                        }
                    }
                });

                // Guardamos el nuevo teléfono en memoria
                userValidatedByDomain.lastValidatedPhone = currentPhone;
                userValidatedByDomain.lastValidatedPrefix = currentPrefix;
            }
            // ---------------------------------------------------------

            // 1. Leemos los textos localizados generados por Grails en el GSP
            var helpText = isWhatsApp ? $step2Container.attr("data-msg-help-whatsapp") : $step2Container.attr("data-msg-help-sms");
            var linkText = isWhatsApp ? $step2Container.attr("data-msg-link-whatsapp") : $step2Container.attr("data-msg-link-sms");
            var errorText = isWhatsApp ? $step2Container.attr("data-msg-error-whatsapp") : $step2Container.attr("data-msg-error-sms");

            var txtSendWa = $step2Container.attr("data-msg-send-whatsapp");
            var txtResendWa = $step2Container.attr("data-msg-resend-whatsapp");
            var txtSendSms = $step2Container.attr("data-msg-send-sms");
            var txtResendSms = $step2Container.attr("data-msg-resend-sms");


            // 2 y 3. Aplicamos textos generales (CORRECCIÓN A11Y)
            var $helpBlock = $step2Container.find(".help-block");

            // Le ponemos un ID dinámico al bloque de ayuda si no lo tiene
            var helpBlockId = "help-text-phoneCode";
            $helpBlock.attr("id", helpBlockId).text(helpText);

            // Vinculamos el input con el texto de ayuda en lugar de sobreescribir su aria-label
            $("#phoneCode").attr("aria-describedby", helpBlockId);

            $("#whatsAppNotReceived").text(linkText);

            if (isWhatsApp) {
                $(".whatsapp-only-help").show();
            } else {
                $(".whatsapp-only-help").hide();
            }

            // 4. Cambiamos textos y los guardamos como base (data-original-text)
            if (isWhatsApp) {
                $("#btn-resend-whatsapp-action").attr('data-original-text', txtResendWa).find('.btn-text').text(txtResendWa);
                $("#btn-resend-sms-action").attr('data-original-text', txtSendSms).find('.btn-text').text(txtSendSms);
            } else {
                $("#btn-resend-whatsapp-action").attr('data-original-text', txtSendWa).find('.btn-text').text(txtSendWa);
                $("#btn-resend-sms-action").attr('data-original-text', txtResendSms).find('.btn-text').text(txtResendSms);
            }

            var $fallback = $("#btn-resend-sms-action-fallback");
            if ($fallback.length && !$fallback.attr('data-original-text')) {
                // Guardamos el texto limpio original (sin el icono) sacándolo del span
                $fallback.attr('data-original-text', $fallback.find('.btn-text').text());
            }

            // 5. INICIAMOS O MANTENEMOS EL CONTADOR
            var channelUsed = isWhatsApp ? 'WHATSAPP' : 'SMS';

            if (!userValidatedByDomain.resendTimers[channelUsed]) {
                // No había temporizador activo, lo iniciamos fresco de 60s
                userValidatedByDomain.startResendCountdown(channelUsed);
            } else {
                // Ya había temporizador (el usuario puso el mismo número y reenvió desde el Paso 1).
                // Mantenemos el estado 'disabled' visualmente para que no haya parpadeos,
                // el setInterval en segundo plano le pondrá los (Segundos) exactos en el siguiente "tick".
                var $activeBtn = isWhatsApp ? $("#btn-resend-whatsapp-action") : $("#btn-resend-sms-action");
                $activeBtn.prop('disabled', true).addClass('disabled');
            }

            // (Opcional) Si el OTRO canal tiene su cuenta atrás activa en segundo plano, mantenemos su estilo bloqueado
            var otherChannel = isWhatsApp ? 'SMS' : 'WHATSAPP';
            if (userValidatedByDomain.resendTimers[otherChannel]) {
                var $otherBtn = isWhatsApp ? $("#btn-resend-sms-action") : $("#btn-resend-whatsapp-action");
                $otherBtn.prop('disabled', true).addClass('disabled');
            }

            // 6. ACTUALIZAMOS EL MENSAJE DE ERROR DEL VALIDADOR
            $("#phoneCode").rules("add", {
                messages: { required: errorText }
            });

            if ($("#phoneCode-error").length) {
                $("#phoneCode-error").text(errorText);
            }
            // =========================================================

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
    // ORQUESTADOR PRINCIPAL DEL ENVÍO
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

                // Inyección de dependencia: Seleccionamos la estrategia (por defecto sms si no existe)
                var senderStrategy = userValidatedByDomain.communicationStrategies[channel] || userValidatedByDomain.communicationStrategies.sms;

                // Ejecutamos la promesa delegando responsabilidades
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
    // CONTROLADORES DE EVENTOS
    // ==========================================
    sendSMSForPhoneValidation: function(e) {
        if (e != undefined) { e.preventDefault(); }
        var $button = $(this);

        // Leemos el flag del GSP mediante el data-attribute
        var isWhatsAppActive = $button.attr('data-whatsapp-enabled') === 'true';

        // CORRECCIÓN: Asignamos el valor directamente a la memoria del objeto
        userValidatedByDomain.currentChannel = isWhatsAppActive ? 'WHATSAPP' : 'SMS';

        console.log("Paso 1 (Envío) -> Canal guardado en memoria: " + userValidatedByDomain.currentChannel);

        userValidatedByDomain.sendValidationCode($button, userValidatedByDomain.currentChannel);
    },

    handleResendCodeAction: function(e) {
        if (e != undefined) { e.preventDefault(); }
        var $clickedButton = $(this);

        // 1. Identificamos qué botón se ha pulsado por su ID
        var channel = $clickedButton.attr('id') === 'btn-resend-whatsapp-action' ? 'WHATSAPP' : 'SMS';

        // 2. Actualizamos nuestra memoria. ¡Súper importante para la validación posterior!
        userValidatedByDomain.currentChannel = channel;
        console.log("-> Reenvío solicitado. Canal en memoria actualizado a: " + userValidatedByDomain.currentChannel);

        // 3. Ocultamos el panel de ayuda (si estaba abierto) con una animación suave
        if ($("#kuorum-whatsapp-help-panel").length) {
            $("#kuorum-whatsapp-help-panel").slideUp(200);
        }

        // 4. Limpiamos el input del código anterior para que el usuario escriba el nuevo
        $("#phoneCode").val('');

        // =========================================================
        // 5. MEJORA UX: Vaciamos el texto de instrucciones anterior
        // para no confundir al usuario en conexiones lentas.
        // =========================================================
        $(".modal-domain-validation-phone-step2 .help-block").empty();

        // =========================================================
        // 6. MEJORA UX (NUEVO): Limpiamos los mensajes de error de
        // validación (jQuery Validate) y el error global del modal.
        // =========================================================
        $("#phoneCode").removeClass("error"); // Quitamos el borde rojo

        // Buscamos específicamente el label local que crea jQuery Validate sin usar el ID conflictivo
        $("label.error[for='phoneCode']").hide().empty();

        // Limpiamos correctamente el error global de arriba (el del iconito rojo)
        userValidatedByDomain.hideErrorModal();

        // 7. ¡LA MAGIA DE LA REUTILIZACIÓN!
        // Usamos el botón original del Paso 1 ("Enviar código") como "ancla".
        var $originalButton = $("#validatePhoneDomain-modal-form-button-id");
        userValidatedByDomain.sendValidationCode($originalButton, channel);
    },

    // ==========================================
    // RESTO DE MÉTODOS ORIGINALES
    // ==========================================
    handleLoginAndValidationUser: function ($button, callbackNameAfterLogin, clickButtonOnSuccess) {
        var loggedUserAlias = $button.attr("data-loggedUser");
        var noLoggedUser = loggedUserAlias == undefined || loggedUserAlias == "";
        var allowedAnonymousVote = $button.attr("data-allowAnonymousAction") == "true";
        console.log("Handling login and validation");
        if (noLoggedUser && allowedAnonymousVote) {
            console.log("CHECK DOMAIN VALIDATION");
            userValidatedByDomain.initDataValidation($button, clickButtonOnSuccess);
            userValidatedByDomain.checkNoUserValidations($button, clickButtonOnSuccess)
        } else if (noLoggedUser) {
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", callbackNameAfterLogin);
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        } else {
            console.log("User Logged. ");
            userValidatedByDomain.executeClickButtonHandlingValidations($button, clickButtonOnSuccess);
        }
    },

    openAndPrepareValidationModal: function () {
        if (!userValidatedByDomain.dataValidation.allowAnonymousAction){
            this.hide2ndPhoneFields()

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
                noLoggedCallbacks.reloadPage("Validation :: User logged :: Close validation modal", 1000); //1 sec
            })
        }
    },

    closeRegisterModal: function (reloadPageOnCloseValidation) {
        if (($("#registro").data('bs.modal') || {}).isShown) {
            $("#registro").modal("hide");
            if (reloadPageOnCloseValidation) {
                $('#domain-validation').on('hidden.bs.modal', function () {
                    noLoggedCallbacks.reloadPage("Validation :: Close login modal :: Close validation modal", 500)
                })
            }
        }
    },
    hide2ndPhoneFields: function () {
        $(".form-group.form-group-phone.second-phone").hide()
        userValidatedByDomain.are2ndPhoneFieldsHidden = true
    },
    comparePhones: function () {
        var phoneNo1 = $("#phoneNumber").val();
        var phoneNo2 = $("#phoneNumber2").val();
        var arePhonesEquals = phoneNo1 === phoneNo2;
        return arePhonesEquals;
    },
    comparePhonesPrefix: function () {
        var phonePrefixNo1 = $("#phoneNumberPrefix").val();
        var phonePrefixNo2 = $("#phoneNumberPrefix2").val();
        var arePhonesPrefixesEquals = phonePrefixNo1 === phonePrefixNo2;
        return arePhonesPrefixesEquals;
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
            userValidatedByDomain.checkUserValid(executableFunctionCallback)
        } else {
            userValidatedByDomain.checkGroupValidation(executableFunctionCallback)
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
        }
        userValidatedByDomain.successFunctionCallback = callbackSuccess;
    },

    checkNoUserValidations: function ($button) {

        var url = userValidatedByDomain.dataValidation.urlAnonymousValidation;
        console.log(url)
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
                    userValidatedByDomain.validated = true
                }
            },
            error: function () {
                // User is no logged or is not validated
                // Showing modal validation process
                pageLoadingOff();
                display.error(i18n.kuorum.session.validation.error)
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
                    userValidatedByDomain.validated = true
                }
            },
            error:function(){
                // User is no logged or is not validated
                // Showing modal validation process
                pageLoadingOff();
                display.error(i18n.kuorum.session.validation.error)
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    checkGroupValidation:function(executableFunctionCallback, closeCallbackModal){

        var joinCallbacks = new userValidatedByDomain.ExcutableFunctionCallback(function(params) {
            console.log("groupValidationJoined")
            if (executableFunctionCallback != undefined) {
                console.log("Executing callback")
                executableFunctionCallback.exec()
            }
            if (closeCallbackModal != undefined) {
                closeCallbackModal()
            }
        }, "Joined functions -> No need params")
        if (userValidatedByDomain.dataValidation.groupValidation != undefined && userValidatedByDomain.dataValidation.groupValidation != ''){
            userValidatedByDomain._ajaxRemoteCheckGroupValidation(joinCallbacks)
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
                    console.log("Validation Group :: Ok");
                    executableFunctionCallback.exec()
                }else{
                    console.log("Validation Group :: No group");
                    userValidatedByDomain.initVariables();
                    userValidatedByDomain.openAndPrepareValidationModal();
                    userValidatedByDomain.showWarnGroupValidation();
                }
            },
            error:function(){
                display.error(i18n.kuorum.session.validation.groupError)
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    showCensusValidation:function(){
        $("#domain-validation .modal-domain-validation").hide()
        $("#domain-validation .modal-domain-validation-census").show()
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active");
        $("#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-census").addClass("active");
    },

    showCodeValidation:function(){
        $("#domain-validation .modal-domain-validation").hide()
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
        // Clean fields at the beginning of validation

        $("#validationPhoneNumber").val('');
        $("#phoneCode").val('');
        $("#phoneHash").val('');

        userValidatedByDomain.currentChannel = '';

        // Asegurarse de que el panel de ayuda está cerrado si el usuario vuelve atrás
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

            // CORRECCIÓN: Limpiamos basura del DOM y leemos directo de memoria (con un fallback de seguridad a 'SMS')
            var data = {
                campaignId:userValidatedByDomain.dataValidation.campaignId,
                validationPhoneNumber: $("#validationPhoneNumber").val(),
                validationPhoneNumberPrefix: $("#validationPhoneNumberPrefix").val(),
                phoneHash: $("#phoneHash").val(),
                phoneCode: $("#phoneCode").val(),
                channel: userValidatedByDomain.currentChannel || 'SMS'
            };

            console.log("Paso 2 (Validación) -> Enviando código usando canal: " + data.channel);

            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (dataSmsValidation) {
                    userValidatedByDomain.nextValidationStep(dataSmsValidation);
                },
                error: function (dataError) {
                    display.error("Error validating the sms")
                },
                complete: function () {
                    userValidatedByDomain.hideModalLoading()
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
            var url = $form.attr("action")
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
                    display.error("Error validating the sms")
                },
                complete: function () {
                    userValidatedByDomain.hideModalLoading()
                }
            });
        }
    },

    ExcutableFunctionCallback: function (excutable, params){
        this.exec = function(){
            excutable(params)
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
                    display.error("Error validating user")
                },
                complete: function () {
                    userValidatedByDomain.hideModalLoading()
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
        var idError = userValidatedByDomain.modal.find("input:visible").attr("aria-errormessage")
        if (idError != undefined) {
            userValidatedByDomain.modalNotifications.find(".text-danger .text-error-data").attr("id", idError);
        }
        userValidatedByDomain.modalNotifications.find(".text-danger").show();
        userValidatedByDomain.modal.find(".modal-login-action-buttons").show();
        captcha.clearCaptcha()
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
                $("#validationPhoneNumber").val('')
                $("#validationPhoneNumberPrefix").val('')
                setTimeout(function () {
                    console.log("Closing modal")
                    $("#domain-validation").modal("hide")
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
                    $(".modal-domain-validation-phone-step1-predefinedPhone-phone").html(callbackData.pendingValidations.phoneValidation.data.phone)
                }
                userValidatedByDomain.showPhoneValidation();
            }
        }
    }
};
