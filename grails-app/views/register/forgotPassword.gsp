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
        <li class="underline">
            <g:link mapping="login" class="navbar-link">
                <g:message code="register.head.login"/>
            </g:link>
        </li>
    </ul>
</content>

<content tag="intro">
    <h1><g:message code="forgotPassword.intro.login"/> </h1>
    <p><g:message code="forgotPassword.intro.description"/></p>
</content>

<content tag="mainContent">

        <h2><g:message code="forgotPassword.form.title"/> </h2>
        <formUtil:validateForm bean="${command}" form="remember-pass"/>
        <g:form mapping="resetPassword" name="remember-pass" role="form">
            <div class="form-group">
                <formUtil:input command="${command}" field="email" required="true" labelCssClass="sr-only"/>
            </div>
            <div class="form-group clearfix">
                <input type="submit" class="btn btn-lg pull-right" value="${message(code: 'forgotPassword.form.submit')}"/>
            </div>
        </g:form>

    <script>
        $(document).ready(function() {
            $('#email').focus();
        });
    </script>

</content>

<content tag="description">
    <g:render template="registerSocial"/>
</content>