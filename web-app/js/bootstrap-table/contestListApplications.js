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
        pageLoadingOn();
        $.post(url, changeStatusData)
            .done(function (data) {
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
                pageLoadingOff();
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
            action: 'REJECT'
        }
        const paramsReject = jQuery.param(params);
        params['action'] = 'APPROVED'
        const paramsApprove = jQuery.param(params);

        return "" +
            "<a href='" + kuorumUrls.contestApplicationUpdateReview + "?" + paramsReject + "' class='btn btn-grey-light'>" + i18n.kuorum.web.commands.payment.contest.reject + "</a>" +
            "<a href='" + kuorumUrls.contestApplicationUpdateReview + "?" + paramsApprove + "' class='btn btn-blue'>" + i18n.kuorum.web.commands.payment.contest.approve + "</a>";
    } else {
        return "";
    }
}

window.inputEventsCheckValidation = {
    'change :checkbox': function (e, value, row, index) {
        var newValue = $(e.target).prop('checked');
        var updateData = {
            approved: newValue,
            price: row.price,
            rejectComment: row.rejectComment
        }
        ContestListProposalHelper.updateContestRow(index, row, updateData)
    }
}

var contestListProposalHelper = {

    updateContestRow: function (index, row, updatableData) {

        var data = {
            ContestId: row.Contest.id,
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
