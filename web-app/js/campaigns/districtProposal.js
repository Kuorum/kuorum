$(function () {
    
    $("body").on("click",".districtProposal-support, .districtProposal-vote", districtProposalHelper.bindActionClick)
    $("body").on("click", ".comment-counter .disabled", function(e){e.preventDefault();})
    $(".districtProposal-support, .districtProposal-vote").on("click", districtProposalHelper.bindActionClick)

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
        e.stopPropagation();
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
        $('#registro').modal('hide');
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
                $button.blur()
            },
            error: function(xhr, textStatus, errorThrown){
                var errorJson = JSON.parse(xhr.responseText);
                if (errorJson.error == "API_ERROR"){
                    if (errorJson.code == "error.api.SERVICE_CAMPAIGN_PARTICIPATORY_BUDGET_BUDGET_OVERFLOW"){
                        var modalId = "#warn-district-budget-overflow-"+districtId;
                        $(modalId).modal()
                        prepareProgressBar();
                    }else{
                        var modalId = "#warn-different-district-"+districtId;
                        $(modalId).find(".modal-body p:first-child").html(errorJson.msg)
                        $(modalId).modal()
                    }

                    if (callback != undefined){
                        $(modalId).on('hidden.bs.modal', function () {
                            callback();
                        })
                    }
                }else{
                    display.error("There was an unexpected error")
                    if (callback != undefined){
                        callback();
                    }
                }
            },
            complete: function(){
                //$button.on('click', postFunctions.bindLikeClick);
                pageLoadingOff();
            }
        });

    },

    districtProposalVoteExtraAction:function (districtProposalRSDTO) {
        console.log(districtProposalRSDTO)
        var $buttonColumnC = $("#aside-ppal .call-to-action .actions a")
        var $districtProposalCounter = $(".comment-counter-"+districtProposalRSDTO.id)
        if (districtProposalRSDTO.voted){
            $districtProposalCounter.find("a").addClass("active");
            $districtProposalCounter.find(".fa-shopping-cart").addClass("fas");
            $districtProposalCounter.find(".fa-shopping-cart").removeClass("fal");
            $buttonColumnC.addClass('on')
            $buttonColumnC.blur()
        }else{
            $districtProposalCounter.find("a").removeClass("active");
            $districtProposalCounter.find(".fa-shopping-cart").removeClass("fas");
            $districtProposalCounter.find(".fa-shopping-cart").addClass("fal");
            $buttonColumnC.removeClass('on')
            $buttonColumnC.blur()
        }
        $districtProposalCounter.find(".number").html(districtProposalRSDTO.numVotes);
        var width = Math.round(districtProposalRSDTO.district.amountUserInvested / districtProposalRSDTO.district.budget * 100 )  ;
        $(".budget .progress-bar-custom .progress-bar-custom-done").animate({width:width+'%'},"slow")
        $(".budget .campaign-progress-bar").attr("data-width",width)
        $("#participatoryBudget-districtProposals-list #proposal-district-"+districtProposalRSDTO.district.id+" .progress-bar-custom .progress-bar-custom-done").animate({width:width+'%'},"slow")
        $("#participatoryBudget-districtProposals-list #proposal-district-"+districtProposalRSDTO.district.id+" .campaign-progress-bar").attr("data-width",width)
        $(".budget .campaign-progress-bar .pop-up .amount-user-invested").html(districtProposalRSDTO.district.amountUserInvested);
        $("#participatoryBudget-districtProposals-list #proposal-district-"+districtProposalRSDTO.district.id+" .pop-up .number").html(districtProposalRSDTO.district.amountUserInvested);
    },

    districtProposalSupportExtraAction:function (districtProposalRSDTO) {
        // console.log(districtProposalRSDTO)
        var $buttonColumnC = $("#aside-ppal .call-to-action .actions a")
        var $districtProposalCounter = $(".comment-counter-"+districtProposalRSDTO.id)
        if (districtProposalRSDTO.supported){
            $districtProposalCounter.find("a").addClass("active");
            $districtProposalCounter.find(".fa-rocket").addClass("fas");
            $districtProposalCounter.find(".fa-rocket").removeClass("fal");
            $buttonColumnC.addClass('on')
            $buttonColumnC.blur()
        }else{
            $districtProposalCounter.find("a").removeClass("active");
            $districtProposalCounter.find(".fa-rocket").removeClass("fas");
            $districtProposalCounter.find(".fa-rocket").addClass("fal");
            $buttonColumnC.removeClass('on')
            $buttonColumnC.blur()
        }
        $districtProposalCounter.find(".number").html(districtProposalRSDTO.numSupports);
    }

}