<!-- MODAL CONTACT -->
<g:set var="command" value="${new kuorum.web.commands.customRegister.ContactRegister() }"/>
<div class="modal fade in" id="contact-modal" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="false">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span
                        class="sr-only">Cerrar</span></button>
                <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>
                <h5>Message to Barack Obamasss</h5>
            </div>

            <div class="modal-body">
                <!-- email subscription form -->
                <formUtil:validateForm form="contact-modal-form" bean="${command}"/>
                <g:form mapping="campaignPoll" id="contact-modal-form" role="form" method="post" name="contact-modal-form">
                    <div class="form-group">
                        <label for="cause" class="control-label">Which cause is this message related to?</label>
                        <select class="form-control" name="cause" id="cause">
                            <g:each in="${politician.tags}" var="tag">
                                <option>${tag}</option>
                            </g:each>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="message" class="control-label">Message:</label>
                        <formUtil:textArea field="message" rows="10" command="${command}" texteditor="true" />
                    </div>

                    <div class="form-group">
                        <formUtil:input field="name" command="${command}" showLabel="true"/>
                    </div>

                    <div class="form-group">
                        <formUtil:input field="email" command="${command}"/>
                    </div>

                    <div class="form-group">
                        <input type="submit" class="btn" value="Send Message!">
                    </div>

                    <div class="form-group">
                        You are accepting the <a href="https://kuorum.org/kuorum/politica-privacidad" target="_blank">service conditions</a>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</div>
<!-- fin modal -->