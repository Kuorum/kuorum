$(function () {
    $("#changeContestBtn").on("click", function (e) {
        e.preventDefault();
        $("#changeContestStatusModal").modal("show")
    })

    $("#changeContestStatusSubmit").on("click", function (e) {
        e.preventDefault();
        var $a = $(this)
        var url = $a.attr("href")
        var status = $("#changeContestStatusModalSelect").val()
        var statusText = $("#changeContestStatusModalSelect option:selected").text();
        var changeStatusData = {
            'status': status
        }
        pageLoadingOn("Changing contest status");
        $.post(url, changeStatusData)
            .done(function (data) {
                console.log("Change status")
                if (data.success) {
                    contestListProposalHelper.refreshTable();
                    $("#changeContestStatusModal").modal("hide")
                    $("#changeContestBtnStatusText").html(statusText)
                } else {
                    display.warn(data.msg)
                }
            })
            .fail(function (error) {
                display.warn("There was an error changing the status");
            })
            .always(function () {
                pageLoadingOff("Changing contest status");
            });
    })
    $("#contestApplicationsReviewTable").on("click", ".box-ppal-action a", function (e) {
        e.preventDefault();
        $("#changeContestApplicationStatusModal").modal("show")
        const $approveOrRejectButton = $(this);
        const $modalOkButton = $("#modalEditParticipatoryBudgetStatusButtonOk");

        const statusParam = $approveOrRejectButton.attr("href");
        const urlUpdateStatus = kuorumUrls.contestApplicationUpdateReview + statusParam;
        $modalOkButton.attr("href", urlUpdateStatus);
    })
    $("#changeContestApplicationStatusModal").on("click", "#modalEditParticipatoryBudgetStatusButtonOk", function (e) {
        e.preventDefault()
        pageLoadingOn();

        const data = {}
        $.post($(this).attr("href"), data)
            .done(function (response) {
                if (response.success) {
                    contestListProposalHelper.refreshTable();
                } else {
                    display.error("Error updating application - Malformed data")
                }
            })
            .fail(function (messageError) {
                display.error("Error updating application")
            })
            .always(function () {
                pageLoadingOff();
                $("#changeContestApplicationStatusModal").modal("hide");
            });
    })
});

function contestApplicationTableRowStyle(contestApplicationRow, index) {

    if (contestApplicationRow.campaignStatus.type == 'SENT' || contestApplicationRow.campaignStatus.type == 'APPROVED') {
        return {classes: 'success'};
    }
    if (contestApplicationRow.campaignStatus.type == 'REJECT') {
        return {classes: 'danger'};
    }
    return {};
}

function detailFormatterActions(index, campaignRow) {
    if (campaignRow.campaignStatus.type == 'REVIEW') {
        var params = {
            contestApplicationId: campaignRow.id,
            newStatus: 'REJECT'
        }
        const paramsReject = jQuery.param(params);
        params['newStatus'] = 'APPROVED'
        const paramsApprove = jQuery.param(params);

        return "" +
        "<a href='"+ "?" + paramsReject + "' class='btn btn-grey-light' >" + i18n.kuorum.web.commands.payment.contest.reject + "</a>" +
            "<a href='" + "?" + paramsApprove + "' class='btn btn-blue'>" + i18n.kuorum.web.commands.payment.contest.approve + "</a>";
    } else {
        return "";
    }
}

var contestListProposalHelper = {

    refreshTable: function () {
        $table = $("#contestApplicationsReviewTable")
        $table.bootstrapTable('refresh', {silent: true})
    }
};
