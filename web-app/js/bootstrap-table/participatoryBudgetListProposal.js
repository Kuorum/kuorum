
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


function detailFormatter(index, districtProposalRow) {
    return participatoryBudgetListProposalHelper.renderProposalExtraInfo(districtProposalRow);
}
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

function formatTableParticipatoryBudgetDistrictProposalId(value, districtProposalRow){
    return "<a href='"+districtProposalRow.url+"' target='_blank'>"+value+"</a>"
}

function formatTableParticipatoryBudgetDistrictProposalAuthor(value, districtProposalRow){
    return "<a href='"+districtProposalRow.user.userLink+"' target='_blank'>"+value+"</a>"
}

function formatCheckValidation(value, districtProposalRow){
    if (districtProposalRow.participatoryBudget.status.type =='TECHNICAL_REVIEW'){
        var checked = value ?'checked':'';
        return '<input type="checkbox" '+checked+'>'
    }else{
        return formatBoolean(value)
    }
    //return text;
}

function formatBoolean(value){
    var icon = value ? 'fa-check' : 'fa-remove'
    return '<span class="fa ' + icon + '"></span>';
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

$("#participatoryBudgetProposalReviewTable").on('expand-row.bs.table', function(index, row, $detail){
    prepareYoutubeVideosClick()
})

var participatoryBudgetListProposalHelper = {
    renderProposalExtraInfo:function (districtProposal) {
        var multimedia = ""
        if (districtProposal.photoUrl != null && districtProposal.photoUrl != undefined){
            multimedia = "<img src='"+districtProposal.photoUrl+"'/>"
        }else{
            multimedia = "<img class='empty' src='images/emptyCampaign.png'/>"
        }
        const districtProposalTableInfo = `
         <div class="box-ppal">
            <div class="box-ppal-title">${districtProposal.title}</div>
            <div class="box-ppal-section">
                <div class="row">
                    <div class="col-md-3">
                        ${districtProposal.multimediaHtml}
                    </div>
                    <div class="distict-proposal-body col-md-9">
                        ${districtProposal.body}
                    </div>            

                </div>
            </div>            
         </div>
        `;
        return districtProposalTableInfo;
    },

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
            .done(function(districtProposalData) {
                $table.bootstrapTable('updateRow', {
                    index: index,
                    row: districtProposalData
                });
            })
            .fail(function(messageError) {
                display.warn("Error");
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
