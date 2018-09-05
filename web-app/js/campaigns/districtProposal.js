$(function () {
    
    $("body").on("click",".districtProposal-support, .districtProposal-vote", districtProposalHelper.bindActionClick)

    noLoggedCallbacks['districtProposalAction']= function(){
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#"+buttonId);
        pageLoadingOff();
        districtProposalHelper.districtProposalAction($button, noLoggedCallbacks.reloadPage);
    };
});

var districtProposalHelper={
    bindActionClick : function(e){
        e.preventDefault();
        event.stopPropagation();
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == ""){
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "districtProposalAction");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        }else{
            districtProposalHelper.districtProposalAction($button);
        }
    },

    districtProposalAction : function($button, callback){
        console.log("districtProposalAction")
        if(isPageLoading()){
            return;
        }
        pageLoadingOn();
        var params = {
            callback: callback,
            $button : $button
        }
        var loggedUser = $button.attr('data-loggedUser');
        var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(districtProposalHelper.__executableDistrictProposalAction, params)
        var validationActive = $button.attr('data-campaignValidationActive');
        if (validationActive=="true"){
            userValidatedByDomain.checkUserValid(loggedUser, executableFunction)
        }else{
            executableFunction.exec()
        }
    },

    __executableDistrictProposalAction: function(params){
        var callback = params.callback;
        var $button = params.$button;
        var url = $button.attr("href")
        var districtId = $button.attr("data-districtId")
        var participatoryBudgetId = $button.attr("data-participatoryBudgetId")
        var proposalId = $button.attr("data-proposalId")
        var voteOn = $button.hasClass("on") || $button.hasClass("active")
        var vote = !voteOn;
        var data = {
            districtId:districtId,
            participatoryBudgetId:participatoryBudgetId,
            proposalId:proposalId,
            vote:vote
        }

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(districtProposalRSDTO){
                // $button.find('.fa').toggleClass("fa-heart-o fa-heart");
                // $button.find('.number').text(postRSDTO.likes);
                if ($button.hasClass("districtProposal-support")){
                    districtProposalHelper.districtProposalSupportExtraAction(districtProposalRSDTO)
                }
                if ($button.hasClass("districtProposal-vote")){
                    districtProposalHelper.districtProposalVoteExtraAction(districtProposalRSDTO)
                }
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

    },

    districtProposalVoteExtraAction:function (districtProposalRSDTO) {
        console.log(districtProposalRSDTO)
        $buttonColumnC = $("#aside-ppal .call-to-action .actions a")
        if (districtProposalRSDTO.voted){
            $(".leader-post .footer .comment-counter button").addClass("active");
            $(".leader-post .footer .comment-counter button").find(".fa-shopping-cart").addClass("fas");
            $(".leader-post .footer .comment-counter button").find(".fa-shopping-cart").removeClass("fal");
            $buttonColumnC.addClass('on')
            $buttonColumnC.blur()
        }else{
            $(".leader-post .footer .comment-counter button").removeClass("active");
            $(".leader-post .footer .comment-counter button").find(".fa-shopping-cart").removeClass("fas");
            $(".leader-post .footer .comment-counter button").find(".fa-shopping-cart").addClass("fal");
            $buttonColumnC.removeClass('on')
            $buttonColumnC.blur()
        }
        $(".leader-post .footer .comment-counter button").find(".number").html(districtProposalRSDTO.numVotes);
        var width = Math.round(districtProposalRSDTO.district.amountUserInvested / districtProposalRSDTO.district.budget * 100 )  ;
        $(".budget .progress-bar-custom .progress-bar-custom-done").animate({width:width+'%'},"slow")
        $(".budget .campaign-progress-bar .pop-up .amount-user-invested").html(districtProposalRSDTO.district.amountUserInvested);
    },

    districtProposalSupportExtraAction:function (districtProposalRSDTO) {
        // console.log(districtProposalRSDTO)
        $buttonColumnC = $("#aside-ppal .call-to-action .actions a")
        if (districtProposalRSDTO.supported){
            $(".leader-post .footer .comment-counter button").addClass("active");
            $(".leader-post .footer .comment-counter button").find(".fa-rocket").addClass("fas");
            $(".leader-post .footer .comment-counter button").find(".fa-rocket").removeClass("fal");
            $buttonColumnC.addClass('on')
            $buttonColumnC.blur()
        }else{
            $(".leader-post .footer .comment-counter button").removeClass("active");
            $(".leader-post .footer .comment-counter button").find(".fa-rocket").removeClass("fas");
            $(".leader-post .footer .comment-counter button").find(".fa-rocket").addClass("fal");
            $buttonColumnC.removeClass('on')
            $buttonColumnC.blur()
        }
        $(".leader-post .footer .comment-counter button").find(".number").html(districtProposalRSDTO.numSupports);
    }

}