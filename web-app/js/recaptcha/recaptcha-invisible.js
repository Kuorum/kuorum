
var onloadRecaptchaCallback = function () {

    $('button.g-recaptcha').each(function () {
        var callbackName = $(this).attr('data-callback');
        var uuid = guid();
        var $recaptchaDiv = '<div id="'+ uuid +'"></div>';

        $(this).parent().append($recaptchaDiv);

        var recaptcha = grecaptcha.render(uuid, {
            'sitekey' : kuorumKeys._googleCaptchaKey,
            'size' : 'invisible',
            'callback' : callbackName
        });

        $(this).attr('data-recaptcha', recaptcha);
    });

};
