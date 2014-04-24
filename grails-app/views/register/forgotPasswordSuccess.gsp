<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
</head>

<content tag="headButtons">
    <ul class="nav navbar-nav navbar-right">
        <li class="underline">
            <g:link mapping="footerWhatIsKuorum" class="navbar-link">
                <g:message code="page.title.footer.whatIsKuorum"/>
            </g:link>
        </li>
        <li class="underline">
            <g:link mapping="login" class="navbar-link">
                <g:message code="register.head.login"/>
            </g:link>
        </li>
    </ul>
</content>

<content tag="intro">
    <h1><g:message code="forgotPasswordSuccess.intro.login"/> </h1>
    <p><g:message code="forgotPasswordSuccess.intro.description"/></p>
</content>

<content tag="mainContent">
    <p class="lead"><g:message code="forgotPasswordSuccess.content"/> </p>
    <p class="lead cl-primary">¡Gracias!</p>
</content>

<content tag="description">
    <g:render template="registerSocial"/>
</content>