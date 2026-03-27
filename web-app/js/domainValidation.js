$(document).ready(function () {
    domainValidationFunctions.checkSmsFieldsVisibility();
    domainValidationFunctions.checkExternalIdFieldsVisibility();
    $("#validationPhone").change(function () {
        domainValidationFunctions.checkSmsFieldsVisibility();
    });
    $('input[name=firstFactorValidation]').change(function () {
        domainValidationFunctions.checkExternalIdFieldsVisibility();
    });
});

var domainValidationFunctions = {
    smsName:$("#smsDomainName").val(),
    phonePrefix:$("#defaultPhonePrefix").val(),
    externalIdName:$("#externalIdName").val(),
    toggleInputVisibility: function (disableFields = false) {
        $("#smsDomainName").prop('disabled', disableFields);
        $("#defaultPhonePrefix").prop('disabled', disableFields);
        $("#validationWhatsApp").prop('disabled', disableFields);
        if (disableFields) {
            domainValidationFunctions.getVariableValueFromInput();
            domainValidationFunctions.setInputValuesToBlank([$("#smsDomainName"), $("#defaultPhonePrefix")]);
            $("#validationWhatsApp").prop('checked', false);
        } else {
            domainValidationFunctions.setVariablesValuesOnInput();
        }
    },
    getVariableValueFromInput: function () {
        domainValidationFunctions.smsName = $("#smsDomainName").val();
        domainValidationFunctions.phonePrefix = $("#defaultPhonePrefix").val();
    },
    setVariablesValuesOnInput() {
        $("#smsDomainName").val(domainValidationFunctions.smsName);
        $("#defaultPhonePrefix").val(domainValidationFunctions.phonePrefix);
    },
    checkSmsFieldsVisibility: function () {
        var validationPhoneDisabled = $("#validationPhone").attr("disabled") == 'disabled';
        var isValidationPhoneChecked = $("#validationPhone").prop('checked');
        console.log("Disabled: " + validationPhoneDisabled)
        console.log("Checked: " + isValidationPhoneChecked)
        console.log("Visibility:" + (!validationPhoneDisabled && !isValidationPhoneChecked));
        domainValidationFunctions.toggleInputVisibility(validationPhoneDisabled || !isValidationPhoneChecked);
    },
    checkExternalIdFieldsVisibility: function () {
        var firstFactorDisabled = $("#firstFactor").attr("disabled") == 'disabled';
        var isQRFirstFactorSelected = $('input[name=firstFactorValidation]:checked', '#domainConfigValidationForm').val() === 'QR';
        console.log("Disabled: " + firstFactorDisabled)
        console.log("Checked: " + isQRFirstFactorSelected)
        console.log("Visibility:" + (!firstFactorDisabled && !isQRFirstFactorSelected));
        domainValidationFunctions.toggleInputVisibilityExternalIdName(firstFactorDisabled || !isQRFirstFactorSelected);
    },
    toggleInputVisibilityExternalIdName: function (disableFields = false) {
        $("#externalIdName").prop('disabled', disableFields);
        if (disableFields) {
            domainValidationFunctions.getVariableValueFromInputExternalIdName();
            domainValidationFunctions.setInputValuesToBlank([$("#externalIdName")]);
        } else {
            domainValidationFunctions.setVariablesValuesOnInputExternalIdName();
        }
    },
    setInputValuesToBlank: function ($inputArray){
        for (const $input in $inputArray) {
            $inputArray[$input].val('');
        }
    },
    setVariablesValuesOnInputExternalIdName() {
        $("#externalIdName").val(domainValidationFunctions.externalIdName);
    },
    getVariableValueFromInputExternalIdName: function () {
        domainValidationFunctions.externalIdName = $("#externalIdName").val();
    }
}