$(function () {

    $("#contest-applications-list .nav-underline a").on("click", function (e) {
        e.preventDefault();
        var $a = $(this);
        var listSelector = $a.attr("data-listSelector");
        contestApplicationHelper.showListApplications(listSelector);
    })

    $("#contest-applications-list ul.search-list").on("click", "a.loadMore", function (e) {
        e.preventDefault();
        var $a = $(this);
        var $ul = $a.parents("ul.search-list")
        contestApplicationHelper.loadMoreApplications($ul)
    });

    contestApplicationHelper.showListApplications();
});

var contestApplicationHelper = {
    containerId: "contest-applications-list",
    showListApplications: function (listSelector = "random") {
        $("#" + contestApplicationHelper.containerId).find(".search-list").hide();
        var $activeList = $("#" + contestApplicationHelper.containerId).find(".search-list." + listSelector);
        $activeList.show();
        contestApplicationHelper.loadMoreApplications($activeList)
    },

    loadMoreApplications: function ($ulIdSelector) {
        $ulIdSelector.find("li.load-more-contest-application").remove();
        var urlLoadMoreDistrictProposals = $ulIdSelector.attr("data-loadProposals");
        var params = {
            page: $ulIdSelector.attr("data-page")
        };
        var direction = $ulIdSelector.attr("data-direction");
        if (typeof direction !== typeof undefined && direction !== false) {
            params['direction'] = direction
        }
        var randomSeed = $ulIdSelector.attr("data-randomSeed");
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
                if (moreResults) {
                    $ulIdSelector.append("" +
                        "<li class='col-xs-12 center load-more-contest-application load-more'> " +
                        "<a href='#' class='loadMore' >" + i18n.seeMore + " " +
                        "<span class='fal fa-angle-down'></span>" +
                        "</a>" +
                        "</li>")
                }
                $ulIdSelector.attr("data-page", Number.parseInt(params.page) + 1);
                prepareYoutubeVideosClick();
            })
            .fail(function (messageError) {
                display.warn("Error");
            })
            .always(function () {
                // pageLoadingOff("Load more districts");
                $ulIdSelector.find(".loading").remove()
                // $liLoading.remove()
            });
    },
}