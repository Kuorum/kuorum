<%@ page import="springSecurity.KuorumRegisterCommand" %>
<div class="modal fade modal-register" id="registroDebate" tabindex="-1" role="dialog" aria-labelledby="registroDebateTitle" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span></button>
                <h4 class="sr-only" id="registroDebateTitle">Registro / Login usuario</h4>
            </div>
            <div class="modal-body">
                <!-- Formulario de Entrar -->
                <g:render template="/layouts/loginForm" model="[modalId:'registroDebate']"/>
                <!-- Formulario de Registro -->
                <g:render template="/layouts/registerForm" model="[registerCommand: new springSecurity.KuorumRegisterCommand(), formId:'debate-modal']"/>
            </div>
        </div>
    </div>
</div>