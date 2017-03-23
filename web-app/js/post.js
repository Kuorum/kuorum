$(function () {


    function onClickPostLike($button) {

        $button.off('click', '.post-like');

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
                $button.on('click', '.post-like', binding);
            }
        });
    }

    var likeButton = $('.post-like');
    likeButton.click(binding);

    function binding (e){
        e.preventDefault();
        var $button = $(this);
        onClickPostLike($button);
    }

});

