
var editor = new MediumEditor('.editable', {
    buttonLabels: 'fontawesome',
    targetBlank: true,
    disableDoubleReturn: false,
    toolbar: {
        buttons: ['anchor']
    }
});

var editorComment = new MediumEditor('.editable-comment', {
    buttonLabels: 'fontawesome',
    targetBlank: true,
    disableDoubleReturn: false,
    anchorPreview: {
        /* These are the default options for anchor preview,
         if nothing is passed this is what it used */
        hideDelay: 500,
        previewValueSelector: 'a'
    },
    toolbar: {
        buttons: ['anchor']
    }
});

$(function () {
    //Calculo de altura de las cajas de comentarios de debate
    $(window).load(function() { 
        $('.conversation-box').each(function(idx, obj) {
            var conversationBox = $(obj).find('div.body');
            var conversationBoxParagraph = $(obj).find('div.body p');
            var totalHeight = 0;
            var parragraphNumber = 0;
            var parragraphInfo = {};
            var parragraphMarginBottom = 20; //px
            var lineHeight= 33; //px

            $(conversationBoxParagraph).each(function (idx, obj) {
                totalHeight += $(obj).outerHeight(true);
                parragraphNumber += 1;
                parragraphInfo[idx] = {height: $(obj).outerHeight(true)};
            });
            if (parragraphNumber > 1) { //en este caso habrá margenes entre párrafos
                var firstParragraphLines = (parragraphInfo[0].height - parragraphMarginBottom) / lineHeight;
                console.log('num lineas: ', firstParragraphLines);
                if (firstParragraphLines < 4) {
                    $(conversationBox).height((4 * lineHeight) + parragraphMarginBottom);
                } else {
                    $(conversationBox).height(4 * lineHeight); //132 px
                }
            }
        });
    });
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
    $(".proposal-list").on('click','.conversation-box .footer .comment-counter button.comment', function (event) {
        var $conversationBox = $(event.target.parentElement).closest('.conversation-box');
        conversationSectionClick($conversationBox)

    });


    // Save comment
    $(".proposal-list").on('click','.conversation-box-comments .comment-box .actions button.save-comment', saveCommentButtonClick);

    function saveCommentButtonClick(e, callback){
        if (isPageLoading()){
            return;
        }
        var $button = $(e.target)
        var $conversationBox = $($button.parents('.conversation-box-comments')[0]).prev();
        var $commentsList = $conversationBox.next().children(".conversation-box-comments-list");
        if ( $commentsList.is(":visible") ){
            var userLogged = $button.attr("data-userLogged")
            var debateId = $button.attr("data-debateId");
            var debateAlias = $button.attr("data-debateAlias");
            var proposalId = $button.attr("data-proposalId");
            if (userLogged == undefined || userLogged == "" ){
                // USER NO LOGGED
                console.log("No LOGGED")
                $('#registroDebate').find("form").attr("callback", "publishComment")
                $('#registroDebate').find("form").attr("data-proposalId", proposalId)
                $('#registroDebate').modal('show');
            }else {
                // BOTON SALVAR
                var $mediumEditor = $button.parents('.comment-box').find('.editable-comment');
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
                        var comment = $(htmlComment).hide().fadeIn(2000);
                        var commentDivId = comment.attr("id");
                        console.log(commentDivId)
                        $commentsList.append(comment)
                        $mediumEditor.html("")

                        if (callback != undefined){
                            callback(commentDivId)
                        }
                    },
                    dataType: "html",
                    complete: function(){
                        pageLoadingOff();
                    }
                });
            }
        }else{
            // ABRIR COMENTARIOS
            conversationSectionClick($conversationBox)
        }
    }

    function conversationSectionClick($conversationBox){
        var $conversationBoxComments = $conversationBox.next('.conversation-box-comments');
        var $conversationBoxCommentsComment = $conversationBoxComments.find('.comment-box .editable-comment');
        var $conversationBoxCommentsArrow = $conversationBoxComments.find('.go-up');
        var navbarHeight = $('.navbar').height();

        var isVisible = $conversationBoxCommentsComment.is(':visible');

        $conversationBoxCommentsArrow.trigger('click', [isVisible, $conversationBoxCommentsComment]);
    }

    // See More button (OPEN)
    $(".proposal-list").on('click','.conversation-box button.btn-see-more.stack', function (event) {
        var $actionsContent = $(event.target).closest('.actions');
        var $conversationBox = $actionsContent.closest('.conversation-box');
        var $conversationBoxBody = $actionsContent.closest('.conversation-box').find('.body');
        var collapsibleHeight = 134;

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

    $(".publish-proposal").on("click", publishProposal);

    $('#registroDebate form[name=login-header] input[type=submit]').on("click", function(e){
        e.preventDefault();
        e.stopPropagation();
        var $form = $(this).parents("form[name=login-header]");
        var callback = $form.attr("callback")
        modalLogin($form, noLoggedCallbacks[callback]);
    });

    $('#registroDebate form[name=debate-modal] input[type=submit]').on("click", function(e){
        e.preventDefault();
        var $form = $(this).parents("form[name=debate-modal]")
        var callback = $form.attr("callback")
        modalRegister($form, noLoggedCallbacks[callback]);
    })

    var noLoggedCallbacks = {
        publishProposal: function(){
            var $buttonPublish = $(".publish-proposal")
            $buttonPublish.attr("data-userLoggedAlias", "logged"); //Chapu para que el if no saque de nuevo la modal
            var eventFake = {target:$buttonPublish}
            publishProposal(eventFake, function(proposalDivId){
                var href = document.location.href;
                var sharpPos = href.indexOf("#") < 0 ? href.length:href.indexOf("#");
                var newUrl = href.substring(0,sharpPos);
                newUrl = newUrl + "#"+proposalDivId
                document.location.href = newUrl;
                document.location.reload()
            })
        },
        publishComment: function(){
            var proposalId = $('#registroDebate').find("form").attr("data-proposalId")
            var $buttonSaveComment = $("#proposal_"+proposalId).next("div").find(".actions button.save-comment")
            $buttonSaveComment.attr("data-userLogged", "logged"); //Chapu para que el if no saque de nuevo la modal
            var eventFake = {target:$buttonSaveComment}
            pageLoadingOff();
            saveCommentButtonClick(eventFake, function(commentDivId){
                pageLoadingOn();
                var href = document.location.href;
                var sharpPos = href.indexOf("#") < 0 ? href.length:href.indexOf("#");
                var newUrl = href.substring(0,sharpPos);
                newUrl = newUrl + "#"+commentDivId
                document.location.href = newUrl;
                document.location.reload()
            })
        }
    }


    function publishProposal(e, callback){
        e = e || window.event;
        var $buttonPublish = $(e.target)
        console.log($buttonPublish );
        var alias = $buttonPublish.attr("data-userLoggedAlias")
        if (alias == ""){
            // USER NO LOGGED
            $('#registroDebate').find("form").attr("callback", "publishProposal")
            $('#registroDebate').modal('show');
        }else{
            var $mediumEditor = $(".comment.editable.medium-editor-element");
            if (!validMediumEditor($mediumEditor)){return;}
            pageLoadingOn();
            $buttonPublish.off("click")
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
                    console.log("Success ajax")
                    var proposal = $(htmlProposal).hide().fadeIn(2000);
                    var proposalDivId = proposal.find("div.conversation-box").attr("id");
                    $(".proposal-list").prepend(proposal);
                    $mediumEditor.html("");

                    // Update proposal counter
                    var $counterProposals = $('.leader-post .comment-counter .number');
                    $counterProposals.text(parseInt($counterProposals.text()) + 1);
                    $("#proposal-option a[href=#latest]").trigger("click");
                    if (callback != undefined){
                        callback(proposalDivId)
                    }
                },
                complete : function(){
                    $buttonPublish.on("click",publishProposal)
                    pageLoadingOff();
                },
                dataType: "html"
            });
        }
    }

    function onClickProposalLike() {
        // Unbind
        $(".proposal-list").off("click", ".proposal-like");

        var $button = $(this);
        var userLogged = $button.attr("data-userLogged");
        if (userLogged == undefined || userLogged == "" ) {
            // USER NOT LOGGED
            $('#registro').modal('show');
        } else {
            var like = $(this).find(".fa").hasClass("fa-heart-o"); // Empty heart -> Converting to LIKE = TRUE

            // Prediction: toggle button
            $button.toggleClass("active");
            $button.find(".fa").toggleClass("fa-heart-o fa-heart");
            var count = parseInt($button.find(".number").text());
            if (like) {
                $button.find(".number").text(count + 1)
            } else {
                $button.find(".number").text(count - 1)
            }

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
                success: function () {

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
                    $(".proposal-list").on("click", ".proposal-like", onClickProposalLike);
                },
                dataType: "html"
            });
        }
    }

    // Mark proposal as liked
    $(".proposal-list").on("click", ".proposal-like", onClickProposalLike);

    // Pin proposal
    $(".proposal-list").on("click","button.pin-propusal", function() {
        var userLogged = $(this).attr("data-userLogged");
        if (userLogged == undefined || userLogged == "" ) {
            // USER NO LOGGED
            $('#registro').modal('show');
        } else {
            $(this).toggleClass("active", "");
            var pin = $(this).hasClass("active");
            var url =$(this).attr("data-urlAction");
            var debateId = $(this).attr("data-debateId");
            var debateAlias = $(this).attr("data-debateAlias");
            var proposalId = $(this).attr("data-proposalId");
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
                    console.log(jsonData)
                },
                dataType: "html"
            });
        }
    });

    function validMediumEditor($mediumEditor){
        var text = $mediumEditor.html();
        if (text == undefined || text == ""){
            $mediumEditor.parent().addClass("error");
            return false
        }
        return true;
    }

    // Handle notification menu
    $(".notification-menu").on("click", ".notification-text a", function(e){
        var href = $(this).attr("href");
        if (href.indexOf(window.location.pathname) >= 0 && href.indexOf("#")>=0){
            e.preventDefault();
            var hash = href.substring(href.indexOf("#")+1);
            openElement(hash)
        }
    });
});


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
            var aDateTime = $(a).find(".comment-counter .fa-heart-o").next().text();
            var bDateTime = $(b).find(".comment-counter .fa-heart-o").next().text();
            return aDateTime.localeCompare(bDateTime);
        },
        filter:function(idx){return false;},
        name:"best"
    };
    this.proposalsOptions['pinned']={
        sort:that.proposalsOptions.latest.sort,
        filter:function(idx){return !$(this).find(".pin-propusal").hasClass("active");},
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
var sortProposals;
$(function () {
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
        $(".leader-post > .footer .comment-counter button").trigger("click")
    }
});

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
