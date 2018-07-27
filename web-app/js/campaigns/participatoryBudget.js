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
        var districtId = $(this).attr("data-districtId");
        $("#participatoryBudget-districtProposals-list ul").hide()
        var ulId = "#proposal-district-"+districtId
        if ($(ulId+" li").length <= 0){
            participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        }else{
            $(ulId).show()
        }

    })

    $("#participatoryBudget-districtProposals-list").on("click",'.load-more-district-proposals', function(e){
        e.preventDefault();
        var ulLi = $(this).parent().attr("id")
        participatoryBudgetHelper.loadMoreDistrictProposals(ulId)
        $(this).remove(); // Removes the button. The loadMoreDistrictProposals creates new button
    })


});

var participatoryBudgetHelper={

    loadMoreDistrictProposals: function(ulId){
        pageLoadingOn("Load more districts")
        var urlLoadMoreDistrictProposals = $(ulId).attr("data-loadProposals")
        $.get( urlLoadMoreDistrictProposals)
            .done(function(data, staus, xhr) {
                var moreResults = $.parseJSON(xhr.getResponseHeader('moreResults')); //Para que sea un bool)
                $(ulId).append(data)
                if (moreResults){
                    $(ulId).append("<li class='col-xs-12 center load-more-district-proposals'><a href='#' class='btn btn-grey-light'>Load more</a> </li>")
                }
                $(ulId).show()
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff("Load more districts");
            });
    }
}