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
        if(isPageLoading()){
            return;
        }
        pageLoadingOn();
        var postId = $button.attr('data-postId');
        var userAlias = $button.attr('data-userAlias');
        var url = $button.attr('data-urlAction');

        var like = $button.find('.fa').hasClass('fa-heart-o');
        var data = {
            postId: postId,
            userAlias: userAlias,
            like: like
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(postRSDTO){
                $button.toggleClass('active');
                $button.find('.fa').toggleClass("fa-heart-o fa-heart");
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

