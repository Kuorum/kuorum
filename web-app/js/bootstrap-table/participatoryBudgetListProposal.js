
function detailFormatter(index, districtProposal) {
    var html = [];
    html.push('<div>')
    html.push('<form>')
    html.push('<label>ID</label>')
    html.push('<input type="number" value="'+districtProposal.id+'"/>')
    html.push('</form>')
    html.push('</div>')
    return html.join('');
}
function rowStyle(row, index) {
    //var classes = ['active', 'success', 'info', 'warning', 'danger'];
    //if (index % 2 === 0 && index / 2 < classes.length) {
    //    return {
    //        classes: classes[index / 2]
    //    };
    //}
    if (row.valid){
        return {classes: 'success'};
    }
    return {};
}

function formatCheckValidation(value, districtProposalRow){
    if (districtProposalRow.participatoryBudget.status.name =='TECHNICAL_REVIEW'){
        var checked = value ?'checked':'';
        return '<input type="checkbox" '+checked+'>'
    }else{
        var icon = value ? 'fa-check' : 'fa-remove'
        return '<span class="fa ' + icon + '"></span>';
    }
    //return text;
}
function formatPrice(value, districtProposalRow){
    if (value == null || value == undefined){
        value = '';
    }
    var text = value;
    if (districtProposalRow.participatoryBudget.status.name =='TECHNICAL_REVIEW' && districtProposalRow.approved){
        text = '<input type="text" value="' + value+'"/>';
    }
    return text==''?'--':text;
}

function formatRejectText(value, districtProposalRow){
    if (value == null || value == undefined || value ==''){
        value = '';
    }
    var text = value;
    if (districtProposalRow.participatoryBudget.status.name =='TECHNICAL_REVIEW' && !districtProposalRow.approved){
        text = '<input type="text" value="' + value+'"/>';
    }
    return text==''?'--':text;
}

window.inputEventsPrice = {
    'change :input': function (e, value, row, index) {
        var newValue = $(e.target).val();
        row.price=newValue
        console.log(newValue)
        $("#table").bootstrapTable('updateRow', {
            index: index,
            row: row
        });
    }
};

window.inputEventsCheckValidation={
    'change :checkbox': function (e, value, row, index) {
        var newValue = $(e.target).prop('checked');
        row.approved=newValue
        $("#table").bootstrapTable('updateRow', {
            index: index,
            row: row
        });
    }
}

var participatoryBudgetListProposalHelper = {
    renderProposalExtraInfo:function (districtProposal) {
        const districtProposalTableInfo = `
         <div class="person">
            <h2>
                ${districtProposal.title}
            </h2>
            <p class="location">${districtProposal.location}</p>
            <p class="bio">${districtProposal.bio}</p>
         </div>
        `;
        return districtProposalTableInfo;
    }
}
