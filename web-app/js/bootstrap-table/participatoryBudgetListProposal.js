
function detailFormatter(index, row) {
    var html = [];
    html.push('<div>')
    html.push('<form>')
    html.push('<label>ID</label>')
    html.push('<input type="number" value="'+row.id+'"/>')
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

function formatBoolean(value){
    var icon = value ? 'glyphicon-ok' : 'glyphicon-remove'
    var text=value?"Aceptado":"No aceptado"
    return '<i class="glyphicon ' + icon + '"></i> '+text+' <span style="display:none">' + value+'</span>';
    //return text;
}
function formatEditableField(value){
    return '<input type="text" value="' + value+'"/>';
}

window.inputEventsPrice = {
    'change :input': function (e, value, row, index) {
        var newValue = $(e.target).val();
        row.price=newValue
        console.log(newValue)
        $table.bootstrapTable('updateRow', {
            index: index,
            row: row
        });
    }
};

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
