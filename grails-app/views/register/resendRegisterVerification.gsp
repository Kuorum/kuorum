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
            <g:link mapping="tourStart" class="navbar-link">
                <g:message code="register.head.tour"/>
            </g:link>
        </li>
    </ul>
</content>
<content tag="intro">
    <h1><g:message code="register.locked.title"/> </h1>
    <p>o <g:link mapping="login"><g:message code="register.intro.login"/> </g:link></p>
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
        <p>Ahora te contamos lo que va a pasar</p>
        <p>Lorem ipsum dolor sit amet, labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea consectetur adipisicing elit, sed do eiusmod tempor incididunt ut commodo consequat.</p>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        <img src="${g.resource(dir:'images', file: 'screen.png')}" class="screen">
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
        <a href="#" class="btn btn-lg">Iniciar tour</a>
    </div>
</content>


<content tag="description">
    <h3><g:message code="register.locked.description.title"/></h3>
    <p><g:message code="register.locked.description.p1"/></p>
    <p><g:message code="register.locked.description.p2"/></p>
    <p><g:message code="register.locked.description.p3"/></p>
</content>