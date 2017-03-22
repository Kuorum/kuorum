$(function () {

    $(window).load(function () {

        function onClickPostLike(e) {

            e.preventDefault();

            var $button = $(this);

            $button.off("click", ".post-like");

            var postId = $button.attr('data-postId');
            var userAlias = $button.attr('data-userAlias');

            var like = $button.attr('class', "fa-heart-o");

            $button.toggleClass("active");
            $button.attr('class', "fa").toggleClass("fa-heart-o fa-heart");
            var count = parseInt($button.attr('class', "number").text());
            console.log("El número es" + count);
            if (like) {
                $button.find(".number").text(count + 1)
                console.log('LIKE!');
            } else {
                $button.find(".number").text(count - 1)
            }
        }

        var likeButton = $('.leader-post > .footer button');
        likeButton.click(function (e) {
            onClickPostLike(e);
        });

    });
});

