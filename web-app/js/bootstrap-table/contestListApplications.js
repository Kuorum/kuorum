$(function () {
    $("#changeParticipatoryBudgetBtn").on("click", function (e) {
        e.preventDefault();
        $("#changeParticipatoryBudgetStatusModal").modal("show")
    })

    $("#changeParticipatoryBudgetStatusSubmit").on("click", function (e) {
        e.preventDefault();
        var $a = $(this)
        var url = $a.attr("href")
        var status = $("#changeParticipatoryBudgetStatusModalSelect").val()
        var statusText = $("#changeParticipatoryBudgetStatusModalSelect option:selected").text();
        var changeStatusData = {
            'status': status
        }
        pageLoadingOn();
        $.post(url, changeStatusData)
            .done(function (data) {
                if (data.success) {
                    participatoryBudgetListProposalHelper.refreshTable();
                    $("#changeParticipatoryBudgetStatusModal").modal("hide")
                    $("#changeParticipatoryBudgetBtnStatusText").html(statusText)
                } else {
                    display.warn(data.msg)
                }
            })
            .fail(function (error) {
                display.warn("There was an error changing the status");
            })
            .always(function () {
                pageLoadingOff();
            });
    })
});

function contestApplicationTableRowStyle(contestApplicationRow, index) {
    //var classes = ['active', 'success', 'info', 'warning', 'danger'];
    //if (index % 2 === 0 && index / 2 < classes.length) {
    //    return {
    //        classes: classes[index / 2]
    //    };
    //}
    if (contestApplicationRow.status == 'SENT' && contestApplicationRow.status == 'APPROVED') {
        return {classes: 'success'};
    }
    if (contestApplicationRow.status == 'REJECT') {
        return {classes: 'danger'};
    }
    return {};
}

function detailFormatterActions(index, campaignRow) {
    return "" +
        "<a href='#' class='btn btn-grey-light'>" + i18n.kuorum.web.commands.payment.contest.reject + "</a>" +
        "<a href='#' class='btn btn-blue'>" + i18n.kuorum.web.commands.payment.contest.approve + "</a>";
}

window.inputEventsCheckValidation = {
    'change :checkbox': function (e, value, row, index) {
        var newValue = $(e.target).prop('checked');
        var updateData = {
            approved: newValue,
            price: row.price,
            rejectComment: row.rejectComment
        }
        participatoryBudgetListProposalHelper.updateParticipatoryBudgetRow(index, row, updateData)
    }
}

var participatoryBudgetListProposalHelper = {

    updateParticipatoryBudgetRow: function (index, row, updatableData) {

        var data = {
            participatoryBudgetId: row.participatoryBudget.id,
            districtProposalUserId: row.user.id,
            districtProposalId: row.id,
            approved: updatableData.approved,
            price: updatableData.price,
            rejectComment: updatableData.rejectComment
        }
        $table = $("#contestApplicationsReviewTable")
        var urlUpdateTechnicalReview = $table.attr("data-update-technicalReview-url")
        pageLoadingOn();
        $.post(urlUpdateTechnicalReview, data)
            .done(function (response) {
                $table.bootstrapTable('updateRow', {
                    index: index,
                    row: response.districtProposalData
                });
            })
            .fail(function (messageError) {
                if (messageError.status == 420) {
                    display.error(messageError.responseJSON.msg);
                    $table.bootstrapTable('updateRow', {
                        index: index,
                        row: messageError.responseJSON.districtProposalData
                    });
                } else {
                    display.error("Error updating proposal")
                }

            })
            .always(function () {
                pageLoadingOff();
            });
    },

    refreshTable: function () {
        $table = $("#contestApplicationsReviewTable")
        $table.bootstrapTable('refresh', {silent: true})
    }
};
