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

        if (!valid) {
            var msg = $("#politicianMassMailingForm").attr("data-generalErrorMessage")
            display.warn(msg)
        }

        return valid;
    }

    // Abrir modal confirmar envio de debate
    $('body').on('click','.form-final-options #send', function(e) {
        e.preventDefault();
        if (isValidDebateForm()) {
            // Autoset publish day for today
            var date = new Date();
            var dateString = date.getDate()
                + "/" + ("0" + (date.getMonth() + 1)).slice(-2)
                + "/" + date.getFullYear()
                + " " + date.getHours() + ":" + date.getMinutes();
            $("input[name='publishOn']").val(dateString);
            prepareAndOpenDebateConfirmModal();
        }
    });
    // Abrir modal confirmar envío debate programada
    $('body').on('click','.form-final-options #send-debate-later', function(e) {
        e.preventDefault();
        if (isValidDebateForm()) {
            prepareAndOpenCampaignConfirmModal();
        }
    });

    // Guardar borrador de debate
    $('body').on('click','.form-final-options #save-draft-debate', function(e) {
        e.preventDefault();
        $("input[name='publishOn']").val("");
        $(this).parents("form").submit();
    });

    function prepareAndOpenDebateConfirmModal() {
        var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
        $("#campaignConfirmTitle > span").html(amountContacts);
        $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);
        if (filterContacts.isFilterEdited()) {
            $("#campaignWarnFilterEdited").modal("show");
        } else {
            $("#campaignConfirm").modal("show");
        }
    }


});