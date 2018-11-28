<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="register1ColumnLayout">
    <parameter name="extraHeadCss" value="landing"/>
</head>

<content tag="title">
    <g:if test="${titleMsg}">
        ${titleMsg}
    </g:if>
    <g:else>
        <g:message code="register.head.login"/>
    </g:else>
</content>
<content tag="mainContent">
    <form id="login" role="form" action='${postUrl}' method='POST' class="login">
        <div class="form-group">
            <input type="email" name="j_username" placeholder="${g.message(code: 'login.email.form.email.label')}" class="form-control input-lg" id="j_username" required value="${username}">
            <g:if test="${flash.message}">
                <span for="titlePost" class="error">${flash.message}</span>
            </g:if>
        </div>
        <div class="form-group clearfix">
            <div class="input-append input-group">
                <input type="password" placeholder="${g.message(code:'login.email.form.password.label')}" name="j_password" class="form-control input-lg" id="password" required>
                <span tabindex="100" class="add-on input-group-addon">
                    <label><input type="checkbox" class="show-hide-pass" name="show-pass-header" id="show-pass-header"> <g:message code="login.email.form.password.show"/></label>
                </span>
            </div>
        </div>
        <div class="form-group">
            <input type="submit" value="${g.message(code:'login.email.form.login')}" class="btn btn-blue btn-lg">
            <p>
                <g:link mapping="resetPassword">
                    <g:message  code="login.email.form.password.forgotten"/>
                </g:link>
            </p>
            <p>
                <g:message code="head.noLogged.register" args="[g.createLink(mapping: 'register'),'']"/>
            </p>
        </div>
    </form>
     <script type='text/javascript'>
        (function() {
            document.forms['login'].elements['j_username'].focus();
        })();
    </script>
    <g:render template="/register/registerSocial"/>
</content>

