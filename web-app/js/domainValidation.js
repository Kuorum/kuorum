$(document).ready(function () {
    domainValidationFunctions.checkSmsFieldsVisibility();

    if ($("#validationPhone").click(function () {
        domainValidationFunctions.checkSmsFieldsVisibility();
    })) ;
});

var domainValidationFunctions = {
    smsName:$("#smsDomainName").val(),
    phonePrefix:$("#defaultPhonePrefix").val(),
    toogleInputVisibility: function (disableFields = false) {
        $("#smsDomainName").prop('disabled', disableFields);
        $("#defaultPhonePrefix").prop('disabled', disableFields);
        if (disableFields) {
            domainValidationFunctions.getVariableValueFromInput();
            domainValidationFunctions.setInputValuesToBlank();
        } else {
            domainValidationFunctions.setVariablesValuesOnInput();
        }
    },
    getVariableValueFromInput: function () {
        domainValidationFunctions.smsName = $("#smsDomainName").val();
        domainValidationFunctions.phonePrefix = $("#defaultPhonePrefix").val();
    },
    checkSmsFieldsVisibility: function () {
        domainValidationFunctions.toogleInputVisibility(!$("#validationPhone").prop('checked'));
    },
    setInputValuesToBlank: function (){
        $("#smsDomainName").val('');
        $("#defaultPhonePrefix").val('');
    },
    setVariablesValuesOnInput() {
        $("#smsDomainName").val(domainValidationFunctions.smsName);
        $("#defaultPhonePrefix").val(domainValidationFunctions.phonePrefix);
    }
}