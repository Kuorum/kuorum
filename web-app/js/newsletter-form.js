$(function () {


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

        if ($("input[name=subject]").val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.MassMailingCommand.subject.nullable;
            $("input[name=subject]").parent().append('<span id="campatingTextErrorSpan" for="text" class="error"><span class="tooltip-arrow"></span>'+errorMsg+'</span>');
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
});