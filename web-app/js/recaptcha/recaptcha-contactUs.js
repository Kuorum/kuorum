$(function(){
    $('#contact-us-form-id').on('click', function (e) {
        e.preventDefault()
        $('fieldset.email-sent .in-progress').removeClass('hidden');
        var dataRecaptcha = $(this).attr('data-recaptcha');
        recaptchaContactUs(dataRecaptcha)
    });
});

function recaptchaContactUs(dataRecaptcha){
    grecaptcha.execute(dataRecaptcha);
}

function contactUsCallback(){
    var $form = $('#request-demo-form');
    var dataRecaptcha = $('#contact-us-form-id').attr('data-recaptcha');
    var $submitButton = $("#contact-us-form-id")
    if ($form.valid()){
        var url = $form.attr("action")
        $submitButton.parents(".form-group").addClass("hidden")
        $.ajax({
            url:url,
            data:$form.serializeArray(),
            success:function(data){
                if(data){
                    display.success(data);

                    $('fieldset.email-sent .error').addClass("hidden");
                    $('fieldset.email-sent .in-progress').addClass('hidden');
                    $('fieldset.email-sent .sent').removeClass("hidden");
                }
                else{
                    $('fieldset.email-sent .in-progress').addClass('hidden');
                    $('fieldset.email-sent .error').removeClass("hidden");
                }
            }
        })
    }
    else{
        grecaptcha.reset(dataRecaptcha);
        $('fieldset.email-sent .in-progress').addClass('hidden');
        $submitButton.parents(".form-group").removeClass("hidden")
    }
}