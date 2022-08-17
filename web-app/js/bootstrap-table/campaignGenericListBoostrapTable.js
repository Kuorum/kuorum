function formatTableCampaignId(value, campaignRow) {
    return "<a href='" + campaignRow.url + "' target='_blank'>" + value + "</a>"
}

function formatTableCampaignAuthor(value, campaignRow) {
    return "<a href='" + campaignRow.user.userLink + "' target='_blank'>" + value + "</a>"
}

function detailFormatter(index, campaignRow) {
    var multimedia = ""
    if (campaignRow.photoUrl != null && campaignRow.photoUrl != undefined) {
        multimedia = "<img src='" + campaignRow.photoUrl + "'/>"
    } else {
        multimedia = "<img class='empty' src='images/emptyCampaign.png'/>"
    }
    var buttonActions = ""
    if (typeof detailFormatterActions === "function") {
        buttonActions = detailFormatterActions(index, campaignRow);
    }
    //IE8 compatibility <- Before was const campaignRow = `data`
    var campaignTableInfo = "";
    var campaignTableInfo = campaignTableInfo + "<div class=\"box-ppal\">";
    var campaignTableInfo = campaignTableInfo + "   <div class=\"box-ppal-title\">" + campaignRow.title + "</div>";
    var campaignTableInfo = campaignTableInfo + "   <div class=\"box-ppal-section\">";
    var campaignTableInfo = campaignTableInfo + "       <div class=\"row\">";
    var campaignTableInfo = campaignTableInfo + "           <div class=\"col-md-3\">";
    var campaignTableInfo = campaignTableInfo + "               " + campaignRow.multimediaHtml;
    var campaignTableInfo = campaignTableInfo + "           </div>";
    var campaignTableInfo = campaignTableInfo + "           <div class=\"distict-proposal-body col-md-9\">";
    var campaignTableInfo = campaignTableInfo + "               " + campaignRow.body;
    var campaignTableInfo = campaignTableInfo + "           </div>";
    var campaignTableInfo = campaignTableInfo + "       </div>";
    var campaignTableInfo = campaignTableInfo + "   </div>";
    if (buttonActions != "") {
        var campaignTableInfo = campaignTableInfo + "<div class='box-ppal-action'>" + buttonActions + "</div>";
    }
    var campaignTableInfo = campaignTableInfo + "</div>";
    return campaignTableInfo;
}

function formatBoolean(value) {
    var icon = value ? 'fa-check' : 'fa-remove'
    return '<span class="fa ' + icon + '"></span>';
}

$(".list-working-campaign-table").on('expand-row.bs.table', function (index, row, $detail) {
    prepareYoutubeVideosClick()
})

function formatCheckValidation(value, districtProposalRow) {
    if (districtProposalRow.participatoryBudget.status.type == 'TECHNICAL_REVIEW') {
        var checked = value ? 'checked' : '';
        // return '<input type="checkbox" '+checked+'>'
        return '<label class="checkbox-inline">' +
            '<input type="checkbox" ' + checked + '>' +
            '<span class="check-box-icon"></span>' +
            '<span class="label-checkbox"></span>' +
            '</label>';
    } else {
        return formatBoolean(value)
    }
    //return text;
}