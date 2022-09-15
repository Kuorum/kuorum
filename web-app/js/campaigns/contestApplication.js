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
        const $button = $(this);

        if ($button.attr("disabled") == undefined) {
            const params = {
                callback: undefined,
                $button: $button
            };

            if (contestApplicationFunctions.outOfTime($button.attr("data-deadLineVotesTimeStamp"))) {
                display.error($button.attr("data-deadLineVotesErrorMsg"))
            } else {
                const executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(contestApplicationFunctions.onClickVoteContestApplicationWithParams, params);

                userValidatedByDomain.handleLoginAndValidationUser(
                    $button,
                    "contestApplicationVoteNoLogged",
                    executableFunction)
            }
        }
        // ELSE means that the user has already voted
    },
    nowUTC: function () {
        const now = new Date();
        return now.getTime() + now.getTimezoneOffset() * 60000
    }, outOfTime: function (deadLineVotes) {
        const deadLineMillisecondsUTC = parseInt(deadLineVotes)
        return deadLineMillisecondsUTC < this.nowUTC()
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
        var callback = params.callback;
        var $button = params.$button;
        var url = $button.attr('href');

        var data = {};
        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function (contestApplicationVote) {
                if (contestApplicationVote.success) {
                    console.log("Vote Success")
                    console.log(contestApplicationVote)
                    var $buttonMainPage = $("section#main .leader-post .contestApplication-vote")
                    var $buttonColumnC = $("#aside-ppal .call-to-action .actions a")
                    var $liStats = $(".leader-post-stats .fa-scroll").parents("li")

                    contestApplicationFunctions._disableButton($button, contestApplicationVote.vote.votes);
                    contestApplicationFunctions._disableButton($buttonMainPage, contestApplicationVote.vote.votes);
                    contestApplicationFunctions._disableButton($buttonColumnC, contestApplicationVote.vote.votes);
                    contestApplicationFunctions._disableButton($liStats, contestApplicationVote.vote.votes);
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

    _disableButton: function ($button, numVotes) {
        $button.attr("disabled", "disabled")
        $button.addClass("active")
        $button.addClass("disabled")
        $button.find(".fa-scroll").removeClass("fal")
        $button.find(".fa-scroll").addClass("fas")
        $button.find(".number").html(numVotes)
    }
};
