<%@ page import="kuorum.web.commands.customRegister.KuorumUserContactMessageCommand" %>
<!-- MODAL CONTACT -->
<g:set var="command" value="${new kuorum.web.commands.customRegister.KuorumUserContactMessageCommand() }"/>
<div class="modal fade in" id="contact-modal" tabindex="-1" role="dialog" aria-labelledby="contactModalTitle" aria-hidden="false">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fal fa-times-circle fa"></span><span
                        class="sr-only"><g:message code="default.close"/></span></button>
                <h4 class="sr-only" id="contactModalTitle"><g:message code="login.head.register"/></h4>
                <h5><g:message code="modal.contact.header" args="[politician.name]"/></h5>
            </div>

            <div class="modal-body">
                <!-- email subscription form -->
                <formUtil:validateForm form="contact-modal-form" bean="${command}"/>
                <g:form mapping="ajaxRegisterContact" id="contact-modal-form" role="form" method="post" name="contact-modal-form">
                    <input type="hidden" name="politician" value="${politician.id}"/>
                    <div class="form-group">
                        <label for="cause" class="control-label"><g:message code="modal.contact.causeSelect"/></label>
                        <select class="form-control" name="cause" id="cause">
                            <g:each in="${causes}" var="cause">
                                <option>${cause.name}</option>
                            </g:each>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="message" class="control-label"><g:message code="modal.contact.message"/></label>
                        <formUtil:textArea field="message" rows="10" command="${command}" texteditor="true" />
                    </div>

                    <div class="form-group button">
                        <input type="submit" class="btn" value="${g.message(code:'modal.contact.send')}">
                    </div>

                </g:form>
            </div>
        </div>
    </div>
</div>
<!-- fin modal -->