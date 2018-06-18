import grails.util.Holders

modules = {

    basic{
        resource url: 'css/bootstrap.min.css'
        resource url: 'fonts/font-awesome/css/font-awesome.min.css'
        resource url: 'fonts/icomoon/styles.css'
        resource url: 'fonts/icomoon2/styles.css'
        resource url: 'fonts/icomoon3/styles.css'
        resource url:'js/jquery.min.js'
        resource url:'js/bootstrap.min.js'
        resource url:[dir:'css',file:'style_ie.css'], attrs:[media:'screen'], wrapper: { s -> "<!--[if IE]>$s<![endif]-->" }
        resource url:[dir:'css',file:'style_ie9.css'], attrs:[media:'screen'], wrapper: { s -> "<!--[if IE 9]>$s<![endif]-->" }
        resource url:[dir:'css',file:'style_ie8.css'], attrs:[media:'screen'], wrapper: { s -> "<!--[if IE 8]>$s<![endif]-->" }
        // Soporte HTML5 y pseudo-clases CSS3 para IE9 e inferior
        resource url:[dir:'js',file:'respond.min.js'], wrapper: { s -> "<!--[if (lt IE 9) & (!IEMobile)]>$s<![endif]-->" }
        resource url:[dir:'js',file:'selectivizr.js'], wrapper: { s -> "<!--[if (lt IE 9) & (!IEMobile)]>$s<![endif]-->" }
        // Fin soporte HTML5
//        resource url:'js/modernizr.js'
        resource url:'css/custom.css'
    }

    recaptcha{
        resource url: 'https://www.google.com/recaptcha/api.js?onload=onloadRecaptchaCallback&render=explicit'
        resource url: 'js/recaptcha/recaptcha-invisible.js'
    }

    recaptcha_caseStudyRequest{
        dependsOn 'recaptcha'
        resource url: 'js/recaptcha/recaptcha-caseStudyRequest.js'
    }

    recaptcha_contactUs{
        dependsOn 'recaptcha'
        resource url: 'js/recaptcha/recaptcha-contactUs.js'
    }

    recaptcha_modalRequest{
        dependsOn 'recaptcha'
        resource url: 'js/recaptcha/recaptcha-modalRequest.js'
    }

    recaptcha_register{
        dependsOn 'recaptcha'
        resource url: 'js/recaptcha/recaptcha-register.js'
    }

    recaptcha_modalRegister{
        dependsOn 'recaptcha'
        resource url: 'js/recaptcha/recaptcha-modalRegister.js'
    }

    lang_es{
        dependsOn 'application'
        resource url:'js/timeago/jquery.timeago.es.js'
    }

    lang_en{
        dependsOn 'application'
        resource url:'js/timeago/jquery.timeago.en.js'
    }

    lang_de{
        dependsOn 'application'
        resource url:'js/timeago/jquery.timeago.de.js'
    }

    lang_it{
        dependsOn 'application'
        resource url:'js/timeago/jquery.timeago.it.js'
    }

    lang_lt{
        dependsOn 'application'
        resource url:'js/timeago/jquery.timeago.lt.js'
    }
    lang_ca{
        dependsOn 'application'
        resource url:'js/timeago/jquery.timeago.ca.js'
    }

    datepicker{
        dependsOn 'basic'
        resource url: 'css/datepicker3.css'
        resource url:'js/datepicker/bootstrap-datepicker.js'
        resource url:'js/datepicker/moment-with-locales.min.js'
        resource url:'js/datepicker/bootstrap-datetimepicker.js'
        resource url:'js/datepicker/bootstrap-datepicker.es.js'
        resource url:'js/datepicker/bootstrap-datepicker.de.js'
        resource url:'js/datepicker/bootstrap-datepicker.it.js'
        resource url:'js/datepicker/bootstrap-datepicker.lt.js'
    }

    noty{
        dependsOn("basic")
        resource url:'js/jquery.noty.packaged.min.js'
        resource url:'js/jquery.noty.theme.js'
        resource url:'js/layouts/top.js'
        resource url:'js/layouts/center.js'
    }

    application {
        dependsOn "basic,noty"
        resource url:'js/jquery.validate.min.js'
        resource url:'js/jquery.timeago.js'
        resource url:'js/bootstrap-progressbar.min.js'
        resource url:'js/jquery.slimscroll.min.js'
        resource url:'js/jquery.touchSwipe.min.js'
        resource url:'js/jquery.autocomplete.js'
//        resource url:'js/jquery.bgswitcher.js' //HOME
        resource url:'js/bootstrap-filestyle.min.js'
        resource url:'js/isotope.pkgd.min.js'
        resource url:'js/jquery.mark.min.js'
        resource url:'js/mark.min.js'
        resource url:'js/hideShowPassword.min.js'
        resource url:'js/youtube-helper.js'
        resource url:'js/custom.js'
        resource url:'js/modal-login-form.js'
        resource url:'js/local.js'
        resource url:'js/profile.js'
    }

    event {
        dependsOn("basic")
        resource url:'js/event.js'
    }

    debate {
        dependsOn("basic", "forms", 'cookiesHelper', 'application', 'campaignList')
        resource url:'js/readmore.min.js'
        resource url:'js/medium-editor/medium-editor.js'
        resource url:'css/medium-editor/medium-editor.min.css'
        resource url:'css/medium-editor/themes/default.css'
        resource url:'js/medium-editor/medium-mentions.js'
        resource url:'css/medium-editor/mention-panel.min.css'
        resource url:'js/debate.js'
    }

    post {
        dependsOn("basic", "forms")
        resource url:'js/post.js'
    }

    survey{
        dependsOn("basic")
        resource url:'js/survey.js'
    }

    causes {
        dependsOn("basic")
        resource url:'js/causes.js'
    }

    widget{
        dependsOn("basic", 'cookiesHelper')
        resource url:'css/customWidget.css'
    }

    widgetResizer {
        dependsOn("widget")
        resource url:'js/widget/widget-helper.js'
        resource url:'js/widget/iframe-resizer/iframeResizer.contentWindow.min.js'
    }

    campaignList{
        dependsOn("basic")
        resource url: 'js/list.min.js'
        resource url: 'js/list.pagination.min.js'
        resource url: 'js/campaigns/campaign-list.js'
    }

    campaignForm{
        dependsOn("basic", "forms")
        resource url: 'js/camapign-form.js'
    }

    contactUsForm{
        dependsOn("basic","forms")
        resource url: 'js/contact-us-form.js'
    }

    debateForm {
        dependsOn("campaignForm")
        resource url:'js/debate-form.js'
    }

    postForm {
        dependsOn("campaignForm")
        resource url: 'js/debate-form.js'
    }

    newsletter{
        dependsOn("campaignForm")
        resource url:'js/newsletter-form.js'

    }

    forms{
        dependsOn("datepicker")
//        resource url:'js/jquery-ui/jquery-ui-1.10.4.custom/js/jquery-ui-1.10.4.custom.js'
//        resource url:'js/jquery-ui/jquery-ui-1.10.4.custom/css/base/jquery-ui-1.10.4.custom.min.css'
//        resource url:'js/icheck.min.js' //  CHECKBOX COMMISSIONS  -> check bonito

        resource url:'js/typeahead.bundle.min.js' // PARA el input tag con autocompletado
        resource url:'js/bootstrap-tagsinput.min.js' // PARA el input tag con autocompletado
        resource url:'js/jquery-plugin/tagsinput/jquery.tagsinput.css'
        resource url:'js/jquery-plugin/tagsinput/jquery.tagsinput.js'
        resource url:'js/jquery.dynamiclist.min.js' //Dynamic input
        resource url:'js/jquery-plugin/jquery.are-you-sure.js'
        resource url:'js/jquery-plugin/duplicateFields.min.js'
        resource url:'js/jquery-te-1.4.0.min.js'
        resource url:'css/jquery-te-1.4.0.css'
        resource url:'js/customForm.js'
        resource url:'css/customForms.css'
    }

    surveyForms{
        dependsOn("campaignForm")
        resource url: 'js/survey-form.js'
    }

    contacts{
        dependsOn("basic", "forms")
        resource url:'js/contacts.js'
    }

    findUploader{
        dependsOn("basic")
        resource url : 'js/fineUploader/fine-uploader.core.js'
//        resource url : 'js/fineUploader/fine-uploader.js'
        resource url : 'js/fineUploader/jquery.fine-uploader.js'
        resource url : 'js/fineUploader/fine-uploader.css'
        resource url : 'js/fineUploader/fine-uploader-gallery.css'
        resource url : 'js/fineUploader/fine-uploader-kuorum.css'
    }

    vimeo {
        resource url:'js/froogaloop2.min.js'
    }

    customFileUploader{
        dependsOn 'application'
        resource url:'js/customFileUploader.js'
        resource url:'css/customUploader.css'
        resource url:'js/jquery.Jcrop.min.js'
        resource url:'css/jquery.Jcrop.min.css'
    }

    social{
        resource url:'js/social/facebook.js'
        resource url:'js/social/twitter.js'
        resource url:'js/social/googlePlus.js'
    }

    loginApi{
        dependsOn 'basic'
        resource url: "${Holders.getGrailsApplication().config.kuorum.rest.url}${Holders.getGrailsApplication().config.kuorum.rest.apiPath}/login/social/socialLogin.js", disposition: "defer"
        resource url: "js/social/loginApi.js", disposition: "defer"
    }

    cookiesHelper{
        dependsOn 'basic'
        resource url:'js/cookies-helper.js'
    }

    kuorumCookies{
        dependsOn 'cookiesHelper'
        resource url:'js/cookies.js'
    }

    comparativeChart{
        dependsOn 'basic'
        resource url:'js/highstock.js'
        resource url:'js/charts-kuorum.js'
        resource url:'js/local_comparationChart.js'
    }

    tour{
        dependsOn 'basic'
        resource url:'js/bootstrap-tour.min.js'
        resource url:'css/bootstrap-tour.min.css'
        resource url:'js/tour_dash_user.js'
    }

//    projectStats{
//        dependsOn 'basic'
//        resource url:'js/chart.min.js'
//        resource url:'js/jquery-jvectormap-1.2.2.min.js'
//        resource url:'js/jquery-jvectormap-es.js'
//        resource url:'js/excanvas.min.js'
//        resource url:'js/modernizr.js'
//        resource url:'js/map.js'
//    }
}