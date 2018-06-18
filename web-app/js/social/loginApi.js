
$(function (){
    var callbackError = function(msg){
        console.log("Error login: "+msg)
        display.warn(i18n.register.errors)
    };
    var apiCallback = function(response) {
        pageLoadingOn("Api login")
        var callbackFunctionName = $('#registro').find("form").attr("callback");
        var callbackSuccess = noLoggedCallbacks[callbackFunctionName] || noLoggedCallbacks.reloadPage
        if (response.success)
        {
            var data = response;
            data['provider']='api';
            $.ajax({
                type:"POST",
                url:kuorumUrls.ajaxLoginRRSS,
                data:data,
                dataType:"json"
            }).success(function(data){
                callbackSuccess()
            }).error(function(jqXHR, textStatus,errorThrown){
                callbackError()
                pageLoadingOn("Error Callback")
            }).done(function(data){
            });
        }else{
            callbackError()
            pageLoadingOff("Error Callback")
        }
    }

    $(".socialGo .btn.fb").on("click", function(e){
        e.preventDefault();
        var socialButton = new SocialButton("facebook",apiCallback, callbackError);
        socialButton.openSocialLoginWindow()
    })
    $(".socialGo .btn.gog").on("click", function(e){
        e.preventDefault();
        var socialButton = new SocialButton("google",apiCallback, callbackError);
        socialButton.openSocialLoginWindow()
    })
})