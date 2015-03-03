%{--<formUtil:validateForm bean="${registerCommand}" form="sign-header" autocomplete="off"/>--}%
<g:form mapping="register" autocomplete="off" method="post" name="${formId}" class="login" role="form" novalidate="novalidate">
%{--<g:set var="registerLink" value="${g.createLink(mapping: 'register')}"/>--}%
%{--<form action="${registerLink}" method="post" name="sign-header" id="sign-header" class="login" role="form">--}%
    <div class="form-group">
        <formUtil:input
                command="${registerCommand}"
                field="name"
                cssClass="form-control input-lg"
                labelCssClass="sr-only"
                showCharCounter="false"
                required="true"/>
        %{--<label for="name-header" class="sr-only">Dinos tu nombre o el de tu organización</label>--}%
        %{--<input type="text" name="name-header" class="form-control input-lg" id="name-header" required placeholder="Dinos tu nombre o el de tu organización" aria-required="true">--}%
    </div>

    <div class="form-group">
        <formUtil:input
                command="${registerCommand}"
                field="email"
                type="email"
                id="email"
                cssClass="form-control input-lg"
                labelCssClass="sr-only"
                required="true"/>
        %{--<label for="email-header" class="sr-only">Introduce tu email</label>--}%
        %{--<input type="email" name="email-header" class="form-control input-lg" id="email-header" required placeholder="Introduce tu email" aria-required="true">--}%
    </div>
    <div class="form-group">
        <input type="submit" class="btn btn-lg" value="Regístrate"> <p class="cancel"><g:message code="springSecurity.KuorumRegisterCommand.email.or"/> <a href="#" class="change-home-login"><g:message code="login.intro.login"/></a></p>
    </div>
    <div class="form-group">
        <g:set var="linkTermsUse" value="${createLink(mapping:'footerTermsUse')}"/>
        <g:message code="register.conditions" args="[linkTermsUse]" encodeAs="raw"/>
    </div>
</g:form>