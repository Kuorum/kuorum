$(document).ready(function() {
    $('input[name=name]').focus();

    $('#register-submit').on('click', function (e) {
        e.preventDefault();
        var dataRecaptcha = $(this).attr('data-recaptcha');
        recaptchaRegister(dataRecaptcha);
    });
});

function recaptchaRegister(dataRecaptcha){
    grecaptcha.execute(dataRecaptcha);
}

function registerCallback(){
    var $form = $('#sign');
    var dataRecaptcha = $('#register-submit').attr('data-recaptcha');
    if($form.valid()){
        $form.submit()
    }
    else{
        grecaptcha.reset(dataRecaptcha);
    }
}