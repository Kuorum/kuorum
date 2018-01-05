$(function(){

    $(".event-confirm-button").on("click",function(e) {
        e.preventDefault();
        var $button = $(this)
        eventFunctions.clickConfirmAssistance($button);

    });

    // INIT CALLBACKS
    noLoggedCallbacks['eventConfirmNoLogged'] = function(){
        pageLoadingOn();
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $buttonConfirm = $("#"+buttonId);
        $buttonConfirm.attr("data-userLoggedAlias", "logged"); //Chapu para que el if no saque de nuevo la modal
        eventFunctions.confirmAssistance($buttonConfirm, noLoggedCallbacks.reloadPage)
    };

    $(".actions.call-to-action-mobile.call-mobile-event-confirm ").on("click",function(e){
        e.preventDefault();
        e.stopPropagation();
        var $button = $(this)
        eventFunctions.clickConfirmAssistance($button);
    })
});

var eventFunctions={
    clickConfirmAssistance:function($button){
        var userAliasLogged = $button.attr("data-userLoggedAlias")
        if (userAliasLogged == undefined || userAliasLogged==""){
            var buttonId = guid();
            $button.attr("id", buttonId)
            $('#registro').find("form").attr("callback", "eventConfirmNoLogged")
            $('#registro').find("form").attr("data-buttonId", buttonId)
            $('#registro').modal('show');
        }else{
            eventFunctions.confirmAssistance($button)
        }
    },
    confirmAssistance:function($button, callback){
        pageLoadingOn();
        var eventId = $button.attr("data-eventId")
        var urlConfirm = $button.attr("data-postUrl")
        var data ={eventId:eventId}
        $.ajax({
            type: "POST",
            url: urlConfirm,
            data: data,
            success: function(jsonData){
                var $parentBox = $button.parents(".comment-box.call-to-action")
                $parentBox.addClass("box-event-confirmed")
                $(".event-unconfirmed").fadeOut("slow",function () {
                    $(".event-confirmed").removeClass("hide");
                    $(".event-unconfirmed").remove();
                    $(".comment-box.proposal-comment-box.hide").removeClass("hide");
                    $(".comment-counter .event-confirm-button").addClass("active")
                    $(".comment-counter .event-confirm-button").addClass("disabled")
                })
                if (callback != undefined){
                    callback()
                }
                pageLoadingOff();
            },
            error:function(){
                display.error("Sorry: Error registering on the event")
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    }
}