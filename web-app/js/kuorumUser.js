

$(function(){
    $('body').on("click", ".btn.contact", kuorumUserFunction.bindClickContact);

    // noLoggedCallbacks['contactButtonNologged']= function(){
    //     var buttonId = $('#registro').find("form").attr("data-buttonId");
    //     var $button = $("#"+buttonId);
    //     pageLoadingOff();
    //     petitionFunctions.onClickSignPetition($button, noLoggedCallbacks.reloadPage);
    // };
    $(".profile-pic").on("click", function(e){
        $(this).siblings(".modal-user-picture").modal("show");
    })
    $(".modal-user-picture").on("click", function (e) {
        $(".modal-user-picture").modal("hide");
    });
});

var kuorumUserFunction = {

    bindClickContact : function (event) {
        event.preventDefault();
        event.stopPropagation();
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == ""){
            // NO LOGGED
            // var buttonId = guid();
            // $button.attr("id", buttonId);
            // $('#registro').find("form").attr("callback", "contactButtonNologged");
            // $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }else{
            kuorumUserFunction.openContactModal($button)
        }
    },

    openContactModal : function($button){
        $('#contact-modal').modal('show');
    }

};