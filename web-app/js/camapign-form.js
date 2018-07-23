$(function(){

    // cerrar modal confirmar envío campaña
    $('body').on('click','#campaignConfirm #saveCampaignBtn', function() {
        $("#politicianMassMailingForm").submit();
        $("#campaignConfirm").modal("hide");
    });

    $('body').on('click', '#campaignWarnFilterEditedButtonOk', function(e){
        var $form = $('form#politicianMassMailingForm');
        $form.submit();
    });


    $("#advanced-features-section a").on("click", function(e){
        e.preventDefault();
        $("#advanced-features").slideToggle(function () {
            $("#advanced-features-section .fa-stack").toggleClass("fa-rotate-180","")
        })
        $("#causes_tag").css("width","100%")
    });

    // CREATE CAMPAIGN moving on STEPS
    var $stepButton = $('ul.campaign-steps > li > a');
    $stepButton.on('click', campaignForm.stepSubmit);

    var $nextButton = $('.form-final-options #next[data-redirectLink]');
    $nextButton.on('click', campaignForm.stepSubmit);

    var $saveDraft = $('.form-final-options #save-draft[data-redirectLink]');
    $saveDraft.on('click', campaignForm.stepSubmit);

    var $saveDraftDebate = $('.form-final-options #save-draft-debate[data-redirectLink]');
    $saveDraftDebate.on('click', campaignForm.stepSubmit);

    var $sendButton = $('#campaignConfirm #saveCampaignBtn[data-redirectLink]');
    $sendButton.on('click', function(e){
        e.preventDefault();
        var callback = $('#saveCampaignBtn').attr('data-callback');
        var $form = $('form.campaign-form');
        var $inputHidden = $form.find('#redirectLink');
        var redirect = $(this).attr('data-redirectLink');
        $inputHidden.attr('value', redirect);
        if (callback != undefined && callback != ""){
            campaignForm[callback]();
        }
        campaignForm.stepSubmit(e);
    });

    // Abrir modal confirmar envío campaña (SURVEY) programada
    $('body').on('click','.form-final-options #send-campaign-later', function(e) {
        e.preventDefault();
        if (campaignForm.validateCampaignForm()) {
            $('#saveCampaignBtn').attr('data-callback', 'scheduleParams');
            //$("input[name='sendType']").val("SCHEDULED");
            campaignForm.prepareAndOpenCampaignConfirmModal();
        }
    });

    // Abrir modal confirmar envio de debate
    $('body').on('click','.form-final-options #send-draft', function(e) {
        e.preventDefault();
        if (campaignForm.validateCampaignForm()) {
            $('#saveCampaignBtn').attr('data-callback', 'sendParams');
            campaignForm.prepareAndOpenCampaignConfirmModal();
        }
    });

})

var campaignForm={
    stepSubmit : function (e){
        e.preventDefault();
        var $form = $('form.campaign-form');
        var $inputHidden = $form.find('#redirectLink');
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
    },
    prepareAndOpenCampaignConfirmModal: function (){
        var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
        $("#campaignConfirmTitle > span").html(amountContacts);
        $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);

        $("#campaignConfirm").modal("show");
    },

    sendParams: function () {
        var date = new Date();
        var dateString = date.getDate()
            + "/" + ("0" + (date.getMonth() + 1)).slice(-2)
            + "/" + date.getFullYear()
            + " " + date.getHours() + ":" + date.getMinutes();
        $("input[name='publishOn']").val(dateString);
        $("input[name='sendType']").val("SEND");
    },

    scheduleParams: function () {
        $("input[name='sendType']").val("SCHEDULED");
    },
    validateCampaignForm:function(){
        // EACH CAMPAIGN SHOULD OVERWRITE THIS VALIDATION METHOD
        return true;
    }
}
