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

    $("#newsletterCommunication, #groupValidation").on("change", toggleFilterContactSelectorVisibility)

    function toggleFilterContactSelectorVisibility(){
        var activeFilter = $("#newsletterCommunication:checked, #groupValidation:checked").length >0
        if (activeFilter){
            $("#filter-contact-selector").slideDown();
        }else{
            $("#filter-contact-selector").slideUp();
        }
    }
    toggleFilterContactSelectorVisibility();

    // CREATE CAMPAIGN moving on STEPS
    var $stepButton = $('.campaign-steps ul.actionIcons > li > a');
    $stepButton.on('click', campaignForm.stepSubmit);

    var $nextButton = $('.form-final-options #next[data-redirectLink]');
    $nextButton.on('click', campaignForm.stepSubmit);

    var $saveDraft = $('.form-final-options #save-draft[data-redirectLink]');
    $saveDraft.on('click', campaignForm.stepSubmit);

    var $updateCampaignReactivate = $('.form-final-options #update-campaign-reactivate[data-redirectLink]');
    $updateCampaignReactivate.on('click', function(e){
        e.preventDefault();
        var $inputSendType = $('form.campaign-form').find("input[name=sendType]");
        $inputSendType.val("ACTIVATE")
        campaignForm.updateCampaign(e);
    });

    var $saveDraftDebate = $('.form-final-options #save-draft-debate[data-redirectLink]');
    $saveDraftDebate.on('click', campaignForm.stepSubmit);

    var $updateCampaign = $('.form-final-options #update-campaign[data-redirectLink]');
    $updateCampaign.on('click', campaignForm.updateCampaign);


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

    $("#districtProposalChooseDistrict select[name='districtId']").on("change", function(e){
        var $select = $("#districtProposalChooseDistrict select[name='districtId']")
        var orgValue = $select.attr("data-original-value");
        if (orgValue != "" && $select.val()!=orgValue){
            $("#changeDistrictWarn").modal("show")
        }
    });

    $("#changeDistrictWarnRevertStatus").on("click", function(e){
        var $select = $("#districtProposalChooseDistrict select[name='districtId']")
        var orgValue = $select.attr("data-original-value");
        $select.val(orgValue)
    })
})

var campaignForm={
    stepSubmit : function (e){
        e.preventDefault();
        campaignForm._prepareRedirectLink(e);
        var $form = $('form.campaign-form');
        var $filter = $('select#recipients option:selected').length;
        if($filter && filterContacts.isFilterEdited()){
            var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
            $("#campaignConfirmTitle > span").html(amountContacts);
            $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);
            $("#campaignWarnFilterEdited").modal("show");
        }else{
            console.log("Submit")
            $form.submit();
        }
    },

    updateCampaign: function(e){
        e.preventDefault();
        var $form = $('form.campaign-form');
        campaignForm._prepareRedirectLink(e);
        if (campaignForm.validateCampaignForm()) {
            $form.submit();
        }
    },
    _prepareRedirectLink: function(e){
        var $linkElement = $(e.target);
        if (!($linkElement.is("a") || $linkElement.is("button"))){
            $linkElement = $linkElement.closest("a")
        }
        var $form = $('form.campaign-form');
        var $inputHidden = $form.find('#redirectLink');
        if($inputHidden.val() == undefined || $inputHidden.val() == ""){
            var redirect = $linkElement.attr('data-redirectLink');
            $inputHidden.attr('value', redirect);
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
        console.log("Validation campaign is not overwritten")
        return true;
    }
}
