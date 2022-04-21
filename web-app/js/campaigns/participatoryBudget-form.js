$(function() {

    var _isValidParticipatoryBudgetForm = function () {
        var valid = $("#districtParticipatoryBudgetForm").valid();

        return valid;
    }

    // OVERWRITE CUSTOM FORM VALIDATION
    campaignForm.validateCampaignForm = _isValidParticipatoryBudgetForm


    $("#participatoryBudgetType").change(function (e) {
        $(this).find("option").each(function (idx, option) {
            $(".participatory-budget-voting-container").removeClass(option.value)
        })
        $(".participatory-budget-voting-container").addClass($(this).val())
    })

})