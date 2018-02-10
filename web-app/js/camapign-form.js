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
    $stepButton.on('click', stepSubmit);

    var $nextButton = $('.form-final-options #next[data-redirectLink]');
    $nextButton.on('click', stepSubmit);

    var $saveDraft = $('.form-final-options #save-draft[data-redirectLink]');
    $saveDraft.on('click', stepSubmit);

    var $saveDraftDebate = $('.form-final-options #save-draft-debate[data-redirectLink]');
    $saveDraftDebate.on('click', stepSubmit);
})


function stepSubmit (e){
    e.preventDefault();
    var $form = $('form.campaign-form');
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

/*
function prepareAndOpenCampaignConfirmModal() {
    var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
    $("#campaignConfirmTitle > span").html(amountContacts);
    $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);
    if (filterContacts.isFilterEdited()){
        $("#campaignWarnFilterEdited").modal("show");
    }else{
        $("#campaignConfirm").modal("show");
    }
}*/


function prepareAndOpenCampaignConfirmModal(){
    var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
    $("#campaignConfirmTitle > span").html(amountContacts);
    $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);

    $("#campaignConfirm").modal("show");
}