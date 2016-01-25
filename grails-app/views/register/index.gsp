<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
</head>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="sign" autocomplete="off"/>
    <g:form mapping="register" name="sign" role="form" method="POST" autocomplete="off" class="login">
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="name"
                    cssClass="form-control input-lg"
                    labelCssClass="sr-only"
                    showCharCounter="false"
                    required="true"/>
        </div>
        <div class="form-group">
            <formUtil:input
                    command="${command}"
                    field="email"
                    type="email"
                    id="email"
                    cssClass="form-control input-lg"
                    labelCssClass="sr-only"
                    required="true"/>
        </div>
        <div class="form-group">
            <input type="submit" class="btn btn-lg" value="${g.message(code:'register.email.form.submit')}"> <p class="cancel">o  <g:link mapping="login"><g:message code="register.email.form.alreadyRegister"/></g:link></p>
        </div>
        <div class="form-group">
            <g:message code="register.conditions" args="[g.createLink(mapping: 'footerPrivacyPolicy')]" encodeAs="raw"/>
        </div>
    </g:form>
    <script>
        $(document).ready(function() {
            $('input[name=name]').focus();
        });
    </script>
    <g:render template="/register/registerSocial"/>
</content>


<content tag="description">
    <h1><g:message code="login.description.title"/></h1>
    <h2><g:message code="login.description.p1"/></h2>
</content>
