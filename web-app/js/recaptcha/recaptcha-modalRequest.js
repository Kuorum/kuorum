$(function () {
    $('#request-demo-modal-form-id').on('click', function (e) {
        e.preventDefault()
        var dataRecaptcha = $(this).attr('data-recaptcha');
        recaptchaModal(dataRecaptcha)
    });
});


function recaptchaModal(dataRecaptcha){
    grecaptcha.execute(dataRecaptcha);
}

function requestDemoCallback(){
    var $form = $('#request-demo-modal-form');
    var dataRecaptcha = $('#request-demo-modal-form-id').attr('data-recaptcha');
    var $submitButton = $('#request-demo-modal-form-id')
    if ($form.valid()){
        $('fieldset.email-sent .in-progress').removeClass('hidden');
        var url = $form.attr("action")
        $submitButton.addClass("hidden");
        $.ajax({
            url:url,
            data:$form.serializeArray(),
            success:function(data){
                if(data){
                    display.success(data);
                    $('fieldset.email-sent .error').addClass("hidden");
                    $('fieldset.email-sent .in-progress').addClass('hidden');
                    $('fieldset.email-sent .sent').removeClass("hidden");
                    setTimeout(function(){
                        $('fieldset.email-sent .sent').addClass("hidden");
                        $("#request-demo-modal").modal("hide");
                        $submitButton.removeClass("hidden");
                    }, 2500);
                }
                else{
                    $('fieldset.email-sent .in-progress').addClass('hidden');
                    $('fieldset.email-sent .error').removeClass("hidden");
                    $submitButton.removeClass("hidden")
                }
            }
        })
    }
    else {
        grecaptcha.reset(dataRecaptcha);
        $submitButton.removeClass("hidden")
    }
}