
var editor = new MediumEditor('.editable', {
    buttonLabels: 'fontawesome',
    toolbar: {
        buttons: ['anchor']
    }
});

var editorComment = new MediumEditor('.editable-comment', {
    buttonLabels: 'fontawesome',
    toolbar: {
        buttons: ['anchor']
    }
});

editorComment.subscribe('focus', function (event, editable) {
    $('[data-toggle="tooltip-vote"]').tooltip('hide');
});

$(function(){
    // LightbulButton - comment-counter
    var lightbulbButton = $('.leader-post > .footer .comment-counter button');
    lightbulbButton.on('click', function (event) {
        var $commentBox = $(event.target).closest('.leader-post').next('.comment-box');
        var $comment = $commentBox.find('.comment');
        var navbarHeight = $('.navbar').outerHeight();

        $('html, body').animate({
            scrollTop: $commentBox.offset().top - navbarHeight - 40
        }, 1000, function () {
            $comment.focus();
        });
    });

    // CommentButton
    var $commentButton = $('.conversation-box .footer .comment-counter button.comment');

    $commentButton.on('click', function (event) {
        var $conversationBox = $(event.target.parentElement).closest('.conversation-box');
        conversationSectionClick($conversationBox)

    });

    var $commentSaveButton = $('.conversation-box-comments .comment-box .actions button.save-comment')

    $commentSaveButton.on('click', function (event) {
        var $conversationBox = $($(this).parents('.conversation-box-comments')[0]).prev();
        conversationSectionClick($conversationBox)
    });

    function conversationSectionClick($conversationBox){
        var $conversationBoxComments = $conversationBox.next('.conversation-box-comments');
        var $conversationBoxCommentsComment = $conversationBoxComments.find('.comment-box .editable-comment');
        var $conversationBoxCommentsArrow = $conversationBoxComments.find('.go-up');
        var navbarHeight = $('.navbar').height();

        var isVisible = $conversationBoxCommentsComment.is(':visible');

        $conversationBoxCommentsArrow.trigger('click', [isVisible, $conversationBoxCommentsComment]);
    }

    // See More button
    var $conversationBoxButtonSeeMoreSmall = $('.conversation-box button.btn-see-more.stack');

    $conversationBoxButtonSeeMoreSmall.on('click', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $conversationBox = $actionsContent.closest('.conversation-box');
        var $conversationBoxBody = $actionsContent.closest('.conversation-box').find('.body');

        $(event.target).toggleClass('fa-angle-down fa-angle-up');

        if ($conversationBoxBody.height() > 134) {
            $conversationBoxBody.animate({
                height: 134
            }, 1000);
        } else {
            var targetElementHeight = $conversationBoxBody.addClass('height-auto').height();
            $conversationBoxBody.removeClass('height-auto').animate({
                height: targetElementHeight
            }, 1000);
        }
    });

    function prepareCollapsableBodys(){
        $.each($(".conversation-box .body"), function(idx,obj){
            if ($(this).height() > 134){
                $(this).addClass("collapsible")
            }
        });
    }
    prepareCollapsableBodys();

    // See More comments
    var $seeMoreCommentsSmall = $('.conversation-box-comments .go-up');

    $seeMoreCommentsSmall.on('click', function (event, isVisible, $target) {
        var $placeholder = $(event.target).closest('.comment-box');
        var $conversationComments = $placeholder.prev();
        var $actionsContent = $(event.target).closest('.actions');
        var $commentPropusal = $placeholder.find('.comment-propusal');
        var navbarHeight = $('.navbar').outerHeight();
        var $arrowButton = $(event.target).closest('.go-up').find('.angle');

        if (!isVisible) {
            $commentPropusal.slideToggle();
            $conversationComments.slideToggle(1000, function () {
                $arrowButton.toggleClass('fa-angle-down fa-angle-up');
                if (isVisible === false) {
                    $('html, body').animate({
                        scrollTop: $target.offset().top - navbarHeight - 40
                    }, 1000, function () {
                        $target.focus();
                    });
                }
            });
        } else if (isVisible) {
            $('html, body').animate({
                scrollTop: $target.offset().top - navbarHeight - 40
            }, 1000, function () {
                $target.focus();
            });
        }
    });

    $(".publish-proposal").on("click", function(e){
        var alias = $(this).attr("data-userLoggedAlias")
        if (alias == ""){
            // USER NO LOGGED
            $('#registro').modal('show');
        }else{
            var body = $(".comment.editable.medium-editor-element p").html();
            var debateId = $(this).attr("data-debateId");
            var debateAlias = $(this).attr("data-debateAlias");
            var url = $(this).attr("data-postUrl");
            var data={
                debateId:debateId,
                debateAlias:debateAlias,
                body:body
            };
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function(htmlProposal){
                    console.log(htmlProposal)
                },
                dataType: "html"
            });
        }
    });

    $(".pin-propusal").on("click", function(){
        $(this).toggleClass("active","")
        var pin = $(this).hasClass("active");
        var url =$(this).attr("data-urlAction");
        var debateId = $(this).attr("data-debateId");
        var debateAlias = $(this).attr("data-debateAlias");
        var proposalId = $(this).attr("data-proposalId");
        var data={
            debateId:debateId,
            debateAlias:debateAlias,
            proposalId:proposalId,
            pin:pin
        };
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function(jsonData){
                console.log(jsonData)
            },
            dataType: "json"
        });


    })
});