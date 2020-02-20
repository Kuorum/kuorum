<%@ page import="springSecurity.KuorumRegisterCommand" %>
<%@ page import="kuorum.web.commands.customRegister.ForgotUserPasswordCommand" %>

<!-- Modal registro/login -->
<div class="modal fade modal-register" id="registro" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only">Cerrar</span></button>
                <h4>${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</h4>
                <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>
            </div>
            <div class="modal-body">
                <!-- Formulario de Entrar -->
                <g:include controller="login" action="loginForm"/>
                <g:include controller="register" action="forgotPasswordForm"/>
                <!-- Formulario de Registro -->
                <g:render template="/layouts/registerForm" model="[registerCommand: new springSecurity.KuorumRegisterCommand(), formId:'signup-modal']"/>
                <g:render template="/register/registerSocial"/>
            </div>
        </div>
    </div>
</div>