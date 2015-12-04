<!-- MODAL CONTACT -->
<div class="modal fade in" id="contact-modal" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="false">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span
                        class="sr-only">Cerrar</span></button>
                <h4 class="sr-only" id="registroLoginUsuario">Registro / Login usuario</h4>
                <h5>Message to Barack Obama</h5>
            </div>

            <div class="modal-body">
                <!-- email subscription form -->
                <form action="https://kuorum.org/j_spring_security_check" method="post" name="login-header" id="login-modal" role="form" class="login">
                    <div class="form-group">
                        <label for="message-text" class="control-label">Which cause is this message related to?</label>
                        <select class="form-control">
                            <g:each in="${politician.tags}" var="tag">
                                <option>${tag}</option>
                            </g:each>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="message-text" class="control-label">Message:</label>
                        <textarea class="form-control" id="message-text" placeholder="Type your message here..."></textarea>
                    </div>

                    <div class="form-group">
                        <label for="recipient-name" class="control-label">Leave us your email to get updates on this message!</label>
                        <input type="text" name="name" class="form-control counted " id="name" required="" placeholder="Tell us your name" value="" aria-required="true">
                    </div>

                    <div class="form-group">
                        <input type="email" name="email" class="form-control center-block" id="email" required="" placeholder="Introduce your email" value=""
                               aria-required="true">
                    </div>

                    <div class="form-group">
                        <input type="submit" class="btn" value="Send Message!">
                    </div>

                    <div class="form-group">
                        You are accepting the <a href="https://kuorum.org/kuorum/politica-privacidad" target="_blank">service conditions</a>
                    </div>
                </form>
                <script type="text/javascript">
                    $(function () {
                        $("#sign-modal").validate({
                            errorClass: 'error',
                            errorPlacement: function (error, element) {
                                if (element.attr('id') == 'deadline')
                                    error.appendTo(element.parent("div").parent("div"));
                                else if (element.attr('id') == 'JUSTICE')
                                    error.appendTo(element.parent("div").parent("div").parent("div").parent("div"));
                                else
                                    error.insertAfter(element);
                            },
                            errorElement: 'span',
                            rules: {'name': {required: true, maxlength: 17}, 'email': {required: true, email: true}}, messages: {'name': {required: 'We need a name to address you', maxlength: 'The username must have a maximum of 17 characters'}, 'email': {required: 'We need an email to communicate with you', email: 'Wrong email format'}}
                        });
                    });
                </script>
            </div>
        </div>
    </div>
</div>
<!-- fin modal -->