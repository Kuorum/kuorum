$(function(){

    $(".event-confirm-button").on("click",function(e) {
        e.preventDefault();
        var $button = $(this)
        var userAliasLogged = $button.attr("data-userLoggedAlias")
        console.log(userAliasLogged )
        if (userAliasLogged == undefined || userAliasLogged==""){
            var buttonId = guid();
            $button.attr("id", buttonId)
            $('#registro').find("form").attr("callback", "eventConfirmNoLogged")
            $('#registro').find("form").attr("data-buttonId", buttonId)
            $('#registro').modal('show');
        }else{
            eventFunctions.confirmAssistance($button)
        }

    });

    // INIT CALLBACKS
    noLoggedCallbacks['eventConfirmNoLogged'] = function(){
        pageLoadingOn();
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $buttonConfirm = $("#"+buttonId);
        $buttonConfirm.attr("data-userLoggedAlias", "logged"); //Chapu para que el if no saque de nuevo la modal
        pageLoadingOff();
        eventFunctions.confirmAssistance($buttonConfirm, function(jsonSuccess){
            document.location.reload()
        })
    };
});

var eventFunctions={
    confirmAssistance:function($button, callback){
        var debateId = $button.attr("data-debateId")
        var urlConfirm = $button.attr("data-postUrl")
        var data ={debateId:debateId}
        $.ajax({
            type: "POST",
            url: urlConfirm,
            data: data,
            success: function(jsonData){
                var $parentBox = $button.parents(".comment-box.call-to-action")
                $parentBox.addClass("event-confirmed")
                $parentBox.find(".unconfirmed").fadeOut("slow",function () {
                    $parentBox.find(".confirmed").removeClass("hide");
                })
                if (callback != undefined){
                    callback()
                }
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