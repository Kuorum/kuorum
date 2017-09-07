
$(document).ready(function() {
    cookiesHelper.displayCookiesPolitics();
})


var cookiesHelper = {
    cookieNamePoliticsAccepted: "kuorumCookiesAccepted",
    setCookie:function(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays*24*60*60*1000));
        var expires = "expires="+d.toGMTString();
        var domain = document.domain;
        if (domain.indexOf("kuorum.org")>1){
            domain = ".kuorum.org"; //Subdomains shares the cookie
        }
        document.cookie = cname + "=" + cvalue + "; " + expires+ ";domain="+domain+";path=/";
    },
    getCookie:function (cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for(var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) != -1) return c.substring(name.length,c.length);
        }
        return "";
    },
    removeCookie:function(cname){
        cookiesHelper.setCookie(cname, "", -1);
    },
    checkCookie: function(cname, onCookieFound, onNotCookieFound) {
        var cvalue=this.getCookie(cname);
        if (cvalue!="") {
            onCookieFound(cvalue);
        }else{
            onNotCookieFound(cname);
        }
    },
    displayCookiesPolitics: function (){
        this.checkCookie(
            this.cookieNamePoliticsAccepted,
            function(){},
            function(cName){
                var button = "<button id='acceptCookies' class='btn btn-xs' onclick='cookiesHelper.acceptedCookiesPolitics()'>"+i18n.cookies.accept+"</button>";
                var message = i18n.cookies.message + button;
                display.cookie(message);
            }
        )
    },
    acceptedCookiesPolitics: function(){
        this.setCookie(this.cookieNamePoliticsAccepted, "true", 99999);
    }
}

