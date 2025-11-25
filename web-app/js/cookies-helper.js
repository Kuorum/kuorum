var cookiesHelper = {
    defaultCookieConsent: {
        'ad_storage': 'denied',
        'analytics_storage': 'denied',
        'personalization_storage': 'denied',
        'marketing_storage': 'denied',
        'ad_user_data': 'denied',
        'ad_personalization': 'denied'
    },
    cookieV2Name: "cookieV2Preferences",
    cookieTechnicalAccepted: "kuorumCookiesAccepted",
    cookieThirdAccepted: "kuorumThirdCookiesAccepted",

    setCookie: function(cname, cvalue, exdays) {
        const d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        const expires = "expires=" + d.toGMTString();
        const domain = document.domain;
        document.cookie = `${cname}=${encodeURIComponent(JSON.stringify(cvalue))}; ${expires};domain=${domain};path=/`;
    },

    getCookie: function(cname) {
        const name = cname + "=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1);
            if (c.indexOf(name) === 0) return c.substring(name.length, c.length);
        }
        return "";
    },

    getV2Cookie: function() {
        const preferences = this.getCookie(this.cookieV2Name);
        return preferences ? JSON.parse(preferences) : { ...this.defaultCookieConsent };
    },

    removeCookie: function(cname) {
        document.cookie = `${cname}=;domain=${document.domain}; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;`;
    },

    checkCookie: function(cname, onCookieFound, onNotCookieFound) {
        const cvalue = this.getCookie(cname);
        if (cvalue !== "") {
            onCookieFound(cvalue);
        } else {
            onNotCookieFound(cname);
        }
    },

    displayCookiesPolitics: function() {
        this.checkCookie(this.cookieTechnicalAccepted, () => {}, (cName) => {
            const buttonAccept = `<button id='acceptCookies' class='btn btn-orange' onclick='cookiesHelper.acceptAllCookies()'>${i18n.cookies.accept}</button>`;
            const buttonReject = `<button id='rejectCookies' class='btn btn-orange' onclick='cookiesHelper.rejectAllCookies()'>${i18n.cookies.reject}</button>`;
            const message = `<p>${i18n.cookies.message}</p>${buttonAccept}${buttonReject}${i18n.cookies.settingsLink}`;
            display.cookie(message);
        });
    },

    hideCookiesPolitics: function() {
        $('#noty_cookieLayout_layout_container').hide();
    },

    acceptTechnicalCookies: function() {
        this.setCookie(this.cookieTechnicalAccepted, "true", 99999);
    },

    acceptAllCookies: function() {
        this.setCookie(this.cookieTechnicalAccepted, "true", 99999);
        this.setCookie(this.cookieThirdAccepted, "true", 99999);
        const grantedConsent = this.updateAllConsent(this.defaultCookieConsent, 'granted');
        gtag('consent', 'update', grantedConsent);
        this.setCookie(this.cookieV2Name, grantedConsent, 99999);
        this.hideCookiesPolitics();
        if (typeof(cookiesInfo) !== 'undefined') {
            cookiesInfo.initTechnicalBox(true);
        }
    },

    rejectAllCookies: function() {
        this.setCookie(this.cookieTechnicalAccepted, "true", 99999);
        this.removeCookie(this.cookieThirdAccepted);
        const deniedConsent = this.updateAllConsent(this.defaultCookieConsent, 'denied');
        gtag('consent', 'update', deniedConsent);
        this.setCookie(this.cookieV2Name, deniedConsent, 99999);
        this.hideCookiesPolitics();
    },


    updateSpecificConsent: function(currentConsent, consentType, status) {
        const newConsent = { ...currentConsent };
        if (newConsent.hasOwnProperty(consentType)) {
            newConsent[consentType] = status;
        }
        return newConsent;
    },

    updateAllConsent: function(currentConsent, status) {
        const newConsent = { ...currentConsent };
        for (const key in newConsent) {
            if (newConsent.hasOwnProperty(key)) {
                newConsent[key] = status;
            }
        }
        return newConsent;
    }
};