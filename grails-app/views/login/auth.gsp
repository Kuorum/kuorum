<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
</head>

<content tag="headButtons">
    <ul class="nav navbar-nav navbar-right">
        %{--<li class="underline">--}%
            %{--<g:link mapping="footerWhatIsKuorum" class="navbar-link">--}%
                %{--<g:message code="page.title.footer.whatIsKuorum"/>--}%
            %{--</g:link>--}%
        %{--</li>--}%
        <li>
            <g:link mapping="register" class="btn">
                <g:message code="login.head.register"/>
            </g:link>
        </li>
    </ul>
</content>

<content tag="intro">
    <h1><g:message code="login.intro.login"/> </h1>
    <p>o <g:link mapping="register"><g:message code="login.intro.register"/> </g:link></p>
</content>


<content tag="mainContent">
    <g:render template="/register/registerSocial"/>
    <h2><g:message code="login.email.label"/> </h2>
    <form id="login" role="form" action='${postUrl}' method='POST'>
        <div class="form-group">
            <label for="j_username"><g:message code="login.email.form.email.label"/></label>
            <input type="email" name="j_username" class="form-control input-lg" id="j_username" required>
            <g:if test="${flash.message}">
                <span for="titlePost" class="error">${flash.message}</span>
            </g:if>
        </div>
        <div class="form-group clearfix">
            <label for="password"><g:message code="login.email.form.password.label"/></label>
            <input type="password" name="j_password" class="form-control input-lg" id="password" required>
            <label class="checkbox-inline pull-left"><input type="checkbox" id="show-pass" value="mostrar"><g:message code="login.email.form.password.show"/> </label>
        </div>
        <div class="form-group">
            <g:link mapping="resetPassword" class="cancel">
                <g:message  code="login.email.form.password.forgotten"/>
            </g:link>
            <input type="submit" class="btn btn-grey btn-lg" value="${g.message(code:'login.email.form.login')}"/>
        </div>
    </form>
     <script type='text/javascript'>
        (function() {
            document.forms['login'].elements['j_username'].focus();
        })();
    </script>

    %{--<div id='login'>--}%
        %{--<div class='inner'>--}%
            %{--<div class='fheader'><g:message code="springSecurity.login.header"/></div>--}%

            %{--<g:if test='${flash.message}'>--}%
                %{--<div class='login_message'>${flash.message}</div>--}%
            %{--</g:if>--}%

            %{--<form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>--}%
                %{--<p>--}%
                    %{--<label for='username'><g:message code="springSecurity.login.username.label"/>:</label>--}%
                    %{--<input type='text' class='text_' name='j_username' id='username'/>--}%
                %{--</p>--}%

                %{--<p>--}%
                    %{--<label for='password'><g:message code="springSecurity.login.password.label"/>:</label>--}%
                    %{--<input type='password' class='text_' name='j_password' id='password'/>--}%
                %{--</p>--}%

                %{--<p id="remember_me_holder">--}%
                    %{--<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>--}%
                    %{--<label for='remember_me'><g:message code="springSecurity.login.remember.me.label"/></label>--}%
                %{--</p>--}%

                %{--<p>--}%
                    %{--<input type='submit' id="submit" value='${message(code: "springSecurity.login.button")}'/>--}%
                %{--</p>--}%
            %{--</form>--}%
        %{--</div>--}%
    %{--</div>--}%


    %{--<div class="hidden" id="facebookLoginContainer">--}%
        %{--<facebookAuth:connect id="facebookLogin" type="server"/>--}%
    %{--</div>--}%

    %{--<hr/>--}%
    %{--LOGIN GOOGLE--}%
    %{--<oauth:connect provider="google" id="google-connect-link">Google</oauth:connect>--}%

    %{--Logged with google?--}%
    %{--<s2o:ifLoggedInWith provider="google">yes</s2o:ifLoggedInWith>--}%
    %{--<s2o:ifNotLoggedInWith provider="google">no</s2o:ifNotLoggedInWith>--}%
    %{--<hr/>--}%
    %{--<g:link mapping="register" class="button_send new"  elementId="registerPageLink">--}%
        %{--Registrarse--}%
    %{--</g:link>--}%
%{--<br/>--}%
    %{--<g:link mapping="resetPassword" class="button_send new">--}%
        %{--Se me ha olvidado--}%
    %{--</g:link>--}%
</content>


<content tag="description">
    <h3><g:message code="login.description.title"/></h3>
    <p><g:message code="login.description.p1"/> </p>
    <p><g:message code="login.description.p2"/> </p>
    <p><g:message code="login.description.p3"/> </p>
</content>

