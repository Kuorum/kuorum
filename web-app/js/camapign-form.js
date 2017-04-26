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

})

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

function editedFilterModal() {
    var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
    $("#campaignConfirmTitle > span").html(amountContacts);
    $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);

    $("#campaignWarnFilterEdited").modal("show");
}

function prepareAndOpenCampaignConfirmModal(){
    var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
    $("#campaignConfirmTitle > span").html(amountContacts);
    $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);

    $("#campaignConfirm").modal("show");
}