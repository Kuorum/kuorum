

var userValidatedByDomain={

    executable : undefined,
    binded:false,
    checkUserValid:function(userId, executableFunctionCallback){
        var url = kuorumUrls.profileValidByDomainChecker;
        var data = {};
        executable = executableFunctionCallback;
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                console.log(dataLogin)
                userValidatedByDomain.nextValidationStep(dataLogin);
                if (!dataLogin.validated){
                    // INIT BUTTONS
                    if(!userValidatedByDomain.binded){
                        $("#validateDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationForm );
                        $("#validatePhoneDomain-modal-form-button-id").on("click",userValidatedByDomain.sendSMSForPhoneValidation );
                        $("#validatePhoneCodeDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationPhone );
                        userValidatedByDomain.binded = true
                    }

                    // SHOW DOMAIN VALIDATION MODAL
                    $("#domain-validation").modal("show");

                    // CLOSE LOGIN MODAL IF IT IS OPEN
                    if (($("#registro").data('bs.modal') || {}).isShown){
                        $("#registro").modal("hide");
                        $('#domain-validation').on('hidden.bs.modal', function () {
                            noLoggedCallbacks.reloadPage()
                       })
                    }else if (!isUserLogged()){
                        // User is logged but the page is not reloaded
                        $('#domain-validation').on('hidden.bs.modal', function () {
                            console.log("DISPLAY ERROR")
                            // display.error(i18n.kuorum.web.commands.profile.DomainValidationCommand.validationError);
                            // Delay reload to show the error message
                            setTimeout(function(){noLoggedCallbacks.reloadPage() }, 1000); //1 sec
                        })
                    }
                }
            },
            error:function(){
                // User is no logged or is not validated
                // Showing modal validation process
                // pageLoadingOff();
                display.error("Error checking validation")
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },

    showPhoneValidation:function(){
        $("#domain-validation .modal-domain-validation-census").hide()
        $("#domain-validation .modal-domain-validation-phone").show()
    },
    showCensusValidation:function(){
        $("#domain-validation .modal-domain-validation-census").show()
        $("#domain-validation .modal-domain-validation-phone").hide()
    },

    sendSMSForPhoneValidation:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        var $loading = $button.siblings(".loading");
        if ($form.valid()) {
            $loading.show();
            $button.hide();
            var url = kuorumUrls.profileValidPhoneByDomainSendSms;
            var data = {phoneNumber: $("#phoneNumber").val()};
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (dataSms) {
                    $("#phoneHash").val(dataSms.hash)
                    $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step1").hide()
                    $("#domain-validation .modal-domain-validation-phone .modal-domain-validation-phone-step2").show()
                },
                error: function (dataError) {
                    display.error("There was an error sending a sms validation to your phone number")
                },
                complete: function () {
                    // pageLoadingOff();
                }
            });
        }
    },

    handleSubmitValidationPhone:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        if ($form.valid()) {
            var $loading = $button.siblings(".loading");
            $loading.show();
            $button.hide();
            var url = kuorumUrls.profileValidPhoneByDomainValidate;
            var data = {
                phoneHash: $("#phoneHash").val(),
                phoneCode: $("#phoneCode").val()
            };
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (dataSmsValidation) {
                    userValidatedByDomain.nextValidationStep(dataSmsValidation, $button);
                },
                error: function (dataError) {
                    display.error("Error validating the sms")
                },
                complete: function () {
                    // pageLoadingOff();
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
        var data = $form.serialize();
        var $loading = $button.siblings(".loading");
        if ($form.valid()){
            $loading.show();
            $button.hide();

            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (data) {
                    userValidatedByDomain.nextValidationStep(data, $button);
                },
                error:function(){
                    // Wrong user validation
                    display.error("Error validating user")
                },
                complete: function () {
                    $loading.hide()
                    // pageLoadingOff();
                }
            });
        }
    },

    nextValidationStep: function(callbackData, $button){
        // Success is 200 code No
        console.log(callbackData);
        if (callbackData.validated) {
            executable.exec();
            $("#validateDomain-modal-form-button-id").siblings(".text-success").show();
            setTimeout(function () {
                $("#domain-validation").modal("hide")
            }, 1000);
        }else if (!callbackData.success){ // ERRORS ON AJAX CALL
            // RESTORE STATUS
            if ($button != undefined){
                $button.show();
                var $loading = $button.siblings(".loading");
                $loading.hide();
            }
            display.error(callbackData.msg)
        }

        if (!callbackData.pendingValidations.censusValidation){
            userValidatedByDomain.showCensusValidation();
        }else if (!callbackData.pendingValidations.phoneValidation){
            userValidatedByDomain.showPhoneValidation();
        }
    }
};


// function test(args){
//     console.log(args)
// }
// var executable = new userValidatedByDomain.ExcutableFunctionCallback(test, {var1:"hola"})
//
// executable.exec()