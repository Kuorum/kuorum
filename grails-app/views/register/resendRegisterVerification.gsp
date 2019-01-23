<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Resend verification </title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="intro">
    <g:message code="register.locked.title"/>
</content>



<content tag="mainContent">
    <div class="max600 text-center">
        <g:form mapping="registerResendMail" name="sign" role="form" method="POST" autocomplete="off">
            <input type="hidden" name="email" id="email" value="${params.email}"/>
            <div class="form-group">
                <input type="submit" class="btn btn-lg" value="${g.message(code:'register.locked.form.submit')}"/>
                <p><g:message code="register.resend.subtitle"/></p>
            </div>
        </g:form>
        <img src="${g.resource(dir:'images', file: 'screen.png')}" class="screen">
    </div>
</content>
