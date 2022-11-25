<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login" args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/> </title>
    <meta name="layout" content="centerColumn1Layout">
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
    <div class="box-ppal auto-width-center">
        <form id="login" role="form" action='${postUrl}' method='POST' class="login">
            <div class="form-group">
                <label for="j_username" class="">${g.message(code: 'login.email.form.email.label')}</label>
                <input type="email" name="j_username" placeholder="${g.message(code: 'login.email.form.email.label')}"
                       class="form-control input-lg" id="j_username" required value="${username}">
                <g:if test="${flash.message}">
                    <span for="titlePost" class="error">${flash.message}</span>
                </g:if>
            </div>

            <div class="form-group clearfix">
                <label for="j_password" class="">${g.message(code: 'login.email.form.password.label')}</label>

                <div class="input-append input-group">

                    <input type="password" placeholder="${g.message(code: 'login.email.form.password.label')}"
                           name="j_password" class="form-control input-lg" id="password" required>
                    <span tabindex="100" class="add-on input-group-addon">
                        <label><input type="checkbox" class="show-hide-pass" name="show-pass-header"
                                      id="show-pass-header"> <g:message code="login.email.form.password.show"/></label>
                    </span>
                </div>
            </div>

            <div class="form-group center clearfix">
                <input type="submit" value="${g.message(code: 'login.email.form.login')}" class="btn btn-lg col-xs-12">

                <div class="col-xs-12 sign-in-option">
                    <g:link mapping="resetPassword">
                        <g:message code="login.email.form.password.forgotten"/>
                    </g:link>
                </div>
            </div>
        </form>
        <script type='text/javascript'>
            (function () {
                document.forms['login'].elements['j_username'].focus();
            })();
        </script>
    </div>

    <div class="login-social center">
        <g:render template="/register/registerSocial"/>
    </div>

    <div class="center">
        <form>
            <div class="form-group center clearfix">
                <div class="col-xs-12 sign-in-option">
                    <g:message code="head.noLogged.register" args="[g.createLink(mapping: 'register'), '']"/>
                </div>
            </div>
        </form>
    </div>
</content>

