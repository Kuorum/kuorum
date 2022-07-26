
$(function(){
    $("#changeParticipatoryBudgetBtn").on("click", function(e){
        e.preventDefault();
        $("#changeParticipatoryBudgetStatusModal").modal("show")
    })

    $("#changeParticipatoryBudgetStatusSubmit").on("click", function(e){
        e.preventDefault();
        var $a = $(this)
        var url = $a.attr("href")
        var status = $("#changeParticipatoryBudgetStatusModalSelect").val()
        var statusText = $("#changeParticipatoryBudgetStatusModalSelect option:selected").text();
        var changeStatusData = {
            'status' : status
        }
        pageLoadingOn();
        $.post(url, changeStatusData)
            .done(function(data){
                if (data.success){
                    participatoryBudgetListProposalHelper.refreshTable();
                    $("#changeParticipatoryBudgetStatusModal").modal("hide")
                    $("#changeParticipatoryBudgetBtnStatusText").html(statusText)
                }else{
                    display.warn(data.msg)
                }
            })
            .fail(function(error){
                display.warn("There was an error changing the status");
            })
            .always(function() {
                pageLoadingOff();
            });
    })
});

function districtProposalTableRowStyle(districtProposalRow, index) {
    //var classes = ['active', 'success', 'info', 'warning', 'danger'];
    //if (index % 2 === 0 && index / 2 < classes.length) {
    //    return {
    //        classes: classes[index / 2]
    //    };
    //}
    if (districtProposalRow.implemented){
        return {classes: 'success'};
    }
    if (districtProposalRow.technicalReviewStatus.type=='INCORRECT'){
        return {classes: 'danger'};
    }
    return {};
}

function formatPrice(value, districtProposalRow){
    if (value == null || value == undefined){
        value = '';
    }
    var text = value;
    if (districtProposalRow.participatoryBudget.status.type =='TECHNICAL_REVIEW' && districtProposalRow.approved){
        text = '<input type="text" class="form-control" value="' + value+'"/>';
    }
    return text==''?'--':text;
}

function formatRejectText(value, districtProposalRow){
    if (value == null || value == undefined || value ==''){
        value = '';
    }
    var text = value;
    if (districtProposalRow.participatoryBudget.status.type =='TECHNICAL_REVIEW' && !districtProposalRow.approved){
        text = '<input type="text" class="form-control" value="' + value+'"/>';
    }
    return text==''?'--':text;
}

window.inputEventsPrice = {
    'change :input': function (e, value, row, index) {
        var newValue = $(e.target).val();
        var updateData = {
            approved:row.approved,
            price:newValue,
            rejectComment:row.rejectComment
        }
        participatoryBudgetListProposalHelper.updateParticipatoryBudgetRow(index, row, updateData)
    }
};

window.inputEventsRejectComment = {
    'change :input': function (e, value, row, index) {
        var newValue = $(e.target).val();
        var updateData = {
            approved:row.approved,
            price:row.price,
            rejectComment:newValue
        }
        participatoryBudgetListProposalHelper.updateParticipatoryBudgetRow(index, row, updateData)
    }
};

window.inputEventsCheckValidation={
    'change :checkbox': function (e, value, row, index) {
        var newValue = $(e.target).prop('checked');
        var updateData = {
            approved:newValue,
            price:row.price,
            rejectComment:row.rejectComment
        }
        participatoryBudgetListProposalHelper.updateParticipatoryBudgetRow(index, row, updateData)
    }
}
var participatoryBudgetListProposalHelper = {

    updateParticipatoryBudgetRow:function(index,row, updatableData){

        var data = {
            participatoryBudgetId:row.participatoryBudget.id,
            districtProposalUserId:row.user.id,
            districtProposalId:row.id,
            approved:updatableData.approved,
            price:updatableData.price,
            rejectComment:updatableData.rejectComment
        }
        $table = $("#participatoryBudgetProposalReviewTable")
        var urlUpdateTechnicalReview = $table.attr("data-update-technicalReview-url")
        pageLoadingOn();
        $.post( urlUpdateTechnicalReview, data)
            .done(function(response) {
                $table.bootstrapTable('updateRow', {
                    index: index,
                    row: response.districtProposalData
                });
            })
            .fail(function(messageError) {
                if (messageError.status == 420){
                    display.error(messageError.responseJSON.msg);
                    $table.bootstrapTable('updateRow', {
                        index: index,
                        row: messageError.responseJSON.districtProposalData
                    });
                }else{
                    display.error("Error updating proposal")
                }

            })
            .always(function() {
                pageLoadingOff();
            });
    },

    refreshTable: function(){
        $table = $("#participatoryBudgetProposalReviewTable")
        $table.bootstrapTable('refresh',{silent:true})
    }
};
