$(document).ready(function () {
    let kuorumCookiesAccepted = cookiesHelper.getCookie('kuorumCookiesAccepted') ? true : false

    cookiesInfo.initTechnicalBox(kuorumCookiesAccepted)
})
var cookiesInfo = {
    initTechnicalBox: function (kuorumCookiesAccepted) {
        let technicalBox = $('#technicalCookiesAccepted');
        if (!kuorumCookiesAccepted) {
            cookiesHelper.acceptTechnicalCookies()
        }
        technicalBox.prop('checked', true)
        technicalBox.prop('disabled', true)

    }
}