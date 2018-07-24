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


});