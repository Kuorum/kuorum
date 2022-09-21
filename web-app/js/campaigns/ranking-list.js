$(function () {

    if ($('input#searchRankingCampaign').length) {
        var mark = function () {
            var keyword = $('input#searchRankingCampaign').val();
            $('#rankingListCampaigns').unmark().mark(keyword, rankingHelper.options);
        };
        $('input#searchRankingCampaign').on('input', mark);
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


        rankingHelper.init();

        //select filtro campañas según estado
        $('.rankingFilter').on('change', function () {
            var selection = this.value;
            var selectionFilterField = $(this).attr("data-filter");
            if (selection == 'all') {
                rankingHelper.rankingCampaignList.filter();
            } else {
                // filter items in the list
                rankingHelper.rankingCampaignList.filter(function (item) {
                    return (item.values()[selectionFilterField].indexOf(selection) >= 0);
                });
            }
            $('.totalList').text(rankingHelper.rankingCampaignList.matchingItems.length);
        });
        $("#campaignsOrderOptions").on("click", "a", function (e) {
            e.preventDefault();
        })
    }
    // Sorting campaign by default when load campaigns
    if ($("#campaignsOrderOptions ul li:first a").length > 0) {
        $("#campaignsOrderOptions ul li:first a")[0].click();
    }
    $('.totalList').text(rankingHelper.rankingCampaignList.matchingItems.length);

    setInterval(rankingHelper.reloadAfterTimeOut, 1000); // Each second

})

var rankingHelper = {
    rankingCampaignList: undefined,
    options: undefined,
    timeoutInSeconds: 5,
    init: function () {
        //plugin options
        var paginationTopOptions = {
            name: "paginationTop",
            paginationClass: "paginationTop",
            innerWindow: 1,
            outerWindow: 1
        };
        var paginationBottomOptions = {
            name: "paginationBottom",
            paginationClass: "paginationBottom",
            innerWindow: 1,
            outerWindow: 1
        };

        rankingHelper.options = {
            valueNames: ['id', 'ranking-campaign-title', 'ranking-campaign-author', 'ranking-campaign-author', 'ranking-cause', 'ranking-pos', 'ranking-contest-focusType', 'ranking-contest-activityType', 'ranking-numVotes'],
            page: 10,
            searchClass: "searchRankingCampaign",
            plugins: [
                ListPagination(paginationTopOptions),
                ListPagination(paginationBottomOptions)
            ],
            pagination: true
        };
        rankingHelper.rankingCampaignList = new List('rankingListCampaigns', rankingHelper.options);
    },

    reloadAfterTimeOut: function () {
        const currentTime = parseInt($(".reloading-counter").html());
        var newTime = currentTime - 1;
        if (newTime == 0) {
            console.log("Relaod DATA")
            newTime = rankingHelper.timeoutInSeconds;
            newTime = 0;
            pageLoadingOn()
            location.reload();
            // $("#rankingList li").remove()
            // rankingHelper.init()
        }
        $(".reloading-counter").html(newTime)
    }
}
