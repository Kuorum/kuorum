let smsName = $("#smsDomainName").val();
let phonePrefix = $("#defaultPhonePrefix").val();

$(document).ready(function() {
    enableSmsFields();

    if ($("#validationPhone").click(function () {
        enableSmsFields();
    }));
});

function enableSmsFields() {

    toogleInputDisabled(!$("#validationPhone").prop('checked'));
}

function toogleInputDisabled(disableFields) {
    $("#smsDomainName").prop('disabled', disableFields);
    $("#defaultPhonePrefix").prop('disabled', disableFields);
    if(disableFields) {
        smsName = $("#smsDomainName").val();
        phonePrefix = $("#defaultPhonePrefix").val();
        $("#smsDomainName").val('');
        $("#defaultPhonePrefix").val('');
    } else {
        $("#smsDomainName").val(smsName);
        $("#defaultPhonePrefix").val(phonePrefix);
    }
}