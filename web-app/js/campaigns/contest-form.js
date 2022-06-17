$(function () {

    var _isValidContestForm = function () {
        var valid = $("#contestForm").valid();

        return valid;
    }

    // OVERWRITE CUSTOM FORM VALIDATION
    campaignForm.validateCampaignForm = _isValidContestForm

})