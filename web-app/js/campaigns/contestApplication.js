$(function () {


    $("body").on("click", '.contestApplication-vote', contestApplicationFunctions.bindSignClick);
    noLoggedCallbacks['contestApplicationVoteNoLogged'] = function () {
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#" + buttonId);
        pageLoadingOff();
        contestApplicationFunctions.onClickVoteContestApplication($button, noLoggedCallbacks.reloadPage);
    };
});


var contestApplicationFunctions = {
    bindSignClick: function (event) {
        event.preventDefault();
        event.stopPropagation();
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == "") {
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "contestApplicationVoteNoLogged");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        } else {
            contestApplicationFunctions.onClickVoteContestApplication($button);
        }
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
                console.log("VOTE")
                console.log(contestApplicationVote)
                if (contestApplicationVote.success) {
                    $button.attr("disabled", "disabled")
                    $button.html($button.attr("data-disabledText"))
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

    _loadPDFIframe: function ($modal, viewPdfUrl) {
        var iframe = $modal.find("iframe");
        var $loading = $modal.find(".loading");
        $loading.show()
        iframe.attr("src", viewPdfUrl);
        iframe.hide();
        var reloadUntilCorrectPdf = function (e) {
            var title = iframe.contents().find("title").html();
            console.log("Reloading until pdf generated. Title = " + title)
            if (title == "File not found") { // Title checking
                console.log("Reloading IFRAME")
                iframe.attr('src', function (i, val) {
                    return val;
                }); // resets iframe
                iframe.attr('data', function (i, val) {
                    return val;
                }); // resets iframe
            } else {
                console.log("PDF Loaded")
                contestApplicationFunctions._showIframeWithPdf($modal);
            }
        }
        iframe.load(reloadUntilCorrectPdf);
    },
    _showIframeWithPdf: function ($modal) {
        var $iframe = $modal.find("iframe");
        var $loading = $modal.find(".loading");
        $iframe.show();
        $loading.hide();
    }
};