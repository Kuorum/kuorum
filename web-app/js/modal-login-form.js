$(document).ready(function(){
    // cambio de formulario Entrar/Registro
    $('body').on('click','.change-home-register', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $('form#login-modal').hide();
        $('form#pass-forget').hide();
        $('form#signup-modal').show();
    });

    $('body').on('click','.change-home-login', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $('form#signup-modal').hide();
        $('form#pass-forget').hide();
        $('form#login-modal').show();
    });

    $('body').on('click','.change-home-forgot-password', function(e) {
        e.stopPropagation();
        e.preventDefault();
        $('form#signup-modal').hide();
        $('form#login-modal').hide();
        $('form#pass-forget').show();
    });

    // al hacer clic en el botón "Regístrate" de la Home cambio el orden de aparición
    // natural de los formularios
    $('body').on('click','.open-sign-form', function() {
        $('form#login-modal').fadeOut('fast');
        $('form#sign-modal').fadeIn('fast');
    });
    $('body').on('click','.homeMore.two .btn-blue', function(e) {
        $('form#login-modal').fadeIn('fast');
        $('form#sign-modal').fadeOut('fast');
    });

    // Restore normal appearance on modal
    $('#registro').on('show.bs.modal', function(){
        $('form#login-modal').hide();
        $('form#pass-forget').hide();
        $("#pass-forget-success").hide();

        $('form#signup-modal').show();
        $('.socialGo').show();
    });

    $('#registro form[name=login-header] input[type=submit]').on('click', function(e){
        e.preventDefault();
        e.stopPropagation();
        var $form = $(this).parents("form[name=login-header]");
        var callback = $form.attr("callback")
        var callbackFunction = noLoggedCallbacks[callback]
        if (noLoggedCallbacks[callback] == undefined){
            callbackFunction = noLoggedCallbacks.reloadPage
        }
        modalLogin($form, callbackFunction);
    });


    $('#registro form[name=pass-forget] input[type=submit]').on('click', function(e){
        e.preventDefault();
        e.stopPropagation();
        var $form = $(this).parents("form[name=pass-forget]");
        modalForgotPassword($form);
    });
});


function modalLogin($form, callback){
    pageLoadingOn("Modal login");
    if ($form.valid()) {
        $form.parents(".modal").modal("hide")
        var url = $form.attr("action")
        var data = {
            j_username: $form.find("input[name=j_username]").val(),
            j_password: $form.find("input[name=j_password]").val()
        };
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                console.log(dataLogin)
                if (dataLogin.success) {
                    callback()
                } else {
                    // Form validation doesn't allow to take this conditional branch
                    display.error(dataLogin.error)
                    pageLoadingOff("Modal login");
                    // document.location.href = dataLogin.url
                }
            },
            complete: function () {
                // pageLoadingOff();
            }
        });
    } else {
        pageLoadingOff("Modal login");
    }
}

function modalForgotPassword($form){
    pageLoadingOn("Modal forgot password");
    if ($form.valid()) {
        var url = $form.attr("action")
        var data = {
            email: $form.find("input[name=email]").val()
        };
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function (dataLogin) {
                $form.hide();
                $('.socialGo').hide();
                $("#pass-forget-success").show();
            },
            complete: function () {
                pageLoadingOff("Modal forgot password");
            }
        })
    } else {
        pageLoadingOff("Modal forgot password");
    }
};