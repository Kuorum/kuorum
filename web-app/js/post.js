$(function () {

    var likeButton = $('.post-like');
    likeButton.on("click", postFunctions.bindLikeClick);
    noLoggedCallbacks['likePostNoLogged']= function(){
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#"+buttonId);
        pageLoadingOff();
        postFunctions.onClickPostLike($button, noLoggedCallbacks.reloadPage);
    };
});

var postFunctions = {
    bindLikeClick: function(event){
        event.preventDefault();
        event.stopPropagation();
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == ""){
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "likePostNoLogged");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }else{
            postFunctions.onClickPostLike($button);
        }
    },
    onClickPostLike:function($button, callback) {
        //$button.off('click');
        console.log("onClickPostLike")
        if(isPageLoading()){
            return;
        }
        pageLoadingOn();
        var params = {
            callback: callback,
            $button : $button
        }
        var loggedUser = $button.attr('data-loggedUser');
        var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(postFunctions.__executableAsyncPostLike, params)
        var validationActive = $button.attr('data-campaignValidationActive');
        if (validationActive=="true"){
            userValidatedByDomain.checkUserValid(loggedUser, executableFunction)
        }else{
            executableFunction.exec()
        }
    },
    __executableAsyncPostLike:function (params) {
        var callback = params.callback;
        var $button = params.$button;

        var postId = $button.attr('data-postId');
        var postUserId = $button.attr('data-postUserId');
        var url = $button.attr('data-urlAction');

        var like = $button.find('.fa-heart').hasClass('fal');
        var data = {
            postId: postId,
            postUserId: postUserId,
            like: like
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(postRSDTO){
                $button.toggleClass('active');
                $button.find('.fa-heart').toggleClass("fas fal");
                $button.find('.number').text(postRSDTO.likes);
                if (callback != undefined){
                    callback();
                }
            },
            error: function(){

            },
            complete: function(){
                //$button.on('click', postFunctions.bindLikeClick);
                pageLoadingOff();
            }
        });
    }
}

