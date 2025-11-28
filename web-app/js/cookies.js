$(document).ready(handleCookieWindow);

function handleCookieWindow() {
    // Default consent is already defined in <head>.
    const currentCookie = cookiesHelper.getV2Cookie();
    const isThirdCookiesAccepted = cookiesHelper.getCookie(cookiesHelper.cookieThirdAccepted) !== '';

    if (isThirdCookiesAccepted) {
        gtag('consent', 'update', currentCookie);
    }

    if (typeof cookiesInfo === 'undefined') {
        cookiesHelper.displayCookiesPolitics();
    }

    hideCookieWindow();
    setupCookieV2Config();
}

function hideCookieWindow() {
    const thirdCookiesAcceptedCheckbox = document.getElementById("thirdCookiesAccepted");
    if (thirdCookiesAcceptedCheckbox) {
        thirdCookiesAcceptedCheckbox.addEventListener("click", function() {
            if (thirdCookiesAcceptedCheckbox.checked) {
                cookiesHelper.hideCookiesPolitics();
            }
        });
    }
}

function setupCookieV2Config() {
    const thirdCookiesAcceptedCheckbox = document.getElementById("thirdCookiesAccepted");
    const checkboxIds = ['ad_storage', 'analytics_storage', 'personalization_storage', 'marketing_storage', 'ad_user_data', 'ad_personalization'];
    const checkboxes = checkboxIds.map(id => document.getElementById(id));
    const allCheckboxesExist = checkboxes.every(cb => cb !== null);

    if (!allCheckboxesExist) {
        return;
    }

    const toggleIndividualCheckboxes = (isChecked) => {
        checkboxes.forEach(checkbox => {
            checkbox.disabled = !isChecked;
        });
    };

    const isThirdCookiesAccepted = cookiesHelper.getCookie(cookiesHelper.cookieThirdAccepted) !== '';
    const currentCookieConfig = cookiesHelper.getV2Cookie();

    thirdCookiesAcceptedCheckbox.checked = isThirdCookiesAccepted;
    checkboxes.forEach(checkbox => {
        if (currentCookieConfig.hasOwnProperty(checkbox.id)) {
            checkbox.checked = (currentCookieConfig[checkbox.id] === 'granted');
        }
    });

    toggleIndividualCheckboxes(isThirdCookiesAccepted);

    thirdCookiesAcceptedCheckbox.addEventListener("change", () => {
        const status = thirdCookiesAcceptedCheckbox.checked ? 'granted' : 'denied';
        const updatedConsent = cookiesHelper.updateAllConsent(cookiesHelper.defaultCookieConsent, status);
        gtag('consent', 'update', updatedConsent);
        cookiesHelper.setCookie(cookiesHelper.cookieV2Name, updatedConsent, 99999);

        if (thirdCookiesAcceptedCheckbox.checked) {
            cookiesHelper.setCookie(cookiesHelper.cookieThirdAccepted, "true", 99999);
        } else {
            cookiesHelper.removeCookie(cookiesHelper.cookieThirdAccepted);
        }

        toggleIndividualCheckboxes(thirdCookiesAcceptedCheckbox.checked);

        if (!thirdCookiesAcceptedCheckbox.checked) {
            checkboxes.forEach(checkbox => checkbox.checked = false);
        } else {
            checkboxes.forEach(checkbox => checkbox.checked = true);
        }
    });

    addEventsForCookieCheckboxes();
}

function addEventsForCookieCheckboxes() {
    const checkboxIds = ['ad_storage', 'analytics_storage', 'ad_user_data', 'ad_personalization', 'personalization_storage', 'marketing_storage'];
    const checkboxes = checkboxIds.map(id => document.getElementById(id));

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const currentConsent = cookiesHelper.getV2Cookie();
            const status = checkbox.checked ? 'granted' : 'denied';
            const updatedConsent = cookiesHelper.updateSpecificConsent(currentConsent, checkbox.id, status);
            gtag('consent', 'update', updatedConsent);
            cookiesHelper.setCookie(cookiesHelper.cookieV2Name, updatedConsent, 99999);
        });
    });
}