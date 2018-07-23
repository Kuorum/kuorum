$(function() {

    var _isValidParticipatoryBudgetForm = function(){
        var valid = $("#districtParticipatoryBudgetForm").valid();

        return valid;
    }

    // OVERWRITE CUSTOM FORM VALIDATION
    campaignForm.validateCampaignForm = _isValidParticipatoryBudgetForm
})