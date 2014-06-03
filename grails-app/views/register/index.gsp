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
    <h1><g:message code="register.intro.register"/> </h1>
    <p>o <g:link mapping="login"><g:message code="register.intro.login"/> </g:link></p>
</content>



<content tag="mainContent">
    <g:render template="/register/registerSocial"/>
    <h2><g:message code="register.email.label"/> </h2>
    <formUtil:validateForm bean="${command}" form="sign"/>
    <g:form mapping="register" name="sign" role="form" method="POST" autocomplete="off">
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="name"
                    cssClass="form-control input-lg"
                    required="true"/>
        </div>
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="email"
                    type="email"
                    id="email"
                    cssClass="form-control input-lg"
                    helpBlock="${g.message(code:'register.email.form.email.why')}"
                    required="true"/>
        </div>
        <div class="form-group clearfix">
            <label for="password"><g:message code="springSecurity.KuorumRegisterCommand.password.label"/></label>
            <label class="checkbox-inline pull-right">
                <input type="checkbox" id="show-pass" value="mostrar"><g:message code="login.email.form.password.show"/>
            </label>
            <input type="password" name="password" class="form-control input-lg ${hasErrors(bean: command, field: 'password', 'error')}" id="password" required>
            <g:if test="${hasErrors(bean: command, field: 'password', 'error')}">
                <span for="password" class="error">${g.fieldError(bean: command, field: 'password')}</span>
            </g:if>
        </div>
        <div class="form-group">
            <label class="checkbox-inline">
                <input type="checkbox" name="conditions" id="accept" value="true" class="${hasErrors(bean: command, field: 'conditions', 'error')}">
                <g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>

                <g:if test="${hasErrors(bean: command, field: 'conditions', 'error')}">
                    <span for="password" class="error">${g.fieldError(bean: command, field: 'conditions')}</span>
                </g:if>
            </label>
        </div>
        <div class="form-group">
            <g:link mapping="login" class="cancel">
                <g:message code="register.email.form.alreadyRegister"/>
            </g:link>
            <input type="submit" class="btn btn-lg" value="${g.message(code:'register.email.form.submit')}"/>
        </div>
    </g:form>
    <script>
        $(document).ready(function() {
            $('input[name=name]').focus();
        });
    </script>
</content>


<content tag="description">
    <h3><g:message code="login.description.title"/></h3>
    <p><g:message code="login.description.p1"/> </p>
    <p><g:message code="login.description.p2"/> </p>
    <p><g:message code="login.description.p3"/> </p>
</content>
