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


});

var participatoryBudgetHelper={

    loadMoreDistrictProposals: function(ulId){
        var urlLoadMoreDistrictProposals = $(ulId).attr("data-loadProposals")
        $.get( urlLoadMoreDistrictProposals)
            .done(function(data) {
                $(ulId).append(data)
                $(ulId).show()
            })
            .fail(function(messageError) {
                display.warn("Error");
            })
            .always(function() {
                pageLoadingOff();
            });
    }
}