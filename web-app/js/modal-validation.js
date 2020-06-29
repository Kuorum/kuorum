

var userValidatedByDomain={

    executable : undefined,
    binded:false,
    validated: false,
    modalNotifications: undefined,
    modal: undefined,
    loading:undefined,
    dataValidation: undefined,

    initVariables:function(){
        if(!userValidatedByDomain.binded){
            $("#validateDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationForm );
            $("#validatePhoneDomain-modal-form-button-id").on("click",userValidatedByDomain.sendSMSForPhoneValidation );
            $("#validatePhoneCodeDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationPhone );
            $("#validatePhoneCodeDomain-modal-form-button-back").on("click",userValidatedByDomain.showPhoneValidationStep1 );
            $("#validateCustomCodeDomain-modal-form-button-id").on("click",userValidatedByDomain.handleSubmitValidationCustomCode );
            $("#groupValidationCampaign-modal-button-id").on("click",function(e){
                e.preventDefault();
                $("#domain-validation").modal("hide");
            });
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
                noLoggedCallbacks.reloadPage("Validation :: Close login modal :: Close validation modal",500)
            })
        }

        if (!isUserLogged()){
            // User is logged but the page is not reloaded
            $('#domain-validation').on('hidden.bs.modal', function () {
                if (!userValidatedByDomain.validated){
                    // Delay reload to show the error message
                    display.error(i18n.kuorum.web.commands.profile.DomainValidationCommand.closeWithoutValidation);
                }
                noLoggedCallbacks.reloadPage("Validation :: User logged :: Close validation modal",1000); //1 sec
            })
        }
    },

    executeClickButtonHandlingValidations:function($button, executableFunctionCallback){
        userValidatedByDomain.dataValidation ={
            loggedUser : $button.attr('data-useralias'), // No needed
            validationActive : $button.attr('data-campaignValidationActive'),
            groupValidation : $button.attr('data-campaignGroupValidationActive'),
            campaignId : $button.attr('data-campaignId'),
            predefinedPhone : false,
        }
        if (userValidatedByDomain.dataValidation.validationActive=="true"){
            userValidatedByDomain.checkUserValid(executableFunctionCallback)
        }else{
            userValidatedByDomain.checkGroupValidation(executableFunctionCallback)
        }
    },

    checkUserValid:function(executableFunctionCallback){
        var url = kuorumUrls.profileDomainValidationChecker;
        var data = userValidatedByDomain.dataValidation;
        executable = executableFunctionCallback;
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
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
    checkGroupValidation:function(executableFunctionCallback, closeCallbackModal){
        console.log("Check Group Validation")
        if (userValidatedByDomain.dataValidation.groupValidation != undefined && userValidatedByDomain.dataValidation.groupValidation != ''){
            console.log("No group")
            userValidatedByDomain._ajaxRemoteCheckGroupValidation(executableFunctionCallback)
        }else{
            executableFunctionCallback.exec()
            if (closeCallbackModal != undefined){
                closeCallbackModal()
            }
        }
    },
    _ajaxRemoteCheckGroupValidation:function(executableFunctionCallback){
        var url =userValidatedByDomain.dataValidation.groupValidation;
        $.ajax({
            type: "POST",
            url: url,
            data: userValidatedByDomain.dataValidation,
            success: function (dataCheckGroupValidation) {
                console.log(dataCheckGroupValidation)
                // var jsonValidation = JSON.parse(dataCheckGroupValidation);
                console.log(dataCheckGroupValidation.belongsToCampaignGroup)
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
        $("#domain-validation .modal-domain-validation").hide();
        $("#domain-validation .modal-domain-validation-phone").show();
        if ($("phoneHash").val()=="" || $("phoneHash").val() == undefined){
            userValidatedByDomain.showPhoneValidationStep1();
        }else{
            userValidatedByDomain.showPhoneValidationStep2();
        }
    },
    showPhoneValidationStep1:function(e){
        if (e != undefined){e.preventDefault();}
        if (userValidatedByDomain.dataValidation.predefinedPhone){
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
                    console.log(dataSms)
                    if (dataSms.success){
                        $("#phoneHash").val(dataSms.hash)
                        $("#validationPhoneNumber").val(dataSms.validationPhoneNumber)
                        $("#validationPhoneNumberPrefix").val(dataSms.validationPhoneNumberPrefix)
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
        console.log("Next step");
        if (callbackData.validated) {
            console.log("Validated")
            userValidatedByDomain.hideErrorModal();
            userValidatedByDomain.validated = true;
            $("#validateDomain-modal-form-button-id").find(".text-success").show();
            userValidatedByDomain.checkGroupValidation(executable, function(){
                setTimeout(function () {
                    $("#domain-validation").modal("hide")
                }, 1000);
            });
        }else if (!callbackData.success){ // ERRORS ON AJAX CALL
            // RESTORE STATUS
            console.log("Error")
            userValidatedByDomain.showErrorModal(callbackData.msg)
        }else{
            if (!callbackData.pendingValidations.censusValidation.success){
                console.log("Show Census validation")
                userValidatedByDomain.showCensusValidation();
            }else if (!callbackData.pendingValidations.codeValidation.success){
                console.log("Show Code validation")
                userValidatedByDomain.showCodeValidation();
            }else if (!callbackData.pendingValidations.phoneValidation.success){
                if (callbackData.pendingValidations.phoneValidation.data.predefinedPhone){
                    userValidatedByDomain.dataValidation.predefinedPhone=true;
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