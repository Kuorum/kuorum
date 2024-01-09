$(document).ready(handleCookieWindow)

function handleCookieWindow() {
    var isThirdCookiesAccepted = cookiesHelper.getCookie('kuorumThirdCookiesAccepted') !== '';
    if (typeof cookiesInfo === 'undefined' || !isThirdCookiesAccepted) {
        cookiesHelper.displayCookiesPolitics();
    }
    hideCookieWindow();
}

function hideCookieWindow() {
    var thirdCookiesAcceptedCheckbox = document.getElementById("thirdCookiesAccepted");
    if (thirdCookiesAcceptedCheckbox) {
        thirdCookiesAcceptedCheckbox.addEventListener("click", function () {
            thirdCookiesAcceptedCheckbox.checked ? cookiesHelper.hideCookiesPolitics() : ''
        });
    }
}