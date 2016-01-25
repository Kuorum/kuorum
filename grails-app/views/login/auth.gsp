<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="register2ColumnsLayout">
</head>

<content tag="mainContent">
    <form id="login" role="form" action='${postUrl}' method='POST' class="login">
        <div class="form-group">
            <input type="email" name="j_username" placeholder="${g.message(code: 'login.email.form.email.label')}" class="form-control input-lg" id="j_username" required>
            <g:if test="${flash.message}">
                <span for="titlePost" class="error">${flash.message}</span>
            </g:if>
        </div>
        <div class="form-group clearfix">
            <div class="input-append input-group">
                <input type="password" placeholder="Contraseña" name="j_password" class="form-control input-lg" id="password" required>
                <span tabindex="100" class="add-on input-group-addon">
                    <label><input type="checkbox" name="show-pass-header" id="show-pass-header"> <g:message code="login.email.form.password.show"/></label>
                </span>
            </div>
        </div>
        <div class="form-group">
            <input type="submit" value="${g.message(code:'login.email.form.login')}" class="btn btn-blue btn-lg"> <p class="cancel"><g:message code="head.noLogged.register_or"/> <g:link mapping="register"><g:message code="head.noLogged.register"/></g:link></p>
        </div>
        <div class="form-group">
            <g:link mapping="resetPassword">
                <g:message  code="login.email.form.password.forgotten"/>
            </g:link>
        </div>
    </form>
     <script type='text/javascript'>
        (function() {
            document.forms['login'].elements['j_username'].focus();
        })();
    </script>
    <g:render template="/register/registerSocial"/>
</content>


<content tag="description">
    <h1><g:message code="login.description.title"/></h1>
    <h2><g:message code="login.description.p1"/></h2>
</content>

