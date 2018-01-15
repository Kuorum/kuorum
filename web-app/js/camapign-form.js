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
    })

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


function prepareAndOpenCampaignConfirmModal(){
    var amountContacts = $('select#recipients option:selected').attr("data-amountContacts");
    $("#campaignConfirmTitle > span").html(amountContacts);
    $("#campaignWarnFilterEdited .modal-body > p > span").html(amountContacts);

    $("#campaignConfirm").modal("show");
}