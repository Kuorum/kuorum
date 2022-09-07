$(document).ready(function () {
    let kuorumCookiesAccepted = cookiesHelper.getCookie('kuorumCookiesAccepted') ? true : false
    let thirdCookiesAccepted = cookiesHelper.getCookie('kuorumThirdCookiesAccepted') ? true : false

    cookiesInfo.initTechnicalBox(kuorumCookiesAccepted)
    cookiesInfo.initThirdBox(thirdCookiesAccepted)
    cookiesInfo.initAllButton(kuorumCookiesAccepted)
})
var cookiesInfo = {
    initAllButton: function (kuorumCookiesAccepted) {
        let button = $('#acceptAllButton')
        if (kuorumCookiesAccepted) {
            button.css('display', 'none')
        }else {
            button.css('display', 'inline')
            button.on('click', cookiesHelper.acceptedAllCookies)
        }
    },
    initTechnicalBox: function (kuorumCookiesAccepted) {
        let technicalBox = $('#technicalCookiesAccepted');
        if (kuorumCookiesAccepted) {
            technicalBox.prop('checked', true)
            technicalBox.prop('disabled', true)
        } else {
            technicalBox.on('change', this.technicalBoxHandler)
        }
    },
    initThirdBox: function (thirdCookiesAccepted) {
        let thirdCookiesBox = $('#thirdCookiesAccepted');
        thirdCookiesBox.prop('checked', thirdCookiesAccepted)
        thirdCookiesBox.on('change', this.thirdBoxHandler)
    },
    technicalBoxHandler: function (event) {
        let technicalBox = event.target
        if (technicalBox.checked) {
            cookiesHelper.acceptTechnicalCookies()
            cookiesInfo.initTechnicalBox(true)
            cookiesInfo.initAllButton(true)
        }
    },
    thirdBoxHandler: function (event) {
        cookiesHelper.acceptThirdCookies(event.target.checked)
    }
}