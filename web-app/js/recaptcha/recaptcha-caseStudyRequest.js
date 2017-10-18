$(function(){
    $('#request-case-study-id').on('click', function (e) {
        e.preventDefault()
        var dataRecaptcha = $(this).attr('data-recaptcha');
        recaptchaCaseStudy(dataRecaptcha)
    });
});

function recaptchaCaseStudy(dataRecaptcha){
    grecaptcha.execute(dataRecaptcha);
}

function caseStudyCallback(){
    var $form = $('#request-case-study');
    var dataRecaptcha = $('#request-case-study-id').attr('data-recaptcha');
    if ($form.valid()){
        $form.submit()
    }else{
        grecaptcha.reset(dataRecaptcha);
    }
}