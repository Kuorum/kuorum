<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="normalLayout">
</head>



<content tag="mainContent">
    <div id='login'>
        <div class='inner'>
            <div class='fheader'><g:message code="springSecurity.login.header"/></div>

            <g:if test='${flash.message}'>
                <div class='login_message'>${flash.message}</div>
            </g:if>

            <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                <p>
                    <label for='username'><g:message code="springSecurity.login.username.label"/>:</label>
                    <input type='text' class='text_' name='j_username' id='username'/>
                </p>

                <p>
                    <label for='password'><g:message code="springSecurity.login.password.label"/>:</label>
                    <input type='password' class='text_' name='j_password' id='password'/>
                </p>

                <p id="remember_me_holder">
                    <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                    <label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>
                </p>

                <p>
                    <input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}'/>
                </p>
            </form>
        </div>
    </div>
    <script type='text/javascript'>
        <!--
        (function() {
            document.forms['loginForm'].elements['j_username'].focus();
        })();
        // -->
    </script>


    <hr/><hr/><hr/><hr/><hr/>
    Columna C de Login

    <hr/>
    LOGIN FACEBOOK
    <div class="hidden" id="facebookLoginContainer">
        <facebookAuth:connect id="facebookLogin" type="server"/>
    </div>

    <a href="" onclick="$('#facebookLoginContainer a')[0].click(); return false;" class="button_send fb">
        <g:message code="login.startSession.facebook"/>
    </a>
    <facebookAuth:connect />

    <hr/>
    LOGIN GOOGLE
    <oauth:connect provider="google" id="google-connect-link">Google</oauth:connect>

    Logged with google?
    <s2o:ifLoggedInWith provider="google">yes</s2o:ifLoggedInWith>
    <s2o:ifNotLoggedInWith provider="google">no</s2o:ifNotLoggedInWith>
    <hr/>
    <g:link mapping="register" class="button_send new"  elementId="registerPageLink">
        Registrarse
    </g:link>
<br/>
    <g:link mapping="resetPassword" class="button_send new">
        Se me ha olvidado
    </g:link>
</content>


