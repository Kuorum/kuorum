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
    var $submitButton = $('#register-modal-form-id')
    if ($form.valid()){
        var url = $form.attr("action")
        $submitButton.addClass("hidden");
        $.ajax({
            url:url,
            data:$form.serializeArray(),
            success:function(data){
                if(data){
                    display.success(data);
                    setTimeout(function(){
                        $("#registro").modal("hide");
                        $submitButton.removeClass("hidden");
                    }, 2500);
                }
                else{
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