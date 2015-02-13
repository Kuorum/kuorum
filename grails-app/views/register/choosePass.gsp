
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
    <g:form name="myForm" mapping="registerPassword" id="formResetPassword">
        <g:hiddenField name="userId" value="${userId}" />
        <g:hiddenField name="token" value="${token}" />
        <g:textField name="password1" placeholder="grails.plugin.springsecurity.ui.ResetPasswordCommand.password.label" />
        <g:textField name="password2"  placeholder="grails.plugin.springsecurity.ui.ResetPasswordCommand.password2.label" />
        <g:submitButton name="Submit" value="Submit" class="lnk" />
    </g:form>
</body>
</html>