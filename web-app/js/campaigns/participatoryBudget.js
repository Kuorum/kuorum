$(function () {
    
    $(".participatory-budget-status .participatory-budget-steps-info a").on("click",function (e) {
        e.preventDefault();
        var status=$(this).attr("data-status")
        var statusText=$(this).attr("data-status-text")

        var link = $("#modalEditParticipatoryBudgetStatusButtonOk").attr("href")
        link = link.replace(/status=.*/, "status=" + status);
        $("#modalEditParticipatoryBudgetStatusButtonOk").attr("href", link)


        $("#modalEditParticipatoryBudgetStatus .modal-body p strong").html(statusText)
        $("#modalEditParticipatoryBudgetStatus").modal("show")

    });

    $("#participatoryBudget-districtProposals-list-tab a").on("click", function(e){
        var $a = $(this)
        $a.parent().parent().children().removeClass("active")
        $a.parent().addClass("active")
        var districtId = $a.attr("data-districtId");
        $("#participatoryBudget-districtProposals-list > div").hide()
        var divId = "#proposal-district-"+districtId
        var ulId;
        if ($(divId + "ul.nav-pills-lvl2 > li > a.active").length > 0){
            var selector = $(divId + "ul.nav-pills-lvl2 > li > a.active").attr("data-listSelector")
            ulId = divId +"> ul.search-list."+selector;
        }else{
            ulId = divId+" > ul.search-list.random"
        }
        if ($(ulId+ " > li").length <= 0){
            participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        }else{
            $(divId).show()
        }

    })

    $("#participatoryBudget-districtProposals-list ul.nav-pills-lvl2 > li > a").on("click", function(e){
        e.preventDefault();
        var $a = $(this)
        $a.parent().parent().children().removeClass("active")
        $a.parent().addClass("active");
        var districtId = $a.attr("data-districtId");
        var selector = $a.attr("data-listSelector")
        var divId = "#proposal-district-"+districtId
        var ulId = divId +"> ul.search-list."+selector;
        var params = {
            direction:'ASC'
        }
        console.log($a)
        if ($a.find(".fal").length>0){
            var $spanDirection = $a.find(".fal");
            if ($spanDirection.hasClass("fa-angle-down")){
                $spanDirection.removeClass("fa-angle-down")
                $spanDirection.addClass("fa-angle-up")
                params.direction='ASC';
                $(ulId).empty();
            }else{
                $spanDirection.removeClass("fa-angle-up")
                $spanDirection.addClass("fa-angle-down")
                params.direction='DESC';
                $(ulId).empty();
            }
        }

        $(divId +"> ul.search-list").hide()
        $(ulId).show()
        if ($(ulId+ " > li").length <= 0){
            participatoryBudgetHelper.loadMoreDistrictProposals(ulId, params)
        }

    })

    $("#participatoryBudget-districtProposals-list").on("click",'.load-more-district-proposals', function(e){
        e.preventDefault();
        var ulLi = $(this).parent().attr("id")
        participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        $(this).remove(); // Removes the button. The loadMoreDistrictProposals creates new button
    })

    $("#participatoryBudget-districtProposals-list-tab > li:first a").click()

});

var participatoryBudgetHelper={

    loadMoreDistrictProposals: function(ulId, params){
        // pageLoadingOn("Load more districts")
        var urlLoadMoreDistrictProposals = new URL($(ulId).attr("data-loadProposals"))
        for (var key in params) {
            urlLoadMoreDistrictProposals.searchParams.append(key, params[key])
        }

        $(ulId).append("<li class='loading'></li>")
        $(ulId).parent().show()
        $.get( urlLoadMoreDistrictProposals)
            .done(function(data, staus, xhr) {
                var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')); //Para que sea un bool)
                $(ulId).append(data)
                if (moreResults){
                    $(ulId).append("<li class='col-xs-12 center load-more-district-proposals'><a href='#' class='btn btn-grey-light'>Load more</a> </li>")
                }
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                // pageLoadingOff("Load more districts");
                $(ulId).find(".loading").remove()
                // $liLoading.remove()
            });
    }
}