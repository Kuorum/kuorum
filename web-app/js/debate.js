
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
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="tooltip-vote"]').tooltip({
        html: true,
        trigger: 'manual',
        title : function () {
            var voteUser = $(this).parent().closest('.comment-box').find('.vote-user').clone();
            return voteUser.addClass('show')[0].outerHTML;
        }
    });

    // Mouse events
    $('[data-toggle="tooltip-vote"]').mouseover(function () {
        if (!$(this).next('.tooltip').is(':visible')) {
            $(this).tooltip('show');
        }
    });

    $('[data-toggle="tooltip-vote"]').on('shown.bs.tooltip', function () {
        var $self = $(this);
        var $commentBox = $(this).closest('.comment-box');
        var $tooltip = $commentBox.find('.tooltip');

        $commentBox.mouseleave(function () {
            $self.tooltip('hide');
        });

        $tooltip.mouseleave(function () {
            $self.tooltip('hide');
        });
    });

    // LightbulButton
    var lightbulbButton = $('.leader-post > .footer .comment-counter button');
    lightbulbButton.on('click', function () {
        var $commentBox = $($('.comment-box .comment')[0]);
        var navbarHeight = $('.navbar').outerHeight();

        $('html, body').animate({
            scrollTop: $commentBox.offset().top - navbarHeight - 40
        }, 1000, function () {
            $commentBox.focus();
        });
    });

    // CommentButton
    var commentButton = $('.conversation-box .footer .comment-counter button.comment');
    commentButton.on('click', function (event) {
        var $commentBox = $(event.target.parentElement).closest('.conversation-box').next('.conversation-box-comments').find('.comment-box .editable-comment');
        var navbarHeight = $('.navbar').height();

        $('html, body').animate({
            scrollTop: $commentBox.offset().top - navbarHeight - 40
        }, 1000, function () {
            $commentBox.focus();
        });
    });

    // See More button
    var $conversationBoxButtonSeeMore = $('.conversation-box .btn.btn-see-more');
    var $conversationBoxButtonSeeMoreSmall = $('.conversation-box button.btn-see-more.stack');

    $conversationBoxButtonSeeMoreSmall.on('mouseover', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $conversationBoxButtonSeeMore = $actionsContent.find('.btn.btn-see-more');
        var $conversationBoxButtonSeeMoreSmall = $actionsContent.find('button.btn-see-more.stack');

        $conversationBoxButtonSeeMoreSmall.hide();
        $conversationBoxButtonSeeMore.show();
    });

    $conversationBoxButtonSeeMore.on('mouseleave', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $conversationBoxButtonSeeMore = $actionsContent.find('.btn.btn-see-more');
        var $conversationBoxButtonSeeMoreSmall = $actionsContent.find('button.btn-see-more.stack');

        $conversationBoxButtonSeeMore.hide();
        $conversationBoxButtonSeeMoreSmall.show();
    });

    $conversationBoxButtonSeeMore.on('click', function (event) {
        var $targetElement = $(event.target)
        var conversationBoxButtonSeeMoreSmall = $('.conversation-box button.btn-see-more.stack .angle')[0];

        $targetElement.toggleClass('btn-see-less angle-down angle-up');
        $(conversationBoxButtonSeeMoreSmall).toggleClass('fa-angle-down fa-angle-up');

        if ($targetElement.parent().prev('.body').height() > 134) {
            $targetElement.text('See More');
            $targetElement.parent().prev('.body').animate({
                height: 134
            }, 1000);
        } else {
            $targetElement.text('Collapse');
            var targetElementHeight = $targetElement.parent().prev('.body').addClass('height-auto').height();
            $targetElement.parent().prev('.body').removeClass('height-auto').animate({
                height: targetElementHeight
            }, 1000);
        }
    });

    // See More comments
    var $seeMoreComments = $('.conversation-box-comments .btn.btn-see-more');
    var $seeMoreCommentsSmall = $('.conversation-box-comments .go-up');

    $seeMoreCommentsSmall.on('mouseover', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $seeMoreComments = $actionsContent.find('.btn.btn-see-more');
        var $seeMoreCommentsSmall = $actionsContent.find('button.go-up.stack');

        $seeMoreCommentsSmall.hide();
        $seeMoreComments.show();
    });

    $seeMoreComments.on('mouseleave', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $seeMoreComments = $actionsContent.find('.btn.btn-see-more');
        var $seeMoreCommentsSmall = $actionsContent.find('button.go-up.stack');

        $seeMoreComments.hide();
        $seeMoreCommentsSmall.show();
    });

    $seeMoreComments.on('click', function (event) {
        var $placeholder = $(event.target).closest('.comment-box');
        var $conversationComments = $placeholder.prev();
        var $actionsContent = $(event.target).closest('.actions');
        var $seeMoreComments = $actionsContent.find('.btn.btn-see-more');

        $conversationComments.slideToggle(1000);
        $seeMoreComments.toggleClass('btn-see-less angle-down angle-up');
        $seeMoreCommentsSmall.find('.angle').toggleClass('fa-angle-down fa-angle-up');

        if ($seeMoreComments.hasClass('angle-up')) {
            $seeMoreComments.text('Collapse Comments')
        } else {
            $seeMoreComments.text('See Comments')
        }
    });
});