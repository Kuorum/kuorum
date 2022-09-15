$(function () {


    $("body").on("click", '.contestApplication-vote', contestApplicationFunctions.bindVoteClick);
    noLoggedCallbacks['contestApplicationVoteNoLogged'] = function () {
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#" + buttonId);
        pageLoadingOff();
        console.log("Contest application voting NO LOGGED :: RELOADING")
        contestApplicationFunctions.onClickVoteContestApplication($button, noLoggedCallbacks.reloadPage);
    };
});


var contestApplicationFunctions = {
    bindVoteClick: function (event) {
        event.preventDefault();
        event.stopPropagation();
        var $button = $(this);

        if ($button.attr("btn-disabled") === "false") {
            var params = {
                callback: undefined,
                $button: $button
            };
            var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(contestApplicationFunctions.onClickVoteContestApplicationWithParams, params);

            userValidatedByDomain.handleLoginAndValidationUser(
                $button,
                "contestApplicationVoteNoLogged",
                executableFunction)
        }
        // ELSE means that the user has already voted
    },
    onClickVoteContestApplicationWithParams: function (params) {
        contestApplicationFunctions.onClickVoteContestApplication(params.$button, params.callback)
    },
    onClickVoteContestApplication: function ($button, callback) {
        if (isPageLoading()) {
            return;
        }
        pageLoadingOn();
        var params = {
            callback: callback,
            $button: $button
        };
        var executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(contestApplicationFunctions._executableVoteContestApplication, params);
        userValidatedByDomain.executeClickButtonHandlingValidations($button, executableFunction);
    },

    _executableVoteContestApplication: function (params) {
        const $button = params.$button;
        const url = $button.attr('href');
        const data = {};

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function (contestApplicationVote) {
                if (contestApplicationVote.success) {
                    this._handleButtonsAtSuccess(contestApplicationVote, $button);
                    display.success(contestApplicationVote.message)
                } else {
                    display.error(contestApplicationVote.message)
                }
            },
            error: function () {
                display.error("Error saving vote")
            },
            complete: function () {
                pageLoadingOff();
            }
        });
    },

    _handleButtonsAtSuccess: function (contestApplicationVote, $button) {
        const $buttonMainPage = $("section#main .leader-post .contestApplication-vote")
        const $buttonColumnC = $("#aside-ppal .call-to-action .actions .contestApplication-vote a")
        const $buttonContestApplicationList = $("#contest-applications-list  .contestApplication-vote-" + contestApplicationVote.vote.contestApplicationId)
        const $liStats = $(".leader-post-stats .fa-scroll").parents("li")

        if ($button.attr("data-loggedUser") !== '') {
            contestApplicationFunctions._disableVoteButton($buttonMainPage, contestApplicationVote.vote.votes);
            contestApplicationFunctions._disableVoteButton($buttonColumnC, contestApplicationVote.vote.votes);
            contestApplicationFunctions._disableVoteButton($buttonContestApplicationList, contestApplicationVote.vote.votes);
        }
        contestApplicationFunctions._updateNumVotes($buttonMainPage, contestApplicationVote.vote.votes);
        contestApplicationFunctions._updateNumVotes($liStats, contestApplicationVote.vote.votes);
        contestApplicationFunctions._updateNumVotes($buttonContestApplicationList, contestApplicationVote.vote.votes);
    },

    _disableVoteButton: function ($button, numVotes) {
        $button.attr("btn-disabled", "true")
        $button.addClass("active")
        $button.addClass("disabled")
        $button.find(".fa-scroll").removeClass("fal")
        $button.find(".fa-scroll").addClass("fas")

        if ($button.parent().hasClass("actions")) {
            $button.html($button.attr("data-disabledText"))
        }
    },

    _updateNumVotes: function ($button, numVotes) {
        $button.find(".number").html(numVotes)
    }
};