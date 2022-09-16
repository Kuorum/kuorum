$(document).ready(function () {
    let kuorumCookiesAccepted = cookiesHelper.getCookie('kuorumCookiesAccepted') ? true : false
    let thirdCookiesAccepted = cookiesHelper.getCookie('kuorumThirdCookiesAccepted') ? true : false

    cookiesInfo.initTechnicalBox(kuorumCookiesAccepted)
    cookiesInfo.initThirdBox(thirdCookiesAccepted)
})
var cookiesInfo = {
    initTechnicalBox: function (kuorumCookiesAccepted) {
        let technicalBox = $('#technicalCookiesAccepted');
        if (!kuorumCookiesAccepted) {
            cookiesHelper.acceptTechnicalCookies()
        }
        technicalBox.prop('checked', true)
        technicalBox.prop('disabled', true)

    },
    initThirdBox: function (thirdCookiesAccepted) {
        let thirdCookiesBox = $('#thirdCookiesAccepted');
        thirdCookiesBox.prop('checked', thirdCookiesAccepted)
        thirdCookiesBox.on('change', this.thirdBoxHandler)
    },
    thirdBoxHandler: function (event) {
        cookiesHelper.acceptThirdCookies(event.target.checked)
    }
}