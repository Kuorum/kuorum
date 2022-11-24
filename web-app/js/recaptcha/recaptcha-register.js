$(document).ready(function () {
    $('input[name=name]').focus();

    $('#register-submit').on('click', manageClicRecapcha);

    $('#join-code-submit').on('click', manageClicRecapcha);
});

function recaptchaRegister(dataRecaptcha) {
    grecaptcha.execute(dataRecaptcha);

}


function manageClicRecapcha(e) {
    e.preventDefault();
    var dataRecaptcha = $(this).attr('data-recaptcha');
    recaptchaRegister(dataRecaptcha);
}

function registerCallback() {
    var $form = $('[name=sign]');
    var dataRecaptcha = $('#register-submit').attr('data-recaptcha');
    if ($form.valid()) {
        $form.submit()
    } else {
        grecaptcha.reset(dataRecaptcha);
    }
}

function joinRegisterCallback() {
    var $form = $('[name=sign]');
    var dataRecaptcha = $('#join-code-submit').attr('data-recaptcha');
    if ($form.valid()) {
        window.location +="/"+$form.find('input[name="joinCode"]').val()
    } else {
        grecaptcha.reset(dataRecaptcha);
    }
}
