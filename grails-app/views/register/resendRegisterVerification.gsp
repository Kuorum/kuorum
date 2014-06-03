<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="register1ColumnLayout">
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
    <h1><g:message code="register.locked.title"/> </h1>
    <p><g:message code="register.resend.subtitle"/></p>
</content>



<content tag="mainContent">
    <div class="max600 text-center">
        <g:form mapping="registerResendMail" name="sign" role="form" method="POST" autocomplete="off">
            <input type="hidden" name="email" id="email" value="${params.email}"/>
            <div class="form-group">
                <input type="submit" class="btn btn-lg" value="${g.message(code:'register.locked.form.submit')}"/>
            </div>
        </g:form>
        <script>
            $(document).ready(function() {
                $('input[name=email]').focus();
            });
        </script>
        <p><g:message code="register.resend.top"/></p>
        <img src="${g.resource(dir:'images', file: 'screen.png')}" class="screen">
    </div>
</content>


<content tag="description">
    <h3><g:message code="register.locked.description.title"/></h3>
    <p><g:message code="register.locked.description.p1"/></p>
    <p><g:message code="register.locked.description.p2"/></p>
    <p><g:message code="register.locked.description.p3"/></p>
</content>