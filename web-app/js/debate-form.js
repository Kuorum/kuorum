$(function(){

    function isValidDebateForm() {
        var valid = $("#politicianMassMailingForm").valid();

        if ($("textarea[name=body]").val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.DebateCommand.body.nullable;
            $("div.textareaContainer").append('<span id="campatingTextErrorSpan" for="text" class="error"><span class="tooltip-arrow"></span>'+errorMsg+'</span>');
            valid = false;
        } else {
            $("#campatingTextErrorSpan").fadeOut()
        }

        if ($("input[name=title]").val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.DebateCommand.title.nullable;
            $("input[name=title]").closest("div").append('<span id="campatingTextErrorSpan" for="text" class="error"><span class="tooltip-arrow"></span>'+errorMsg+'</span>');
            valid = false;
        } else {
            $("#campatingTextErrorSpan").fadeOut()
        }

        if (!valid) {
            var msg = $("#politicianMassMailingForm").attr("data-generalErrorMessage")
            display.warn(msg)
        }

        return valid;
    }

    // Abrir modal confirmar envio de debate
    $('body').on('click','.form-final-options #send-draft', function(e) {
        e.preventDefault();
        if (isValidDebateForm()) {
            $('#saveCampaignBtn').attr('data-callback', 'sendParams');
            prepareAndOpenDebateConfirmModal();
        }
    });
    // Abrir modal confirmar envío debate programada
    $('body').on('click','.form-final-options #send-debate-later', function(e) {
        e.preventDefault();
        if (isValidDebateForm()) {
            $('#saveCampaignBtn').attr('data-callback', 'scheduleParams');
            //$("input[name='sendType']").val("SCHEDULED");
            prepareAndOpenCampaignConfirmModal();
        }
    });

    // Guardar borrador de debate
    $('body').on('click','.form-final-options #save-draft-debate', function(e) {
        e.preventDefault();
        $("input[name='publishOn']").val("");
        $("input[name='sendType']").val("DRAFT");
        $(this).parents("form").submit();
    });

    /*function prepareAndOpenDebateConfirmModal() {
        var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
        $("#campaignConfirmTitle > span").html(amountContacts);
        $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);
        if (filterContacts.isFilterEdited()) {
            $("#campaignWarnFilterEdited").modal("show");
        } else {
            $("#campaignConfirm").modal("show");
        }
    }*/


    function prepareAndOpenDebateConfirmModal(){
        var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
        $("#campaignConfirmTitle > span").html(amountContacts);
        $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);

        $("#campaignConfirm").modal("show");
    }

    // CREATE CAMPAIGN moving on STEPS
    var $stepButton = $('ul.campaigns.twoSteps > li > a');
    $stepButton.on('click', stepSubmit);

    var $nextButton = $('.form-final-options #next[data-redirectLink]');
    $nextButton.on('click', stepSubmit);

    var $saveDraft = $('.form-final-options #save-draft[data-redirectLink]');
    $saveDraft.on('click', stepSubmit);

    var $saveDraftDebate = $('.form-final-options #save-draft-debate[data-redirectLink]');
    $saveDraftDebate.on('click', stepSubmit);

    var $sendButton = $('#campaignConfirm #saveCampaignBtn[data-redirectLink]');
    $sendButton.on('click', function(e){
        e.preventDefault();
        var callback = $('#saveCampaignBtn').attr('data-callback');
        var $form = $('form#politicianMassMailingForm');
        var $inputHidden = $form.find('#redirectLink');
        var redirect = $(this).attr('data-redirectLink');
        $inputHidden.attr('value', redirect);
        if (callback != undefined && callback != ""){
            window[callback]();
        }
        stepSubmit(e);
    });

    // Animate view when click on add image or video
    $("a[data-toggle='tab']").on('shown.bs.tab', function () {
        var $navbarTabs = $("ul.nav-pills");
        var navbarHeight = $('.navbar').outerHeight();
        $('html, body').animate({
            scrollTop: $navbarTabs.offset().top - navbarHeight - 40
        }, 1000, function () {
            // ÑAPA: solo hace falta hacer focus para el input de video de YT
            $("#videoPost").focus();
        });
    });

    $("#advanced-features-section a").on("click", function(e){
        e.preventDefault();
        $("#advanced-features").slideToggle(function () {
            $("#advanced-features-section .fa-stack").toggleClass("fa-rotate-180","")
        })


        $("#causes_tag").css("width","100%")

    })
});

function sendParams() {
    var date = new Date();
    var dateString = date.getDate()
        + "/" + ("0" + (date.getMonth() + 1)).slice(-2)
        + "/" + date.getFullYear()
        + " " + date.getHours() + ":" + date.getMinutes();
    $("input[name='publishOn']").val(dateString);
    $("input[name='sendType']").val("SEND");
}

function scheduleParams() {
    $("input[name='sendType']").val("SCHEDULED");
}

function stepSubmit (e){
    e.preventDefault();
    var $form = $('form#politicianMassMailingForm');
    var $inputHidden = $form.find('#redirectLink');
    console.log($inputHidden.val());
    if($inputHidden.val() == undefined || $inputHidden.val() == ""){
        var redirect = $(this).attr('data-redirectLink');
        $inputHidden.attr('value', redirect);
    }
    var $filter = $('select#recipients option:selected').length;
    if($filter && filterContacts.isFilterEdited()){
        var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
        $("#campaignConfirmTitle > span").html(amountContacts);
        $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);
        $("#campaignWarnFilterEdited").modal("show");
    }else{
        $form.submit();
    }
}