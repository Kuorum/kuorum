$(function(){

    function isValidDebateForm() {
        var valid = $("#politicianMassMailingForm").valid();

        if ($("textarea[name=body]").val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.DebateCommand.body.nullable;
            $("div.textareaContainer").append('<span id="campatingTextErrorSpan" class="error"><span class="tooltip-arrow"></span>' + errorMsg + '</span>');
            valid = false;
        } else {
            $("#campatingTextErrorSpan").fadeOut()
        }

        if ($("input[name=title]").val() == "") {
            var errorMsg = i18n.kuorum.web.commands.payment.massMailing.DebateCommand.title.nullable;
            $("input[name=title]").closest("div").append('<span id="campatingTextErrorSpan" class="error"><span class="tooltip-arrow"></span>' + errorMsg + '</span>');
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


    // OVERWRITE CUSTOM FORM VALIDATION
    campaignForm.validateCampaignForm = isValidDebateForm
    // Abrir modal confirmar envio de debate

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
});