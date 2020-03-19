

var userValidatedByDomain={

    executable : undefined,
    binded:false,
    validated: false,
    modalNotifications: undefined,
    modal: undefined,
    loading:undefined,

    initVariables:function(){
        if(!userValidatedByDomain.binded){
            $("#validateDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationForm );
            $("#validatePhoneDomain-modal-form-button-id").on("click",userValidatedByDomain.sendSMSForPhoneValidation );
            $("#validatePhoneCodeDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationPhone );
            $("#validatePhoneCodeDomain-modal-form-button-back").on("click",userValidatedByDomain.showPhoneValidationStep1 );
            $("#validateCustomCodeDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationCustomCode );
            userValidatedByDomain.binded = true
            userValidatedByDomain.modal = $("#domain-validation")
            userValidatedByDomain.modalNotifications = $("#domain-validation .modal-domain-validation-notifications")
        }
    },

    openAndPrepareValidationModal:function(){
        // Open validation modal
        $("#domain-validation").modal({
            backdrop: 'static',
            keyboard: false
        });

        // CLOSE LOGIN MODAL IF IT IS OPEN
        if (($("#registro").data('bs.modal') || {}).isShown){
            $("#registro").modal("hide");
            $('#domain-validation').on('hidden.bs.modal', function () {
                noLoggedCallbacks.reloadPage()
            })
        }

        if (!isUserLogged()){
            // User is logged but the page is not reloaded
            $('#domain-validation').on('hidden.bs.modal', function () {
                if (!userValidatedByDomain.validated){
                    // Delay reload to show the error message
                    display.error(i18n.kuorum.web.commands.profile.DomainValidationCommand.closeWithoutValidation);
                }
                setTimeout(function(){noLoggedCallbacks.reloadPage() }, 1000); //1 sec
            })
        }
    },
    checkUserValid:function(userId, executableFunctionCallback){
        var url = kuorumUrls.profileDomainValidationChecker;
        var data = {};
        executable = executableFunctionCallback;
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                console.log(dataLogin)
                userValidatedByDomain.initVariables();
                userValidatedByDomain.nextValidationStep(dataLogin);
                if (!dataLogin.validated){
                    userValidatedByDomain.openAndPrepareValidationModal();
                }else{
                    userValidatedByDomain.validated = true
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
    showPhoneValidation:function(){
        $("#domain-validation .modal-domain-validation").hide()
        $("#domain-validation .modal-domain-validation-phone").show();
        userValidatedByDomain.showPhoneValidationStep1();
    },
    showPhoneValidationStep1:function(e){
        if (e != undefined){e.preventDefault();}
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
        userValidatedByDomain.hideModalLoading();
        userValidatedByDomain.hideErrorModal();
    },

    sendSMSForPhoneValidation:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        if ($form.valid()) {
            userValidatedByDomain.showModalLoading();
            var url = $form.attr("action");
            var data = {
                phoneNumberPrefix: $("#phoneNumberPrefix").val(),
                phoneNumber: $("#phoneNumber").val()
            };
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (dataSms) {
                    if (dataSms.success){
                        $("#phoneHash").val(dataSms.hash)
                        userValidatedByDomain.showPhoneValidationStep2();
                    }else{
                        userValidatedByDomain.showErrorModal(dataSms.msg)
                    }
                },
                error: function (dataError) {
                    display.error("There was an error sending a sms validation to your phone number")
                },
                complete: function () {
                    // userValidatedByDomain.hideModalLoading()
                }
            });
        }
    },

    handleSubmitValidationPhone:function(e){
        e.preventDefault();
        var $button = $(this);
        var $form = $button.closest("form");
        if ($form.valid()) {
            userValidatedByDomain.showModalLoading();
            var url = $form.attr("action");
            var data = {
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
        var data = $form.serialize();
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
        console.log(callbackData);
        if (callbackData.validated) {
            userValidatedByDomain.hideErrorModal();
            executable.exec();
            userValidatedByDomain.validated = true;
            $("#validateDomain-modal-form-button-id").find(".text-success").show();
            setTimeout(function () {
                $("#domain-validation").modal("hide")
            }, 1000);
        }else if (!callbackData.success){ // ERRORS ON AJAX CALL
            // RESTORE STATUS
            userValidatedByDomain.showErrorModal(callbackData.msg)
        }else{
            if (!callbackData.pendingValidations.censusValidation){
                userValidatedByDomain.showCensusValidation();
            }else if (!callbackData.pendingValidations.codeValidation){
                userValidatedByDomain.showCodeValidation();
            }else if (!callbackData.pendingValidations.phoneValidation){
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