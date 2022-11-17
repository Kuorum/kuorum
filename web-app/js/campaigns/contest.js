$(function () {

    $(".campaign-steps-status .campaign-steps-info a").on("click", function (e) {
        e.preventDefault();
        var status = $(this).attr("data-status");
        var statusText = $(this).attr("data-status-text");
        var link = $("#modalEditParticipatoryBudgetStatusButtonOk").attr("href");

        link = link.replace(/status=.*/, "status=" + status);
        $("#modalEditParticipatoryBudgetStatusButtonOk").attr("href", link);
        $("#changeContestApplicationStatusModal .modal-body p strong").html(statusText);


        $("#changeContestApplicationStatusModal").modal("show")
    });

    $("#contest-applications-list .nav-underline a").on("click", function (e) {
        e.preventDefault();
        const $a = $(this);
        console.log($a)
        if(!$a.parent().hasClass("active")) {
            const listSelector = $a.attr("data-listSelector");

            setActiveAndRefreshList($a, listSelector);

            contestApplicationHelper.showListApplications(listSelector);
        }
    })

    function setActiveAndRefreshList($a, listSelector) {
        $a.parents("ul.nav").find("li").removeClass("active")
        $a.parent().addClass("active")

        const $ul = $("#" + contestApplicationHelper.containerId).find(".search-list." + listSelector);
        $ul.empty();
        $ul.removeAttr("noMoreResults");
        $ul.attr("data-page", 0);
        $ul.attr("loading", false);
    }

    $("#contest-applications-list ul.search-list").on("click", "a.loadMore", function (e) {
        e.preventDefault();
        var $a = $(this);
        var $ul = $a.parents("ul.search-list")
        contestApplicationHelper.loadMoreApplications($ul)
    });

    contestApplicationHelper.showListApplications();

    noLoggedCallbacks['contestAddApplicationAction'] = function () {
        var buttonId = $('#registro').find("form").attr("data-buttonId");
        var $button = $("#" + buttonId);
        var link = $button.attr("href");
        window.location = link;
    };

    $(window).scroll(function () {
        if (($(window).scrollTop() + $(window).height()) > 0.9 * $(document).height()) {
            $activeListLink = $("#" + contestApplicationHelper.containerId + " > .nav > li.active a")
            var listSelector = $activeListLink.attr("data-listselector");
            var $ul = $("#" + contestApplicationHelper.containerId + " > ul." + listSelector)
            contestApplicationHelper.loadMoreApplications($ul)
        }
    });

    $(".call-to-action").on("click", "a.btn.ADDING_APPLICATIONS", contestApplicationHelper.bindActionClickAddDistrictProposal);

    $(".actions.call-to-action-mobile.go-to-action button").on("click", function(e){
        $("#aside-ppal > .call-to-action:first-child > .actions > a.btn").click()
    });

    $("#changeContestApplicationStatusModal").on("click", "#modalEditParticipatoryBudgetStatusButtonOk", function (e) {
        console.log("Change contest status js event");
        e.preventDefault()
        pageLoadingOn();

        const data = {}
        $.post($(this).attr("href"), data)
            .done(function (response) {
                if (response.success) {
                    window.location.reload();
                } else {
                    display.error("Error updating contest status")
                }
            })
            .fail(function (messageError) {
                display.error("Error updating contest status")
            })
            .always(function () {
                pageLoadingOff();
                $("#changeContestApplicationStatusModal").modal("hide");
            });
    });
});

var contestApplicationHelper = {
    containerId: "contest-applications-list",
    showListApplications: function (listSelector) {
        if (listSelector == undefined) {
            listSelector = $("#contest-applications-list").attr("contest-applications-list") == "VOTING" ? "random" : "vote";
            console.log("Defautl list; " + listSelector)
        }
        $("#" + contestApplicationHelper.containerId).find(".search-list").hide();
        var $activeList = $("#" + contestApplicationHelper.containerId).find(".search-list." + listSelector);
        $activeList.show();
        contestApplicationHelper.loadMoreApplications($activeList)
    },

    loadMoreApplications: function ($ulIdSelector) {
        $ulIdSelector.find("li.load-more-contest-application").remove();
        var noMoreResults = $ulIdSelector.attr("noMoreResults");
        var loading = $ulIdSelector.attr("loading");
        // console.dir({loading:loading, noMoreResults: noMoreResults, isUndefined:!noMoreResults, isLoading:loading === undefined || loading=="false", total:noMoreResults != undefined && (loading === undefined || loading=="true")})
        if (!noMoreResults && (loading === undefined || loading == "false")) {
            console.log("Loading more contests applications");
            $ulIdSelector.attr("loading", "true"); // Prevents double click
            var urlLoadMoreDistrictProposals = $ulIdSelector.attr("data-loadProposals");
            var params = {
                page: $ulIdSelector.attr("data-page")
            };
            var direction = $ulIdSelector.attr("data-direction");
            if (typeof direction !== typeof undefined && direction !== false) {
                params['direction'] = direction
            }
            var randomSeed = contestApplicationHelper._getRandomSeedFromContainer($ulIdSelector);
            if (typeof randomSeed !== typeof undefined && randomSeed !== false) {
                params['randomSeed'] = randomSeed
            }
            var appender = "&"
            if (urlLoadMoreDistrictProposals.indexOf("?") < 0) {
                appender = "?";
            }
            urlLoadMoreDistrictProposals = urlLoadMoreDistrictProposals + appender + $.param(params)

            $ulIdSelector.append("<li class='loading'></li>");
            $ulIdSelector.show();
            $.get(urlLoadMoreDistrictProposals)
                .done(function (data, staus, xhr) {
                    var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')); //Para que sea un bool)
                    $ulIdSelector.append(data);
                    if (!moreResults) {
                        // $ulIdSelector.append("" +
                        //     "<li class='col-xs-12 center load-more-contest-application load-more'> " +
                        //     "<a href='#' class='loadMore' >" + i18n.seeMore + " " +
                        //     "<span class='fal fa-angle-down'></span>" +
                        //     "</a>" +
                        //     "</li>")
                        $ulIdSelector.attr("noMoreResults", "true");
                    }
                    $ulIdSelector.attr("data-page", Number.parseInt(params.page) + 1);
                    prepareYoutubeVideosClick();
                    $ulIdSelector.attr("loading", "false");
                })
                .fail(function (messageError) {
                    display.warn("Error");
                })
                .always(function () {
                    // pageLoadingOff("Load more districts");
                    $ulIdSelector.find(".loading").remove()
                    // $liLoading.remove()
                });
        } else {
            // NO MORE RESULTS
        }
    },
    _getRandomSeedFromContainer: function ($ulIdSelector) {
        if ($ulIdSelector.hasClass("random")) {
            var randomSeed = $ulIdSelector.attr("data-randomseed")
            if (randomSeed == undefined || randomSeed == "") {
                randomSeed = Math.random();
                $ulIdSelector.attr("data-randomseed", randomSeed)
            }
            return randomSeed;
        } else {
            return undefined;
        }
    },

    bindActionClickAddDistrictProposal: function (e) {
        var $button = $(this);
        var loggedUser = $button.attr("data-loggedUser");
        if (loggedUser == undefined || loggedUser == "") {
            e.preventDefault();
            e.stopPropagation();
            event.stopPropagation();
            console.log("NO LOGGED")
            // NO LOGGED
            var buttonId = guid();
            $button.attr("id", buttonId);
            $('#registro').find("form").attr("callback", "contestAddApplicationAction");
            $('#registro').find("form").attr("data-buttonId", buttonId);
            $('#registro').modal('show');
        } else {
            //Hidden button isn't doing default behaviour
            window.location = $button.attr("href");
        }
    }
}