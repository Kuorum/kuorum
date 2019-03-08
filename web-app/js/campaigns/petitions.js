
$(function () {

    var likeButton = $('.petition-sign');
    likeButton.on("click", petitionFunctions.bindSignClick);
    noLoggedCallbacks['signPetitionNoLogged']= function(){
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#"+buttonId);
        pageLoadingOff();
        petitionFunctions.onClickSignPetition($button, noLoggedCallbacks.reloadPage);
    };
});


var petitionFunctions = {
    bindSignClick: function(event){
        event.preventDefault();
        event.stopPropagation();
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == ""){
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "signPetitionNoLogged");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }else{
            petitionFunctions.onClickSignPetition($button);
        }
    },
    onClickSignPetition:function($button, callback) {
        if(isPageLoading()){
            return;
        }
        pageLoadingOn();
        var params = {
            callback: callback,
            $button : $button
        };
        var loggedUser = $button.attr('data-loggedUser');
        var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(petitionFunctions.__executableAsyncPetitionSign, params);
        var validationActive = $button.attr('data-campaignValidationActive');
        if (validationActive=="true"){
            userValidatedByDomain.checkUserValid(loggedUser, executableFunction)
        }else{
            executableFunction.exec()
        }
    },
    __executableAsyncPetitionSign:function (params) {
        var callback = params.callback;
        var $button = params.$button;
        var url = $button.attr('href');

        var sign = $button.find('.fa-microphone').hasClass('fal');
        var petitionUserId = $button.attr('data-petitionUserId');
        var data = {
            sign: sign,
            petitionUserId:petitionUserId
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(petitionSign){
                var petitionRSDTO = petitionSign.petition;
                var $buttons = $(".petition-sign-"+petitionRSDTO.id);
                $buttons.toggleClass('active');
                $buttons.toggleClass('on');
                $buttons.find('.fa-microphone').toggleClass("fas fal");
                $buttons.find('.number').text(petitionRSDTO.signs);
                $button.blur();
                $("#module-petition-user-signs").html(petitionSign.signsHtml);
                if (callback != undefined){
                    callback();
                }
            },
            error: function(){

            },
            complete: function(){
                pageLoadingOff();
            }
        });
    }
};