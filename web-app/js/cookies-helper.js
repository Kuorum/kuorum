var cookiesHelper = {
    cookieTechnicalAccepted: "kuorumCookiesAccepted",
    cookieThirdAccepted: "kuorumThirdCookiesAccepted",
    setCookie: function (cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toGMTString();
        var domain = document.domain;
        if (domain.indexOf("kuorum.org") > 1) {
            domain = ".kuorum.org"; //Subdomains shares the cookie
        }
        document.cookie = cname + "=" + cvalue + "; " + expires + ";domain=" + domain + ";path=/";
    },
    getCookie: function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
        }
        return "";
    },
    removeCookie: function (cname) {
        cookiesHelper.setCookie(cname, "", -1);
        document.cookie = cname + '=; expires=Thu, 01-Jan-70 00:00:01 GMT;';
    },
    checkCookie: function (cname, onCookieFound, onNotCookieFound) {
        var cvalue = this.getCookie(cname);
        if (cvalue != "") {
            onCookieFound(cvalue);
        } else {
            onNotCookieFound(cname);
        }
    },
    displayCookiesPolitics: function () {
        this.checkCookie(
            this.cookieTechnicalAccepted,
            function () {
            },
            function (cName) {
                var buttonAccept = "<button id='acceptCookies' class='btn btn-xs' onclick='cookiesHelper.acceptedAllCookies()'>" + i18n.cookies.accept + "</button>";
                var message = "<p>" + i18n.cookies.message + "</p><div>" + i18n.cookies.settingsLink + buttonAccept + "</div>"
                display.cookie(message);
            }
        )
    },
    acceptedAllCookies: function () {
        cookiesHelper.acceptTechnicalCookies()
        cookiesHelper.acceptThirdCookies(true)
        if (cookiesInfo) {
            cookiesInfo.initTechnicalBox(true)
            cookiesInfo.initThirdBox(true)
            cookiesInfo.initAllButton(true)
        }
    },
    acceptTechnicalCookies: function () {
        cookiesHelper.setCookie(this.cookieTechnicalAccepted, "true", 99999);
    },
    acceptThirdCookies: function (value) {
        if (value) {
            cookiesHelper.setCookie(this.cookieThirdAccepted, "true", 99999);
        } else {
            cookiesHelper.removeCookie(this.cookieThirdAccepted)
        }
    }
}
