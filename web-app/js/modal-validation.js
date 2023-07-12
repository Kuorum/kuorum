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
            userValidatedByDomain.binded = true
            userValidatedByDomain.modal = $("#domain-validation")
            userValidatedByDomain.modalNotifications = $("#domain-validation .modal-domain-validation-notifications")
        }
    },

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
                // console.log(dataCheckGroupValidation)
                // var jsonValidation = JSON.parse(dataCheckGroupValidation);
                // console.log(dataCheckGroupValidation.belongsToCampaignGroup)
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
                // User is no logged or is not validated
                // Showing modal validation process
                // pageLoadingOff();
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
    showPhoneValidationStep2:function(e){
        if (e != undefined){e.preventDefault();}
        $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step1").hide();
        $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step2").show();
        $("#domain-validation .modal-domain-validation-step-tabs li").removeClass("active");
        $("#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-phoneCode").addClass("active");
        $("#phoneCode").val("");
        userValidatedByDomain.hideModalLoading();
        userValidatedByDomain.hideErrorModal();
    },

    showWarnGroupValidation:function(){
        $("#domain-validation .modal-domain-validation").hide();
        userValidatedByDomain.hideErrorModal();
        $("#domain-validation .modal-domain-validation-groupCampaign").show();
    },

    sendSMSForPhoneValidation:function(e) {
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");

        function successCaptchaCallback() {
            if (!userValidatedByDomain.arePhonesAndPrefixEquals()) {
                userValidatedByDomain.showErrorModal(i18n.inputs.errors.nonMatchingPhones);
            } else if ($form.valid() && captcha.isRecaptchaSolved) {
                userValidatedByDomain.showModalLoading();
                var url = $form.attr("action");
                console.log("CampaignID: " + userValidatedByDomain.dataValidation.campaignId);
                var data = {
                    campaignId: userValidatedByDomain.dataValidation.campaignId,
                    phoneNumberPrefix: $("#phoneNumberPrefix").val(),
                    phoneNumber: $("#phoneNumber").val(),
                    'g-recaptcha-response': captcha.grecaptchaResponse
                };
                $.ajax({
                    type: "POST",
                    url: url,
                    data: data,
                    success: function (dataSms) {
                        console.log(dataSms)
                        if (dataSms.success) {
                            captcha.clearCaptcha()
                            $("#phoneHash").val(dataSms.hash)
                            $("#validationPhoneNumber").val(dataSms.validationPhoneNumber)
                            $("#validationPhoneNumberPrefix").val(dataSms.validationPhoneNumberPrefix)
                            if (dataSms.validated) {
                                // This phone is already validated
                                userValidatedByDomain.nextValidationStep(dataSms);
                            } else {
                                userValidatedByDomain.showPhoneValidationStep2();
                            }
                        } else {
                            userValidatedByDomain.showErrorModal(dataSms.msg)
                        }
                    },
                    error: function (dataError) {
                        display.error("There was an error sending a sms validation to your phone number")
                    },
                    complete: function () {
                        pageLoadingOff();
                        // userValidatedByDomain.hideModalLoading()
                    }
                });
            }
        }

        captcha.showCaptcha(successCaptchaCallback)
    },

    handleSubmitValidationPhone:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        if ($form.valid()) {
            userValidatedByDomain.showModalLoading();
            var url = $form.attr("action");
            var data = {
                campaignId:userValidatedByDomain.dataValidation.campaignId,
                validationPhoneNumber: $("#validationPhoneNumber").val(),
                validationPhoneNumberPrefix: $("#validationPhoneNumberPrefix").val(),
                phoneHash: $("#phoneHash").val(),
                phoneCode: $("#phoneCode").val()
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
                    // pageLoadingOff();
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
    },

    hideErrorModal:function(){
        userValidatedByDomain.modalNotifications.hide();
        userValidatedByDomain.modalNotifications.find(".text-danger").hide();
        userValidatedByDomain.modal.find(".modal-login-action-buttons").show();
    },

    nextValidationStep: function(callbackData){
        // Success is 200 code No
        console.log("Next step :: init");
        if (callbackData.validated) {
            console.log("Next step :: Validated")
            userValidatedByDomain.hideErrorModal();
            userValidatedByDomain.validated = true;
            $("#validateDomain-modal-form-button-id").find(".text-success").show();
            userValidatedByDomain.checkGroupValidation(userValidatedByDomain.successFunctionCallback, function () {
                setTimeout(function () {
                    console.log("Closing modal")
                    $("#domain-validation").modal("hide")
                }, 1000);
            });
        }else if (!callbackData.success){ // ERRORS ON AJAX CALL
            // RESTORE STATUS
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


// function test(args){
//     console.log(args)
// }
// var executable = new userValidatedByDomain.ExcutableFunctionCallback(test, {var1:"hola"})
//
// executable.exec()