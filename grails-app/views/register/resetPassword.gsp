<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="register.resetPassword.head.title"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="mainContent">
    %{--<g:render template="/register/registerSocial"/>--}%
    %{--<h2><g:message code="register.resetPassword.form.title"/> </h2>--}%
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
                    required="true"/>
        </div>
        <div class="form-group">
            <a href="${urlCancelResetPassword}" class="cancel">
                <g:message code="register.resetPassword.form.cancel"/>
            </a>
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
    <h1><g:message code="register.resetPassword.description.title"/></h1>
    <h2><g:message code="register.resetPassword.description.p1"/></h2>
</content>

