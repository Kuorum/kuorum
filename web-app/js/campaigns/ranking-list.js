$(function () {

    if ($('input#searchRankingCampaign').length) {
        $('input#searchRankingCampaign').on('input', rankingHelper.searchAndMarc);
    }

    // FILTRADO Y BUSCADOR LISTADO CAMPAÑAS
    if ($('#rankingListCampaigns').length) {


        $('#search-form-campaign').submit(function () {
            //The search campaign form has not a submit action. All the search is done with javascript
            return false;
        });

        // clase active botones ordenar listado
        $('body').on('click', '.sort', function (e) {
            if (!$(this).hasClass('active')) {
                $('.sort').removeClass('active');
                $(this).addClass('active');
            }
        });

        //select filtro campañas según estado
        $('.rankingFilter').on('change', function () {
            rankingHelper.applyFiltersToList()
        });
        $("#campaignsOrderOptions").on("click", "a", function (e) {
            e.preventDefault();
        })
    }
    // Sorting campaign by default when load campaigns
    if ($("#campaignsOrderOptions ul li:first a").length > 0) {
        $("#campaignsOrderOptions ul li:first a")[0].click();
    }

    rankingHelper.loadData();

})

var rankingHelper = {
    rankingCampaignList: undefined,
    options: undefined,
    timeoutInSeconds: 5,
    reloadDataTimer: undefined,
    sortInfo: {
        a: undefined,
        order: undefined,
        activePage: undefined,
        itemPerPage: 100
    },

    init: function (data) {
        //plugin options
        if (rankingHelper.rankingCampaignList == undefined) {
            var paginationTopOptions = {
                name: "paginationTop",
                paginationClass: "paginationTop",
                innerWindow: 1,
                outerWindow: 1
            };
            var paginationBottomOptions = {
                name: "paginationBottom",
                paginationClass: "paginationBottom",
                innerWindow: 2,
                outerWindow: 1
            };

            rankingHelper.options = {
                valueNames: ['id', 'ranking-campaign-title', 'ranking-campaign-author', 'ranking-campaign-author', 'ranking-cause', 'ranking-pos', 'ranking-contest-focusType', 'ranking-contest-activityType', 'ranking-numVotes'],
                page: rankingHelper.sortInfo.itemPerPage,
                searchClass: "searchRankingCampaign",
                plugins: [
                    ListPagination(paginationTopOptions),
                    ListPagination(paginationBottomOptions)
                ],
                pagination: true
            };
            $("#rankingList").html(data);
            rankingHelper.rankingCampaignList = new List('rankingListCampaigns', rankingHelper.options);
            rankingHelper._savePageStatus();
        } else {
            rankingHelper._savePageStatus();
            rankingHelper.rankingCampaignList.clear();
            $("#rankingList").html(data);
            rankingHelper.rankingCampaignList.reIndex();
        }
        rankingHelper.applyFiltersToList();
        rankingHelper._recoverPageStatus();
        $('.totalList').text(rankingHelper.rankingCampaignList.matchingItems.length);
    },

    reloadAfterTimeOut: function () {
        const currentTime = parseInt($(".reloading-counter").html());
        var newTime = currentTime - 1;
        if (newTime == 0) {
            rankingHelper.loadData();
            rankingHelper.stopTimer();
        }
        $(".reloading-counter").html(newTime <= 0 ? 0 : newTime)
    },

    restarConuter: function () {
        $(".reloading-counter").html(rankingHelper.timeoutInSeconds)
    },

    loadData: function () {
        const $rankingList = $("#rankingList");
        const loadDataUrl = $rankingList.attr("data-ajaxLoadContestApplications");
        // pageLoadingOn();
        $.get(loadDataUrl, function (data) {
            rankingHelper.init(data);
            rankingHelper.startTimer();
            rankingHelper.restarConuter();
            // pageLoadingOff();
        });
    },

    startTimer: function () {
        if (rankingHelper.timeoutInSeconds > 0) {
            rankingHelper.reloadDataTimer = setInterval(rankingHelper.reloadAfterTimeOut, 1000); // Each second
        }
    },
    stopTimer: function () {
        clearTimeout(rankingHelper.reloadDataTimer);
    },

    _savePageStatus: function () {
        const $sort = $(".sort-options .sort.active")
        rankingHelper.sortInfo.a = $(".sort-options .sort.active")
        rankingHelper.sortInfo.order = rankingHelper.sortInfo.a.hasClass("desc") ? "desc" : "asc"
        rankingHelper.sortInfo.activePage = $(".paginationTop li.active a").html()
    },

    _recoverPageStatus: function () {
        if (rankingHelper.sortInfo.a != undefined) {
            $(".sort-options .sort.active").removeClass("active")
            rankingHelper.sortInfo.a.addClass(rankingHelper.sortInfo.order)
            rankingHelper.sortInfo.a.addClass("active")
            const field = rankingHelper.sortInfo.a.attr("data-sort")
            rankingHelper.rankingCampaignList.sort(field, {order: rankingHelper.sortInfo.order})
            rankingHelper.rankingCampaignList.show(rankingHelper.sortInfo.activePage, rankingHelper.sortInfo.itemPerPage)
        }
    },

    applyFiltersToList: function () {
        function isFiltered(item, select) {
            var value = $(select).val();
            var field = $(select).attr("data-filter");
            return value == 'all' || (item.values()[field].indexOf(value) >= 0);
        }

        rankingHelper.rankingCampaignList.filter(function (item) {
            // Iterate over all select and apply filter -> it is an AND
            return $.inArray(false, $(".rankingFilter").map(function (idx, select) {
                return isFiltered(item, select);
            })) < 0;
        });
        rankingHelper.searchAndMarc();
        $('.totalList').text(rankingHelper.rankingCampaignList.visibleItems.length);
    },

    searchAndMarc: function () {
        var keyword = $('input#searchRankingCampaign').val();
        rankingHelper.rankingCampaignList.search(keyword);
        $('#rankingListCampaigns').unmark().mark(keyword, rankingHelper.options);
        $('.totalList').text(rankingHelper.rankingCampaignList.visibleItems.length);
    }
}
