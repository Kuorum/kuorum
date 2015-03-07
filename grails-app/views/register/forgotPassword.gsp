<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.register.forgotPassword"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
</head>

<content tag="headButtons">
    <g:include controller="login" action="headAuth"/>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="pass-forget"/>
    <g:form mapping="resetPassword" name="pass-forget" role="form" class="login pass">
        <div class="form-group">
            <formUtil:input command="${command}" field="email" required="true" labelCssClass="sr-only"/>
        </div>
        <div class="form-group">
            <input type="submit" value="${message(code: 'forgotPassword.form.submit')}" class="btn btn-lg">
        </div>
    </g:form>
    <script>
        $(document).ready(function() {
            $('#email').focus();
        });
    </script>

</content>

<content tag="description">
    <h1><g:message code="forgotPassword.form.title"/></h1>
    <h2><g:message code="forgotPassword.form.subTitle"/></h2>
</content>