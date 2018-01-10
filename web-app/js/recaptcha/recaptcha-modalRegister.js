$(function () {
    $('#register-modal-form-id').on('click', function (e) {
        e.preventDefault()
        var dataRecaptcha = $(this).attr('data-recaptcha');
        recaptchaModal(dataRecaptcha)
    });
});


function recaptchaModal(dataRecaptcha){
    grecaptcha.execute(dataRecaptcha);
}

function registerModalCallback(){
    var $form = $('#signup-modal');
    var dataRecaptcha = $('#register-modal-form-id').attr('data-recaptcha');
    console.log(dataRecaptcha)
    var $submitButton = $('#register-modal-form-id')
    if ($form.valid()){
        var callback = $form.attr("callback")
        var callbackFunction = noLoggedCallbacks[callback]
        if (noLoggedCallbacks[callback] == undefined){
            callbackFunction = noLoggedCallbacks.reloadPage
        }
        modalRegister($form, callbackFunction);
    }
    else {
        grecaptcha.reset(dataRecaptcha);
        $submitButton.removeClass("hidden")
    }
}

function modalRegister($form, callback){
    pageLoadingOn("Modal register");
    if ($form.valid()) {

        $form.parents(".modal").modal("hide")
        var url = $form.attr("action-ajax")
        var data = $form.serializeArray()
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                if (dataLogin.success) {
                    callback()
                } else {
                    // Form validation doesn't allow to take this conditional branch
                    display.error(dataLogin.error)
                    pageLoadingOff("Modal register");
                    // $form.submit() // Goes to register page using normal flow and handling errors
                }
            },
            error:function( jqXHR, textStatus, errorThrown ){
                pageLoadingOff("Modal register");
            },
            complete: function () {
                var dataRecaptcha = $('#register-modal-form-id').attr('data-recaptcha');
                grecaptcha.reset(dataRecaptcha);
            }
        });
    } else {
        var dataRecaptcha = $('#register-modal-form-id').attr('data-recaptcha');
        grecaptcha.reset(dataRecaptcha);
        pageLoadingOff("Modal register");
    }
}