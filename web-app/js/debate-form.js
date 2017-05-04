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
        console.log("CLICK")
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

    var $sendButton = $('#campaignConfirm #saveCampaignBtn[data-redirectLink]');
    $sendButton.on('click', stepSubmit);
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