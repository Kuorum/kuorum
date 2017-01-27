
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
        var $button = $(this)
        var $conversationBox = $($button.parents('.conversation-box-comments')[0]).prev();
        var $commentsList = $conversationBox.next().children(".conversation-box-comments-list");
        if ( $commentsList.is(":visible") ){
            var userLogged = $button.attr("data-userLogged")
            if (userLogged == undefined || userLogged == "" ){
                // USER NO LOGGED
                $('#registro').modal('show');
            }else {
                // BOTON SALVAR
                var $mediumEditor = $button.parents('.comment-box').find('.editable-comment');
                var body = $mediumEditor.html();
                if (!validMediumEditor($mediumEditor)) {
                    return;
                }
                var debateId = $(this).attr("data-debateId");
                var debateAlias = $(this).attr("data-debateAlias");
                var proposalId = $(this).attr("data-proposalId");
                var url = $(this).attr("data-postUrl");
                var data = {
                    debateId: debateId,
                    debateAlias: debateAlias,
                    proposalId: proposalId,
                    body: body
                };
                $.ajax({
                    type: "POST",
                    url: url,
                    data: data,
                    success: function (htmlComment) {
                        var comment = $(htmlComment).hide().fadeIn(2000);
                        $commentsList.append(comment)
                        $mediumEditor.html("")
                    },
                    dataType: "html"
                });
            }
        }else{
            // ABRIR COMENTARIOS
            conversationSectionClick($conversationBox)
        }
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
            var $mediumEditor = $(".comment.editable.medium-editor-element p");
            var body = $mediumEditor.html();
            if (!validMediumEditor($mediumEditor)){return;}
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
                    $("#proposal-option a[href=#latest]").trigger("click")
                    var proposal = $(htmlProposal).hide().fadeIn(2000);
                    $(".proposal-list").prepend(proposal)
                    $mediumEditor.html("")
                },
                dataType: "html"
            });
        }
    });

    $("#main").on("click",".proposal-like", function(){
        var $button = $(this)
        var userLogged = $button.attr("data-userLogged")
        if (userLogged == undefined || userLogged == "" ){
            // USER NO LOGGED
            $('#registro').modal('show');
        }else {
            var like = $(this).find(".fa").hasClass("fa-heart-o"); // Empty heart -> Converting to LIKE = TRUE
            var url = $(this).attr("data-urlAction");
            var debateId = $(this).attr("data-debateId");
            var debateAlias = $(this).attr("data-debateAlias");
            var proposalId = $(this).attr("data-proposalId");
            var data = {
                debateId: debateId,
                debateAlias: debateAlias,
                proposalId: proposalId,
                like: like
            };
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (jsonData) {
                    console.log("success")
                    $button.find(".fa").toggleClass("fa-heart-o fa-heart");
                    var count = parseInt($button.find(".number").text())
                    if (like) {
                        $button.find(".number").text(count + 1)
                    } else {
                        $button.find(".number").text(count - 1)
                    }
                },
                dataType: "html"
            });
        }
    })

    $(".pin-propusal").on("click", function(){
        var userLogged = $(this).attr("data-userLogged");
        if (userLogged == undefined || userLogged == "" ){
            // USER NO LOGGED
            $('#registro').modal('show');
        }else{
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
                dataType: "html"
            });
        }
    })

    function validMediumEditor($mediumEditor){
        var text = $mediumEditor.html();
        if (text == undefined || text == ""){
            console.log($mediumEditor.parent())
            $mediumEditor.parent().addClass("error")
            return false
        }
        return true;
    }
});


function SortProposals(){
    var that = this;
    var proposalList = $('ul.proposal-list');


    this.proposalsOptions = {}
    this.proposalsOptions['latest']={
        sort:function(a,b){
            var aDateTime = $(a).find(".conversation-box time.timeago").attr('datetime')
            var bDateTime = $(b).find(".conversation-box time.timeago").attr('datetime')
            return bDateTime.localeCompare(aDateTime);
        },
        filter:function(idx){return false;},
        name:"latest"
    }
    this.proposalsOptions['oldest']={
        sort:function(a,b){
            var aDateTime = $(a).find(".conversation-box time.timeago").attr('datetime')
            var bDateTime = $(b).find(".conversation-box time.timeago").attr('datetime')
            return aDateTime.localeCompare(bDateTime);
        },
        filter:function(idx){return false;},
        name:"oldest"
    }
    this.proposalsOptions['best']={
        sort:function(a,b){
            var aDateTime = $(a).find(".comment-counter .fa-heart-o").next().text()
            var bDateTime = $(b).find(".comment-counter .fa-heart-o").next().text()
            return bDateTime.localeCompare(aDateTime);
        },
        filter:function(idx){return false;},
        name:"best"
    }
    this.proposalsOptions['pinned']={
        sort:that.proposalsOptions.latest.sort,
        filter:function(idx){return !$(this).find(".pin-propusal").hasClass("active");},
        name:"pinned"
    }

    var proposalOption = that.proposalsOptions.latest;

    this.setProposalOption = function(option){
        var opt = that.proposalsOptions[option];
        if (opt == undefined){
            opt = that.proposalsOptions.latest
        }
        proposalOption = opt;
    }

    this.reorderList = function(){
        var proposals = proposalList.children('li').get();
        $("#proposal-option li").removeClass("active")
        $("a[href=#"+proposalOption.name+"]").parent().addClass("active")
        proposals.sort(proposalOption.sort);
        $('ul.proposal-list > li').show();
        $('ul.proposal-list > li').filter(proposalOption.filter).hide();
        $.each(proposals, function(idx, itm) { proposalList.append(itm); });
    }
}
var sortProposals;
$(function(){
    sortProposals = new SortProposals()
    var hash = window.location.hash
    if (hash != undefined || hash != ""){
        hash = hash.substr(1);
        sortProposals.setProposalOption(hash)
    }
    sortProposals.reorderList()

    $("#proposal-option li a").on("click", function(e){
        var optionName = $(this).attr("href").substr(1);
        sortProposals.setProposalOption(optionName);
        sortProposals.reorderList();
    })
});
