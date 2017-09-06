function getKuorumSuggestions(prefix, callback){

    var url = urls.suggestAlias
    var editor = this;
    var data ={term:prefix.slice(1), "boostedAlias":Array.from(debateFunctions.getActiveAlias())}
    var suggestionRequestsXHR = $.ajax({
        type: "POST",
        url: url,
        data: data,
        dataType: 'json'
    }).done(function(data){
        var suggestions = new Array();
        for (i = 0; i < data.suggestions.length; i++) {
            suggestions[i] = {
                alias:'@'+data.suggestions[i].alias,
                avatar:data.suggestions[i].avatar,
                name:data.suggestions[i].name,
                link:data.suggestions[i].link
            }
        }
        callback(suggestions)
    })
    return suggestionRequestsXHR;
}

var editor = new MediumEditor('.editable', {
    buttonLabels: 'fontawesome',
    targetBlank: true,
    disableDoubleReturn: false,
    toolbar: {
        buttons: ['anchor']
    },
    extensions: {
        "mention": new TCMention({
            tagName:"a",
            getSuggestions:getKuorumSuggestions,
            activeTriggerList: ["@"]
        })
    }
});



function prepareEditorComment(){
    var editorComment = new MediumEditor('.editable-comment', {
        buttonLabels: 'fontawesome',
        targetBlank: true,
        disableDoubleReturn: false,
        toolbar: {
            buttons: ['anchor']
        },
        anchorPreview: {
            /* These are the default options for anchor preview,
             if nothing is passed this is what it used */
            hideDelay: 500,
            previewValueSelector: 'a'
        },
        extensions: {
            "mention": new TCMention({
                tagName:"a",
                getSuggestions:getKuorumSuggestions,
                activeTriggerList: ["@"]
            })
        }
    });
}

prepareEditorComment();

$(function () {
    //Calculo de altura de las cajas de comentarios de debate
    $(window).load(function() {
        var parragraphMarginBottom = 20; //px
        var lineHeight= 33; //px

        var calculateParragraphLines = function (parragrahHeight, lastParragraph) {
            if (lastParragraph) {
                return parragrahHeight / lineHeight;
            } else {
                return (parragrahHeight - parragraphMarginBottom) / lineHeight;
            }
        };

        $('.conversation-box').each(function(idx, obj) {
            var $conversationBox = $($(obj).find('div.body'));
            var $conversationBoxParagraph = $($(obj).find('div.body p'));
            var parragraphNumber = 0;
            var totalParragraph = $conversationBoxParagraph.size();
            var parragraphInfo = {};

            $conversationBoxParagraph.each(function (idx, obj) {
                var parragrahHeight = $(obj).outerHeight(true);
                parragraphNumber += 1;
                parragraphInfo[idx] = {
                    height: parragrahHeight,
                    lines: calculateParragraphLines(parragrahHeight, parragraphNumber === totalParragraph)
                };
            });
            var boxHeight = 4 * lineHeight; //132 px;
            if (parragraphNumber > 1) { //en este caso habrá margenes entre párrafos
                var numberVisibleLines = 4;
                var idx = 0;
                while (numberVisibleLines > 0) {
                    if (!parragraphInfo.hasOwnProperty(idx)) {
                        numberVisibleLines = 0;
                    } else {
                        numberVisibleLines = numberVisibleLines - parragraphInfo[idx].lines;
                        idx += 1;
                    }
                }
                switch (idx) { //número de párrafos
                    case 2: //se necesitan usar dos párrafos para rellenar el espacio
                        var height = (parragraphInfo[0].lines * lineHeight) + parragraphMarginBottom + 
                                    (4 - parragraphInfo[0].lines) * lineHeight;
                        $conversationBox.height(height);
                        break;
                    case 3:
                        var height = (parragraphInfo[0].lines * lineHeight) + parragraphMarginBottom +
                                    (parragraphInfo[1].lines * lineHeight) + parragraphMarginBottom + 
                                    (4 - parragraphInfo[0].lines - parragraphInfo[1].lines) * lineHeight;
                        $conversationBox.height(height);
                        break;
                    case 4:
                        var height = (parragraphInfo[0].lines * lineHeight) + parragraphMarginBottom +
                                    (parragraphInfo[1].lines * lineHeight) + parragraphMarginBottom + 
                                    (parragraphInfo[2].lines * lineHeight) + parragraphMarginBottom + 
                                    (4 - parragraphInfo[0].lines - parragraphInfo[1].lines - parragraphInfo[2].lines) * lineHeight;
                        $conversationBox.height(height);
                        break;
                    default:
                        $conversationBox.height(4 * lineHeight); //132 px
                }
                boxHeight = $conversationBox.height();
            }
            $conversationBox.attr('data-height', boxHeight);
        });

        $( ".conversation-box-comments-list" ).each(function() {
            var $commentList = $(this).find("li.conversation-box-comment");
            collapseComments($commentList);
            actionShowMoreButton($commentList);
        });

        var sortProposals;

        checkPinnedFilter();

        sortProposals = new SortProposals();
        var hash = window.location.hash
        if (hash != undefined || hash != "") {
            hash = hash.substr(1);
            sortProposals.setProposalOption(hash)
        }
        sortProposals.reorderList();

        $("#proposal-option li a").on("click", function(e){
            var optionName = $(this).attr("href").substr(1);
            sortProposals.setProposalOption(optionName);
            sortProposals.reorderList();
        });

        // Marc as active the comment or the proposal
        openElement(hash)

        if (hash == "openProposal"){
            $(".leader-post > .footer .comment-counter button").trigger("click");
        }

        var $callToAction = $('body').find('.comment-box.call-to-action');
        $callToAction.find('.comment.editable').focus();
    });

    $(window).scroll(function () {
        var upperLimit = $("section#main .comment-box").offset();
        var buttonPosition = $("section#main .leader-post .header .call-to-action-mobile").offset();
        if (buttonPosition.top > upperLimit.top) {
            if ($(".call-to-action-mobile").is(":visible")) {
                $('.call-to-action-mobile').toggleClass('hidden');
            }
        }
        else if($(".call-to-action-mobile").hasClass("hidden")){
            if((buttonPosition.top + 150) < upperLimit.top){
                $('.call-to-action-mobile').toggleClass('hidden');
            }
        }
    });

    $(".call-to-action-mobile button").on("click", function(){
        var $proposalBox = $(".comment-box .comment.editable");
        var navbarHeight = $('.navbar').outerHeight();
        $('html, body').animate({
            scrollTop: $proposalBox.offset().top - navbarHeight - 40
        }, 1000, function () {
            $proposalBox.focus();
        });
    });

});

function collapseComments($commentList){
    var counter = $commentList.length;
    if (counter > 2){
        $commentList.slice(0, -2).addClass("hidden");
    }
}

function actionShowMoreButton($commentList){
    var counter = $commentList.length;
    if (counter > 2){
        var $showMoreButton = $commentList.closest(".conversation-box-comments").find(".show-more-comments");
        var hiddenComments = counter -2;
        changeShowMoreMessage($showMoreButton, hiddenComments);
    }
}

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
    $(".proposal-list").on('click','.conversation-box .footer .comment-counter button.comment', function (event) {
        var $conversationBox = $(event.target.parentElement).closest('.conversation-box');
        var $commentsBox = $conversationBox.next(".conversation-box-comments");
        var $mediumEditor = $commentsBox.find("div.editable-comment");
        $mediumEditor.focus();
        debateFunctions.conversationSectionClick($conversationBox)

    });


    // Save comment
    $(".proposal-list").on('click','.conversation-box-comments .comment-box .actions button.save-comment', debateFunctions.saveCommentButtonClick);

    // See More button (TOOGLE)
    $(".proposal-list").on('click','.conversation-box button.btn-see-more.stack', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $conversationBox = $actionsContent.closest('.conversation-box');
        var $conversationBoxBody = $actionsContent.closest('.conversation-box').find('.body');
        var collapsibleHeight = $conversationBoxBody.attr('data-height') || $conversationBoxBody.height();
        console.log("TOGGLE");
        $(event.target).toggleClass('fa-angle-down fa-angle-up');

        if ($conversationBoxBody.height() > collapsibleHeight) {
            $conversationBoxBody.animate({
                height: collapsibleHeight
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
            var pxMarginTopParent = 15;
            var pxMarginBottomParent = 20;
            var visibleHeight = 134;
            var maxDivHeight = visibleHeight +pxMarginTopParent+pxMarginBottomParent;
            if ($(this).height() > maxDivHeight ){
                $(this).addClass("collapsible")
            }
        });
    }
    prepareCollapsableBodys();



    // See More button (CLOSE)
    $(".proposal-list").on('click','.conversation-box-comments .go-up', function (event, isVisible, $target) {
        var $placeholder = $(event.target).parent();
        var $conversationComments;
        var $conversationCommentsList;
        if($target){
            $conversationComments = $target.closest(".comment-box");
            $conversationCommentsList = $conversationComments.parent();
        }
        else{
            $conversationCommentsList = $placeholder.parents(".conversation-box-comments");
            $conversationComments = $conversationCommentsList.find(".comment-box");
        }
        var $actionsContent = $(event.target).closest('.actions');
        var $commentsList = $conversationCommentsList.find(".conversation-box-comments-list");
        var $commentProposal = $conversationComments.find('.comment-proposal');
        var navbarHeight = $('.navbar').outerHeight();
        var $showMoreButton = $(event.target).closest('.go-up').parent();
        var $arrowButton = $showMoreButton.find('.hidden-comments');

        var collapsed = $commentsList.hasClass("collapsed");

        var $allComments = $conversationCommentsList.find('li.conversation-box-comment');
        var $hiddenComments = $conversationCommentsList.find('li.conversation-box-comment:hidden');
        $hiddenComments.removeClass("hidden");
        $hiddenComments.css("display", "none");

        if (!isVisible) {
            if (!collapsed){
                collapseComments($allComments);
            }
            $commentsList.toggleClass("collapsed");
            var $hiddenCommentsAfterCollapse = $conversationCommentsList.find('li.conversation-box-comment:hidden');
            var counter = $hiddenCommentsAfterCollapse.length;
            changeShowMoreMessage($showMoreButton, counter);
            $arrowButton.toggleClass('hidden');
            $hiddenComments.slideToggle(1000, function () {
                if (isVisible === false) {
                    $('html, body').animate({
                        scrollTop: $target.offset().top - navbarHeight - 40
                    }, 1000, function () {
                        $target.focus();
                    });
                }
            });
            if($hiddenComments.length === 0 && $target != undefined){
                $('html, body').animate({
                    scrollTop: $target.offset().top - navbarHeight - 40
                }, 1000, function () {
                    $target.focus();
                });
            }
        } else if (isVisible) {
            // $('html, body').animate({
            //     scrollTop: $target.offset().top - navbarHeight - 40
            // }, 1000, function () {
            //     $target.focus();
            // });
        }
    });

    $(".publish-proposal").on("click", debateFunctions.publishProposal);

    // Mark proposal as liked
    $(".proposal-list").on("click", ".proposal-like", debateFunctions.onClickProposalLike);

    // Pin proposal
    $(".proposal-list").on("click","button.pin-proposal", function() {
        var $button = $(this);
        var userLogged = $button.attr("data-userLogged");
        if (userLogged == undefined || userLogged == "" ) {
            // USER NO LOGGED
            $('#registro').modal('show');
        } else {
            $(this).toggleClass("active", "");
            var pin = $button.hasClass("active");
            var url =$button.attr("data-urlAction");
            var debateId = $button.attr("data-debateId");
            var debateAlias = $button.attr("data-debateAlias");
            var proposalId = $button.attr("data-proposalId");
            var data = {
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
                    //console.log(jsonData)
                    removeProposalFromFilter($button);
                    checkPinnedFilter();
                },
                dataType: "html"
            });
        }
    });

    // Handle notification menu
    $(".notification-menu").on("click", ".notification-text a", function(e){
        var href = $(this).attr("href");
        if (href.indexOf(window.location.pathname) >= 0 && href.indexOf("#")>=0){
            e.preventDefault();
            var hash = href.substring(href.indexOf("#")+1);
            openElement(hash)
        }
    });

    // DELETE PROPOSAL
    $(".proposal-list").on("click",".conversation-box button.delete",function(e){
        var $button = $(this)
        var buttonId = guid();
        $button.attr("id", buttonId);
        $modalButton = $("#modalDeleteDebateButton");
        $modalButton.attr("data-buttonId", buttonId)
        $modalButton.attr("data-deleteFunction", "deleteProposal")
        $("#debateDeleteConfirm").modal("show")
    });

    // DELEtE COMMENT
    $(".proposal-list").on("click",".conversation-box-comments ul.conversation-box-comments-list li.conversation-box-comment button.delete",function(e) {
        var $button = $(this)
        var buttonId = guid();
        $button.attr("id", buttonId);
        $modalButton = $("#modalDeleteDebateButton");
        $modalButton.attr("data-buttonId", buttonId)
        $modalButton.attr("data-deleteFunction", "deleteComment")
        $("#debateDeleteConfirm").modal("show")
    });

    // MODAL DELETE CONFIRM
    $("#modalDeleteDebateButton").on("click", function(e){
        e.preventDefault();
        var $button = $(this)
        var buttonId = $modalButton.attr("data-buttonId")
        var functionName = $modalButton.attr("data-deleteFunction")
        var $realButtonClick = $("#"+buttonId);
        deletes[functionName]($realButtonClick, function(){
            $("#debateDeleteConfirm").modal("hide")
        });

    });

    $(".proposal-list").on("click",".conversation-box-comment .footer button.angle",function(e) {
        var $button = $(this)
        var userAliasLogged = $button.attr("data-userAlias")
        if (userAliasLogged == undefined || userAliasLogged==""){
            var buttonId = guid();
            $button.attr("id", buttonId)
            $('#registro').find("form").attr("callback", "voteCommentNoLogged")
            $('#registro').find("form").attr("data-buttonId", buttonId)
            $('#registro').modal('show');
        }else{
            debateFunctions.voteComment($button)
        }

    });

    // INIT CALLBACKS
    noLoggedCallbacks['publishProposalNoLogged'] = function(){
        pageLoadingOn();
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $buttonPublish = $("#"+buttonId);
        $buttonPublish.attr("data-userLoggedAlias", "logged"); //Chapu para que el if no saque de nuevo la modal
        var eventFake = {target:$buttonPublish}
        pageLoadingOff();
        debateFunctions.publishProposal(eventFake, function(htmlProposal){
            console.log("inside");
            var proposal = $(htmlProposal).hide().fadeIn(2000);
            var proposalDivId = proposal.find("div.conversation-box").attr("id");
            console.log(proposalDivId);
            var href = document.location.href;
            var sharpPos = href.indexOf("#") < 0 ? href.length:href.indexOf("#");
            var newUrl = href.substring(0,sharpPos);
            newUrl = newUrl + "#"+proposalDivId
            document.location.href = newUrl;
            document.location.reload()
        })
    };

    noLoggedCallbacks['publishCommentNoLogged']= function(){
        var proposalId = $('#registro').find("form").attr("data-proposalId")
        var $buttonSaveComment = $("#proposal_"+proposalId).next("div").find(".actions button.save-comment")
        $buttonSaveComment.attr("data-userLogged", "logged"); //Chapu para que el if no saque de nuevo la modal
        var eventFake = {target:$buttonSaveComment}
        pageLoadingOff();
        debateFunctions.saveCommentButtonClick(eventFake, function($commentsList, htmlComment){
            pageLoadingOn();
            var comment = $(htmlComment).hide().fadeIn(2000);
            var commentDivId = comment.attr("id");
            var href = document.location.href;
            var sharpPos = href.indexOf("#") < 0 ? href.length:href.indexOf("#");
            var newUrl = href.substring(0,sharpPos);
            newUrl = newUrl + "#"+commentDivId
            document.location.href = newUrl;
            document.location.reload()
        })
    };
    noLoggedCallbacks['voteCommentNoLogged']= function(){
        var buttonId = $('#registro').find("form").attr("data-buttonId")
        var $button = $("#"+buttonId)
        debateFunctions.voteComment($button, function(){
            var commentDivId = $button.parents(".conversation-box-comment").attr("id")
            var href = document.location.href;
            var sharpPos = href.indexOf("#") < 0 ? href.length:href.indexOf("#");
            var newUrl = href.substring(0,sharpPos);
            newUrl = newUrl + "#"+commentDivId;
            document.location.href = newUrl;
            document.location.reload()
        })

    };
    noLoggedCallbacks['likeProposalNoLogged']= function(){
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#"+buttonId);
        debateFunctions.likeProposal($button, function(){
            var proposalId = $button.parents(".conversation-box").attr("id");
            var href = document.location.href;
            var sharpPos = href.indexOf("#") < 0 ? href.length:href.indexOf("#");
            var newUrl = href.substring(0,sharpPos);
            newUrl = newUrl + "#"+proposalId;
            document.location.href = newUrl;
            document.location.reload();
        })
    };

});

function changeShowMoreMessage($showMoreButton, numberHidden) {
    var $messageBuilder = $showMoreButton.find('.message-builder');
    var dataShow = $messageBuilder.attr("data-status-on");
    var dataHide = $messageBuilder.attr("data-status-off");
    var dataSingular = $messageBuilder.attr("data-syntax-singular");
    var dataSingularHide = $messageBuilder.attr("data-syntax-singularHide");
    var dataPlural = $messageBuilder.attr("data-syntax-plural");
    var dataPluralHide = $messageBuilder.attr("data-syntax-pluralHide");

    var $status = $showMoreButton.find('.status');
    var $hiddenElementsNumber = $showMoreButton.find('.comments');
    var $syntax = $showMoreButton.find('.syntax');

    var $commentsList = $showMoreButton.parent().find(".conversation-box-comments-list");
    var collapsed = $commentsList.hasClass("collapsed");

    if(collapsed){
        $status.text(dataShow);
        $hiddenElementsNumber.text(numberHidden);
        $hiddenElementsNumber.removeClass('hidden');
        (numberHidden !=1) ? $syntax.text(dataPlural): $syntax.text(dataSingular);
    }else{
        $status.text(dataHide);
        $hiddenElementsNumber.addClass('hidden');
        (numberHidden !=1) ? $syntax.text(dataPluralHide): $syntax.text(dataSingularHide);
    }

}

function validMediumEditor($mediumEditor){
    var text = $mediumEditor.html();
    if (text == undefined || text == ""){
        $mediumEditor.parent().addClass("error");
        return false
    }
    return true;
}

var debateFunctions = {
    getActiveAlias:function(){
        var aliases = new Set(); $("[data-useralias]").each(function(idx){aliases.add($(this).attr("data-useralias"))})
        return aliases
    },
    voteComment: function ($button, callback){
        var vote = -1;
        if ($button.find("span").hasClass("fa-angle-up")){
            vote = 1;
        }
        var $proposalDiv = $button.parents("div.conversation-box-comments").prev()
        var $commentLi = $button.parents("li.conversation-box-comment")
        var proposalDivId = $proposalDiv.attr("id")
        var proposalId = proposalDivId.substring(proposalDivId.indexOf("_")+1);
        var debateId = $proposalDiv.attr("data-debateId")
        var debateAlias = $proposalDiv.attr("data-debateAlias")
        var commentId = $button.attr("data-commentId")
        var url = $button.attr("data-ajaxVote")
        var $number = $button.siblings(".number")
        var data = {
            proposalId:proposalId,
            debateId:debateId,
            debateAlias:debateAlias,
            commentId:commentId,
            vote:vote
        }

        pageLoadingOn();
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function(jsonData){
                $number.html(jsonData.votes)
                $button.parents(".footer-comment-votes").removeClass("vote-up");
                $button.parents(".footer-comment-votes").removeClass("vote-down");
                if (vote > 0){
                    $button.parents(".footer-comment-votes").addClass("vote-up");
                }else{
                    $button.parents(".footer-comment-votes").addClass("vote-down");
                }
                if (callback != undefined){
                    callback()
                }
            },
            error:function(){
                display.error("Sorry: Error voting comment")
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    onClickProposalLike: function(e) {
        var $button = $(this);
        var userLogged = $button.attr("data-userLogged");
        if (userLogged == undefined || userLogged == "" ) {
            // USER NOT LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId)
            $('#registro').find("form").attr("callback", "likeProposalNoLogged")
            $('#registro').find("form").attr("data-buttonId", buttonId)
            $('#registro').modal('show');
        } else {
            debateFunctions.likeProposal($button);
        }
    },
    likeProposal: function($button, callback){
        // Unbind
        $(".proposal-list").off("click", ".proposal-like");

        var like = $button.find(".fa").hasClass("fa-heart-o"); // Empty heart -> Converting to LIKE = TRUE

        // Prediction: toggle button
        $button.toggleClass("active");
        $button.find(".fa").toggleClass("fa-heart-o fa-heart");
        var count = parseInt($button.find(".number").text());
        if (like) {
            $button.find(".number").text(count + 1)
        } else {
            $button.find(".number").text(count - 1)
        }

        var url = $button.attr("data-urlAction");
        var debateId = $button.attr("data-debateId");
        var debateAlias = $button.attr("data-debateAlias");
        var proposalId = $button.attr("data-proposalId");
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
            success: function () {
                if (callback != undefined){
                    callback()
                }
            },
            error: function () {
                // Recover from prediction
                $button.toggleClass("active");
                $button.find(".fa").toggleClass("fa-heart-o fa-heart");
                var count = parseInt($button.find(".number").text());
                if (like) {
                    $button.find(".number").text(count - 1)
                } else {
                    $button.find(".number").text(count + 1)
                }
            },
            complete: function () {
                // Re-bind
                $(".proposal-list").on("click", ".proposal-like", debateFunctions.onClickProposalLike);
            },
            dataType: "html"
        });
    },
    printProposal: function(htmlProposal){
        var proposal = $(htmlProposal).hide().fadeIn(2000);
        var proposalDivId = proposal.find("div.conversation-box").attr("id");
        $(".proposal-list").prepend(proposal);

        // Update proposal counter
        var $counterProposals = $('.leader-post .comment-counter .number');
        $counterProposals.text(parseInt($counterProposals.text()) + 1);
        $("#proposal-option a[href=#latest]").trigger("click");

        var navbarHeight = $('.navbar').outerHeight();
        $('html, body').animate({
            scrollTop: $('#'+proposalDivId).offset().top - navbarHeight - 40
        }, 1000, function () {
            $('#'+proposalDivId).focus();
        });

        prepareEditorComment();
    },
    publishProposal: function(e, callback){
        e = e || window.event;
        callback = callback || debateFunctions.printProposal;
        var $buttonPublish = $(e.target);
        var alias = $buttonPublish.attr("data-userLoggedAlias");
        var $mediumEditor = $buttonPublish.parents(".comment-box").find(".comment.editable.medium-editor-element");
        var content = $mediumEditor.find("p").text();
        var isValidContent = content?true:false;
        var errorSpan = $buttonPublish.parents(".comment-box").find("span.error");
        if(!isValidContent){
            errorSpan.show();
            return;
        }
        errorSpan.hide();
        if (alias == ""){
            // USER NO LOGGED
            var buttonId = guid();
            $buttonPublish.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "publishProposalNoLogged");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }else{
            if (!validMediumEditor($mediumEditor)){return;}
            pageLoadingOn();
            $buttonPublish.off("click");
            var body = $mediumEditor.html();
            var debateId = $buttonPublish.attr("data-debateId");
            var debateAlias = $buttonPublish.attr("data-debateAlias");
            var url = $buttonPublish.attr("data-postUrl");
            var data={
                debateId:debateId,
                debateAlias:debateAlias,
                body:body
            };
            $.ajax({
                type: "POST",
                url: url,
                data: data,
                success: function (htmlProposal) {
                    callback(htmlProposal);
                    $mediumEditor.html("");
                    if (typeof(dataLayer) != "undefined"){
                        dataLayer.push({
                            'event': 'debate-proposal-published',
                            'pageCategory':'debate',
                            'debateId':debateId,
                            'debateAlias':debateAlias,
                            'alias':alias
                        })
                    }
                },
                complete : function(){
                    $buttonPublish.on("click",debateFunctions.publishProposal)
                    pageLoadingOff();
                },
                dataType: "html"
            });
        }
    },
    printComment: function($commentsList, htmlComment){
        var comment = $(htmlComment).hide().fadeIn(2000);
        $commentsList.append(comment)
    },
    saveCommentButtonClick: function (e, callback){
        if (isPageLoading()){
            return;
        }
        var $button = $(e.target)
        var $conversationBox = $($button.parents('.conversation-box-comments')[0]).prev();
        var $commentsList = $conversationBox.next().children(".conversation-box-comments-list");
        // if ( !$commentsList.hasClass("collapsed") ){
        //     debateFunctions.saveComment($commentsList,$button, callback)
        // }else{
        //     // ABRIR COMENTARIOS
        //     debateFunctions.conversationSectionClick($conversationBox)
        // }
        debateFunctions.saveComment($commentsList,$button, callback)
    },
    saveComment:function($commentsList, $button, callback){
        callback = callback || debateFunctions.printComment;
        var userLogged = $button.attr("data-userLogged");
        var debateId = $button.attr("data-debateId");
        var debateAlias = $button.attr("data-debateAlias");
        var proposalId = $button.attr("data-proposalId");
        var $mediumEditor = $button.parents('.comment-box').find('.editable-comment');
        if (userLogged == undefined || userLogged == "" ){
            // USER NO LOGGED
            $('#registro').find("form").attr("callback", "publishCommentNoLogged")
            $('#registro').find("form").attr("data-proposalId", proposalId)
            $('#registro').modal('show');
        }else {
            // BOTON SALVAR
            if (!validMediumEditor($mediumEditor)) {
                return;
            }
            pageLoadingOn();
            var body = $mediumEditor.html();
            var url = $button.attr("data-postUrl");
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
                    callback($commentsList, htmlComment);
                    $mediumEditor.html("")
                },
                dataType: "html",
                complete: function(){
                    pageLoadingOff();
                }
            });
        }
    },
    conversationSectionClick: function($conversationBox){
        var $conversationBoxComments = $conversationBox.next('.conversation-box-comments');
        var $conversationBoxCommentsComment = $conversationBoxComments.find('.comment-proposal > .editable-comment');
        var $conversationBoxCommentsArrow = $conversationBoxComments.find('.go-up');
        var navbarHeight = $('.navbar').height();

        var isVisible = $conversationBoxCommentsComment.is(':visible');
        console.log($conversationBoxCommentsComment);

        $conversationBoxCommentsArrow.trigger('click', [isVisible, $conversationBoxCommentsComment]);
    }
}

var deletes ={
    deleteProposal: function($button, callback){
        var $proposalDiv = $button.parents("div.conversation-box")
        var proposalDivId = $proposalDiv.attr("id")
        var proposalId = proposalDivId.substring(proposalDivId.indexOf("_")+1);
        var debateId = $proposalDiv.attr("data-debateId")
        var debateAlias = $proposalDiv.attr("data-debateAlias")
        var url = $button.attr("data-ajaxDelete")
        var data = {
            proposalId:proposalId,
            debateId:debateId,
            debateAlias:debateAlias
        }
        pageLoadingOn();
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function(jsonData){
                $proposalDiv.parent("li").fadeOut();
                if (callback != undefined){
                    callback()
                }
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },
    deleteComment: function ($button, callback){
        var $proposalDiv = $button.parents("div.conversation-box-comments").prev()
        var $commentLi = $button.parents("li.conversation-box-comment")
        var proposalDivId = $proposalDiv.attr("id")
        var proposalId = proposalDivId.substring(proposalDivId.indexOf("_")+1);
        var debateId = $proposalDiv.attr("data-debateId")
        var debateAlias = $proposalDiv.attr("data-debateAlias")
        var commentId = $button.attr("data-commentId")
        var url = $button.attr("data-ajaxDelete")
        var data = {
            proposalId:proposalId,
            debateId:debateId,
            debateAlias:debateAlias,
            commentId:commentId
        }

        pageLoadingOn();
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function(jsonData){
                $commentLi.fadeOut();
                if (callback != undefined){
                    callback()
                }
            },
            error:function(){
                display.error("Sorry: Error deleting comment")
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    }
}

function SortProposals() {
    var that = this;
    var proposalList = $('ul.proposal-list');

    this.proposalsOptions = {};
    this.proposalsOptions['latest']={
        sort:function(a,b){
            var aDateTime = $(a).find(".conversation-box time.timeago").attr('datetime');
            var bDateTime = $(b).find(".conversation-box time.timeago").attr('datetime');
            return bDateTime.localeCompare(aDateTime);
        },
        filter:function(idx){return false;},
        name:"latest"
    };
    this.proposalsOptions['oldest']={
        sort:function(a,b){
            var aDateTime = $(a).find(".conversation-box time.timeago").attr('datetime');
            var bDateTime = $(b).find(".conversation-box time.timeago").attr('datetime');
            return aDateTime.localeCompare(bDateTime);
        },
        filter:function(idx){return false;},
        name:"oldest"
    };
    this.proposalsOptions['best']={
        sort:function(a,b){
            var aLikes = parseInt($(a).find(".comment-counter button.proposal-like .number").text().trim());
            var bLikes = parseInt($(b).find(".comment-counter button.proposal-like .number").text().trim());
            return bLikes - aLikes;
        },
        filter:function(idx){return false;},
        name:"best"
    };
    this.proposalsOptions['pinned']={
        sort:that.proposalsOptions.latest.sort,
        filter:function(idx){return !$(this).find(".pin-proposal").hasClass("active");},
        name:"pinned"
    };

    var proposalOption = that.proposalsOptions.latest;

    this.setProposalOption = function(option){
        var opt = that.proposalsOptions[option];
        if (opt == undefined){
            opt = that.proposalsOptions.latest
        }
        proposalOption = opt;
    };

    this.reorderList = function(){
        var proposals = proposalList.children('li').get();
        if(proposals.length>0){
            $("#proposal-option").show()
        }
        $("#proposal-option li").removeClass("active");
        $("a[href=#"+proposalOption.name+"]").parent().addClass("active");
        proposals.sort(proposalOption.sort);
        $('ul.proposal-list > li').show();
        $('ul.proposal-list > li').filter(proposalOption.filter).hide();
        $.each(proposals, function(idx, itm) { proposalList.append(itm); });
    }
}

function removeProposalFromFilter(element){
    var active = element.hasClass('active');
    if(!active){
        sortProposals.reorderList();
    }
}

function openElement(hash){
    var $element = $("#" + hash);
    if ($element != undefined) {
        $element.addClass("active");
        var $commentBox = $element.parents(".conversation-box-comments");
        if ($commentBox != undefined) {
            $commentBox.find("button.go-up[data-anchor=conversation-box]").click()
        }
        if ($element.offset() != undefined) { // Fails some times
            $('html, body').animate({
                scrollTop: $element.offset().top -100
            }, 2000, function () {});
        }
    }
}

function checkPinnedFilter (){

    if($('.proposal-list ul.icons .active').length == 0){
        $('#proposal-option li').find('a[href*="pinned"]').hide();
        $('#proposal-option li').find('a[href*="latest"]').click();
        if(window.location.hash == '#pinned' ){
            window.location.hash = '#latest';
        }
    } else{
        $('#proposal-option li').find('a[href*="pinned"]').show();
    }
}

