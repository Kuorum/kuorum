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
        console.log("onClickPostLike")
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
        var vote = !$button.hasClass("on")
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
                $button.toggleClass('on')
                // $button.find('.fa').toggleClass("fa-heart-o fa-heart");
                // $button.find('.number').text(postRSDTO.likes);
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

    }

}