$(function () {

    var likeButton = $('.post-like');
    likeButton.on("click", postFunctions.bindLikeClick);

});

var postFunctions = {
    bindLikeClick: function(event){
        event.preventDefault();
        var $button = $(event.target);
        postFunctions.onClickPostLike($button);
    },
    onClickPostLike:function($button) {

        $button.off('click');

        var postId = $button.attr('data-postId');
        var userAlias = $button.attr('data-userAlias');
        var url = $button.attr('data-urlAction');

        var like = $button.find('.fa').hasClass('fa-heart-o');

        $button.toggleClass('active');
        $button.find('.fa').toggleClass("fa-heart-o fa-heart");
        var count = parseInt($button.find('.number').text());
        if (like) {
            $button.find('.number').text(count + 1);
        } else {
            $button.find('.number').text(count - 1);
        }

        var data = {
            postId: postId,
            userAlias: userAlias,
            like: like
        };

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(){
                //
            },
            error: function(){
                // Restore counter
                $button.toggleClass('active');
                $button.find('.fa').toggleClass('fa-heart-o fa-heart');
                var count = parseInt($button.find('.number').text());
                if(like){
                    $button.find('.number').text(count - 1);
                } else {
                    $button.find('.number').text(count + 1);
                }
            },
            complete: function(){
                $button.on('click', postFunctions.bindLikeClick);
            }
        });
    }
}

