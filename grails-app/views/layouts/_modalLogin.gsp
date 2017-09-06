<%@ page import="springSecurity.KuorumRegisterCommand" %>

<!-- Modal registro/login -->
<div class="modal fade modal-register" id="registro" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <h4><g:message code="register.title"/></h4>
                <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>
            </div>
            <div class="modal-body">
                <!-- Formulario de Entrar -->
                <g:include controller="login" action="loginForm"/>
                <!-- Formulario de Registro -->
                <g:render template="/layouts/registerForm" model="[registerCommand: new springSecurity.KuorumRegisterCommand(), formId:'signup-modal']"/>
                <g:render template="/register/registerSocial"/>
            </div>
        </div>
    </div>
</div>

<div id="fb-root"></div>
<script>

    /// FACBOOK AJAX LOGIN
    window.fbAsyncInit = function() {
        FB.init({
            //            appId: '226641644202506',
            appId: '${_facebookConfig?.key?:''}',
            status: true,
            cookie: true, // enable cookies to allow the server to access the session
            oauth: true, // parse social plugins on this page
            version:'v2.8',
            xfbml: true});

        //        FB.getLoginStatus(function(response) {
        //            console.log("FB Login")
        //            console.log(response)
        //        });
    };
    (function() {
        var e = document.createElement('script'); e.async = true;
        e.src = document.location.protocol +
                '//connect.facebook.net/es_ES/all.js';
        document.getElementById('fb-root').appendChild(e);
    }());

    function FBLogin(callbackSuccess, callbackError)
    {
        callbackError = callbackError || function(){display.warn("ERROR LOGIN FACEBOOK")};
        callbackSuccess = callbackSuccess || noLoggedCallbacks.reloadPage;
        FB.login(function(response) {
            if (response.authResponse)
            {
                var data = response.authResponse;
                data['provider']='facebook';
                $.ajax({
                    type:"POST",
                    url:"${g.createLink(mapping: 'registerAjaxRRSSOAuth')}",
                    data:response.authResponse,
                    dataType:"json"
                }).success(function(data){
                    callbackSuccess()
                }).error(function(jqXHR, textStatus,errorThrown){
                    callbackError()
                }).done(function(data){
                });
            } else
            {
//                    callbackError()
            }
        },{scope: 'public_profile,email'});
    }
    $("#registro .socialGo .btn.fb").on("click", function(e){
        e.preventDefault();
        var callbackFunctionName = $('#registro').find("form").attr("callback");
        FBLogin(noLoggedCallbacks[callbackFunctionName]);
    })
</script>


<script>
    var GoogleAuth;
    var SCOPE = 'https://www.googleapis.com/auth/drive.metadata.readonly';
    function handleClientLoad() {
        // Load the API's client and auth2 modules.
        // Call the initClient function after the modules load.
        gapi.load('client:auth2', initClient);
    }

    function initClient() {
        // Retrieve the discovery document for version 3 of Google Drive API.
        // In practice, your app can retrieve one or more discovery documents.
        var discoveryUrl = 'https://www.googleapis.com/discovery/v1/apis/drive/v3/rest';

        // Initialize the gapi.client object, which app uses to make API requests.
        // Get API key and client ID from API Console.
        // 'scope' field specifies space-delimited list of access scopes.
        gapi.client.init({
            'apiKey': 'AIzaSyBlkPXlyoUtZZfco4OF3o27OmL7NjCOXm0',
            'discoveryDocs': [discoveryUrl],
            'clientId': '${_googleConfig?.key?:''}',
            'scope': SCOPE
        }).then(function () {
            GoogleAuth = gapi.auth2.getAuthInstance();

            // Listen for sign-in state changes.
            GoogleAuth.isSignedIn.listen(successLogin);

            // Handle initial sign-in state. (Determine if user is already signed in.)
            var user = GoogleAuth.currentUser.get();
            setSigninStatus();

            // Call handleAuthClick function when user clicks on
            //      "Sign In/Authorize" button.
            $("#registro .socialGo .btn.gog").on("click", function(e){
                e.preventDefault()
                handleAuthClick();
            });
        });
    }

    function handleAuthClick() {
        if (GoogleAuth.isSignedIn.get()) {
            // User is authorized and has clicked 'Sign out' button.
//            GoogleAuth.signOut();
            successLogin(true)
        } else {
            // User is not signed in. Start Google auth flow.
            GoogleAuth.signIn();
        }
    }

    function revokeAccess() {
        GoogleAuth.disconnect();
    }

    function setSigninStatus(isSignedIn) {
        var user = GoogleAuth.currentUser.get();
        var isAuthorized = user.hasGrantedScopes(SCOPE);
        if (isAuthorized) {
//            successLogin(true)
        } else {
//            console.log("NOT AUTHORIZED")
        }
    }

    function successLogin(isSignedIn) {
        if (isSignedIn){
            var callbackError = function(){display.warn("ERROR LOGIN Google")};
            var callbackFunctionName = $('#registro').find("form").attr("callback");
            var callbackSuccess = noLoggedCallbacks.reloadPage;
            if (callbackFunctionName){
                callbackSuccess = noLoggedCallbacks[callbackFunctionName];
            }

            var user = gapi.auth2.getAuthInstance().currentUser.get();
            var oauthToken = user.getAuthResponse().access_token;
            if (oauthToken){
                console.log(user.getAuthResponse())
                var data = user.getAuthResponse();
                data['provider']="google"
                $.ajax({
                    type:"POST",
                    url:"${g.createLink(mapping: 'registerAjaxRRSSOAuth')}",
                    data:data,
                    dataType:"json"
                }).success(function(data){
                    callbackSuccess()
                }).error(function(jqXHR, textStatus,errorThrown){
                    callbackError()
                }).done(function(data){
                });
            }
        }
    }
</script>

<script async defer src="https://apis.google.com/js/api.js"
        onload="this.onload=function(){};handleClientLoad()"
        onreadystatechange="if (this.readyState === 'complete') this.onload()">
</script>