<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="register.resetPassword.head.title"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
</head>

<content tag="headButtons">
    <ul class="nav navbar-nav navbar-right">
        %{--<li class="underline">--}%
        %{--<g:link mapping="footerWhatIsKuorum" class="navbar-link">--}%
        %{--<g:message code="page.title.footer.whatIsKuorum"/>--}%
        %{--</g:link>--}%
        %{--</li>--}%
        <li class="underline">
            <g:link mapping="login" class="navbar-link">
                <g:message code="register.head.login"/>
            </g:link>
        </li>
    </ul>
</content>

<content tag="intro">
    <h1><g:message code="register.resetPassword.head.title"/> </h1>
    <p>o <g:link mapping="login"><g:message code="register.resetPassword.subtitle"/> </g:link></p>
</content>


<content tag="mainContent">
    <g:render template="/register/registerSocial"/>
    <h2><g:message code="register.resetPassword.form.title"/> </h2>
    <formUtil:validateForm bean="${command}" form="sign"/>
    <g:form mapping="resetPasswordChange" name="sign" role="form" method="POST" autocomplete="off">
        <g:hiddenField name='t' value='${token}'/>
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="password"
                    type="password"
                    id="email"
                    cssClass="form-control input-lg"
                    required="true"/>
        </div>
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="password2"
                    type="password"
                    id="email"
                    cssClass="form-control input-lg"
                    helpBlock="${g.message(code:'grails.plugin.springsecurity.ui.ResetPasswordCommand.password2.helpBlock')}"
                    required="true"/>
        </div>
        <div class="form-group">
            <g:link mapping="login" class="cancel">
                <g:message code="register.resetPassword.form.cancel"/>
            </g:link>
            <input type="submit" class="btn btn-lg" value="${g.message(code:'register.resetPassword.form.submit')}"/>
        </div>
    </g:form>
    <script>
        $(document).ready(function() {
            $('#password').focus();
        });
    </script>
</content>


<content tag="description">
    <h3><g:message code="register.resetPassword.description.title"/></h3>
    <p><g:message code="register.resetPassword.description.p1"/></p>
</content>

