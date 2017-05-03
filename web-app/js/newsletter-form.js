$(function () {

    // Guardar borrador
    $('body').on('click','.form-final-options #save-draft-campaign', function(e) {
        e.preventDefault();
        $("#sendMassMailingType").val("DRAFT");
        $(this).parents("form").submit();
    });

    // abrir modal confirmar envío campaña
    $('body').on('click','.form-final-options #send', function(e) {
        e.preventDefault();
        if (isValidCampaignForm()) {
            $sendButton.attr("data-massMailingType", "SEND")
            prepareAndOpenCampaignConfirmModal();
        }
    });

    $('body').on('click','#sendTestModalButonOk', function(e) {
        e.preventDefault();
        $("#sendTestModal").modal("hide")
    });
    $('body').on('click','#sendTest', function(e) {
        e.preventDefault();
        if (isValidCampaignForm()) {
            pageLoadingOn();
            var link = $(this).attr("href");
            $("#sendMassMailingType").val("SEND_TEST");
            var postData= $("#politicianMassMailingForm").serialize();
            $.post( link, postData)
                .done(function(data) {
                    $("#sendTestModal").modal("show")
                    if ($("#politicianMassMailingForm input[name=campaignId]").length <=0){
                        $("#politicianMassMailingForm").append("<input name='campaignId' type='hidden'/>")
                    }
                    $("#politicianMassMailingForm input[name=campaignId]").val(data.campaing.id)
                })
                .fail(function(messageError) {
                    display.warn("Error");
                })
                .always(function() {
                    pageLoadingOff();
                });
        }
    });

    function dateFromString(str) {
        // FORMAT dd/mm/yyyy hh24:mi
        var m = str.match(/(\d+)\/(\d+)\/(\d+)\s+(\d+):(\d+)/);
        return new Date(+m[3], +m[2] - 1, +m[1], +m[4], +m[5], 0);
    }

    function correctCampaingScheduled(){
        var strTimestampScheduled = $("#scheduled").val();
        var timestampScheduled = dateFromString(strTimestampScheduled); // Sat Oct 10 2012 03:47:00 GMT-0500 (EST)
        console.log(timestampScheduled)
        if (timestampScheduled < new Date()){
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.MassMailingCommand.scheduled.min.error;
            $("#scheduled").parents("div.input-group.datetime").after("<span for='scheduled' class='error'><span class='tooltip-arrow'></span>"+errorMsg+"</span>")
            $("#scheduled").addClass("error")
            return false;
        }
        return true;
    }

    function isValidCampaignForm(){
        var valid = $("#politicianMassMailingForm").valid();

        var headerPicture = $("input[name=headerPictureId]");
        if ((typeof headerPicture.attr('required') == "undefined" || headerPicture.attr('required') != "false")
            && headerPicture.val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.MassMailingCommand.headerPictureId.nullable;
            $(".uploaderImageContainer").append('<span id="headerPictureErrorSpan" class="error" style="margin-top:1px;"><span class="tooltip-arrow"></span>'+errorMsg+'</span>');
            $(".uploaderImageContainer").parents(".form-group.header-campaign").css("height","auto");
            valid = false;
        } else {
            $("#headerPictureErrorSpan").fadeOut()
        }

        if ($("textarea[name=text]").val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.MassMailingCommand.text.nullable;
            $("div.textareaContainer").append('<span id="campatingTextErrorSpan" for="text" class="error"><span class="tooltip-arrow"></span>'+errorMsg+'</span>');
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

    // Abrir modal confirmar envío campaña programada
    $('body').on('click','.form-final-options #sendLater', function(e) {
        e.preventDefault();
        if (isValidCampaignForm() && correctCampaingScheduled()){
            $sendButton.attr("data-massMailingType", "SCHEDULED")
            prepareAndOpenCampaignConfirmModal();
        }
    });


    // CREATE CAMPAIGN moving on STEPS
    var $stepButton = $('ul.campaigns.threeSteps > li > a');
    $stepButton.on('click', stepSubmit);

    var $nextButton = $('.form-final-options #next[data-redirectLink]');
    $nextButton.on('click', stepSubmit);

    var $sendButton = $('#campaignConfirm #saveCampaignBtn[data-redirectLink]');
    $sendButton.on('click', function(e){
        var mailingType = $sendButton.attr("data-massMailingType")
        $("#sendMassMailingType").val(mailingType);
        stepSubmit(e)
    });

});

function stepSubmit (e){
    e.preventDefault();
    var $form = $('form#politicianMassMailingForm');
    var $inputHidden = $form.find('#redirectLink');
    var redirect = $(this).attr('data-redirectLink');
    $inputHidden.attr('value', redirect);
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