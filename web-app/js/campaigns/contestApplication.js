$(function () {


    $("body").on("click", '.' + contestApplicationFunctions.BUTTON_DATA.BUTTON_CLASS, contestApplicationFunctions.bindVoteClick);
    $("body").on("click", '.modal.confirm-vote-contest-application .confirm-vote-contest-application', contestApplicationFunctions.warnBeforeVote.acceptWarnBeforeVote);
    noLoggedCallbacks['contestApplicationVoteNoLogged'] = function () {
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#" + buttonId);
        pageLoadingOff();
        console.log("Contest application voting NO LOGGED :: RELOADING")
        contestApplicationFunctions.onClickVoteContestApplication($button, noLoggedCallbacks.reloadPage);
    };
});

var contestApplicationFunctions = {

    BUTTON_DATA: {
        ACCEPTED_CONDITIONS: "data-acceptedConditions",
        REMAINING_VOTES: "data-contestRemainingVotes",
        CAMPAIGN_ID: "data-campaignId",
        BUTTON_CLASS: 'contestApplication-vote'
    },

    warnBeforeVote: {
        showWarnBeforeVote: function ($button) {
            const campaignId = $button.attr(contestApplicationFunctions.BUTTON_DATA.CAMPAIGN_ID)
            const $modal = $("#confirm-vote-contest-application-" + campaignId);
            $modal.modal("show")
            const $modalButton = $modal.find(".confirm-vote-contest-application")
            const voteButtonId = guid()
            $button.attr("id", voteButtonId)
            $modalButton.attr("voteButtonId", voteButtonId)
        },
        acceptWarnBeforeVote: function (e) {
            e.preventDefault();
            const $modalButton = $(this)
            const $modal = $modalButton.parents(".confirm-vote-contest-application");
            $modal.modal("hide")
            const $button = $("#" + $modalButton.attr("voteButtonId"))
            contestApplicationFunctions.warnBeforeVote.toggleShowWarn($button, true);
            $button.click()
        },
        isActiveWarn: function ($button) {
            const remaining = parseInt($button.attr(contestApplicationFunctions.BUTTON_DATA.REMAINING_VOTES));
            return remaining > 0 && $button.attr(contestApplicationFunctions.BUTTON_DATA.ACCEPTED_CONDITIONS) != "true"
        },
        toggleShowWarn: function ($button, active) {
            $button.attr(contestApplicationFunctions.BUTTON_DATA.ACCEPTED_CONDITIONS, active)
        }
    },
    bindVoteClick: function (event) {
        event.preventDefault();
        event.stopPropagation();
        const $button = $(this);
        if ($button.attr("btn-disabled") === "false") {
            if (contestApplicationFunctions._outOfTime($button.attr("data-deadLineVotesTimeStamp"))) {
                display.error($button.attr("data-deadLineVotesErrorMsg"))
            } else if (contestApplicationFunctions.warnBeforeVote.isActiveWarn($button)) {
                contestApplicationFunctions.warnBeforeVote.showWarnBeforeVote($button);
            } else {
                contestApplicationFunctions.warnBeforeVote.toggleShowWarn($button, false);
                const params = {
                    callback: undefined,
                    $button: $button
                };
                const executableFunction = new userValidatedByDomain.ExcutableFunctionCallback(contestApplicationFunctions.onClickVoteContestApplicationWithParams, params);

                userValidatedByDomain.handleLoginAndValidationUser(
                    $button,
                    "contestApplicationVoteNoLogged",
                    executableFunction)
            }
        }
        // ELSE means that the user has already voted
    },
    _nowUTC: function () {
        const now = new Date();
        return now.getTime() + now.getTimezoneOffset() * 60000
    },
    _outOfTime: function (deadLineVotes) {
        const deadLineMillisecondsUTC = parseInt(deadLineVotes)
        return deadLineMillisecondsUTC < contestApplicationFunctions._nowUTC()
    },
    _isLogged: function ($button) {
        return $button.attr("data-loggedUser") !== '';
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
                    contestApplicationFunctions._handleButtonsAtSuccess(contestApplicationVote, $button);
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
        const $buttonMain = $("section#main .leader-post .contestApplication-vote");
        const $buttonContestApplicationList = $("#contest-applications-list  .contestApplication-vote-" + contestApplicationVote.vote.contestApplicationId);
        const $buttonColumnCallToAction = $("#aside-ppal .call-to-action .actions .contestApplication-vote a");
        const $badgeNumVotesStats = $(".leader-post-stats .fa-scroll").parents("li");

        const buttons = [$buttonMain, $buttonContestApplicationList, $buttonColumnCallToAction]
        const stats = [$buttonMain,$buttonContestApplicationList, $badgeNumVotesStats]

        const numVotes = contestApplicationVote.vote.votes;
        if (contestApplicationFunctions._isLogged($button)) {
            buttons.forEach(function (item) {
                contestApplicationFunctions._disableVoteButton(item, numVotes)
            });
            $('.' + contestApplicationFunctions.BUTTON_DATA.BUTTON_CLASS).attr(contestApplicationFunctions.BUTTON_DATA.REMAINING_VOTES, 0)
        }
        stats.forEach(function (item){contestApplicationFunctions._updateNumVotes(item,numVotes)});

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