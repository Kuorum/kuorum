<!-- Launchs the header to clickable to open the modal -->
<script>
    $(function(){
        display.blockAdvise("${g.message(code:'modal.election.main.header')}", function(e){
            e.preventDefault();
            $("#causes-modal").modal("show");
        })
    });
</script>

<!-- Modal registro/login -->
<div class="modal fade in" id="causes-modal" tabindex="-1" role="dialog" aria-labelledby="registroLoginUsuario" aria-hidden="false">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="${g.message(code:'default.close')}"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">${g.message(code:'default.close')}</span></button>
                <h4 class="sr-only" id="registroLoginUsuario">${g.message(code:'modal.election.main.header')}</h4>
            </div>
            <div class="modal-body text-center hidden-xs">
                <h1><g:message code="modal.election.header.title"/> </h1>
                <h3><g:message code="modal.election.header.subtitle"/></h3>
                <div class='row causes-icon'>

                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <img  src="${resource(dir: 'images', file: 'business.png')}" alt="">
                        <h4>#Recuperacion Justa</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <img  src="${resource(dir: 'images', file: 'lightbulb.svg')}" alt="">
                        <h4>#Educación & Innovación</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <img  src="${resource(dir: 'images', file: 'democracy.svg')}" alt="">
                        <h4>#Democracia</h4>
                    </div>
                </div>
                <div class="row causes-icon">
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <span class="icon-JUSTICE"></span>
                        <h4>#Igualdad</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <span class='icon-CONSTITUTIONAL'></span>
                        <h4>#Reforma Constitucional</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <span class="icon-FOREIGN_AFFAIRS"></span>
                        <h4>#Politica Exterior</h4>
                    </div>
                </div>
                <!-- email subscription form -->
                <form action="https://kuorum.org/j_spring_security_check" method="post" name="login-header" id="login-modal" role="form" class="login">
                    <h3><g:message code="modal.election.hook"/></h3>
                    <div class="form-group">
                        <input type="email" name="email" class="form-control input-lg center-block" id="email" required="" placeholder="name@example.com" value="" aria-required="true">
                    </div>
                    <div class="form-group">
                        <input type="submit" class="btn" value="Submit my choice!">
                    </div>
                    <div class="form-group">
                        You are accepting the <a href="https://kuorum.org/kuorum/politica-privacidad" target="_blank">service conditions</a>
                    </div>
                </form>
                <script type="text/javascript">
                    $(function (){
                        $("#sign-modal").validate({
                            errorClass:'error',
                            errorPlacement: function(error, element) {
                                if(element.attr('id') == 'deadline')
                                    error.appendTo(element.parent("div").parent("div"));
                                else if(element.attr('id') == 'JUSTICE')
                                    error.appendTo(element.parent("div").parent("div").parent("div").parent("div"));
                                else
                                    error.insertAfter(element);
                            },
                            errorElement:'span',
                            rules: {'name':{required: true ,maxlength: 17},'email':{required: true ,email: true}} ,  messages: {'name':{required: 'We need a name to address you',maxlength: 'The username must have a maximum of 17 characters'},'email':{required: 'We need an email to communicate with you',email: 'Wrong email format'}}
                        });
                    });
                </script>
            </div>
            <div class="modal-body text-center hidden-sm hidden-md hidden-lg">
                <h1>Let Spain hear your voice!</h1>
                <h3>Pick the top 3 causes you support:</h3>
                <div class='row causes-icon'>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <h4>#Recuperacion Justa</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <h4>#Educación & Innovación</h4>
                    </div>
                </div>
                <div class='row causes-icon'>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <h4>#Democracia</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <h4>#Igualdad</h4>
                    </div>
                </div>
                <div class='row causes-icon'>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <h4>#Reforma Constitucional</h4>
                    </div>
                    <div class="causes-checkbox" role="checkbox" id="iCheck-Recovery" aria-checked="false" aria-disabled="false">
                        <input type="checkbox" value="kk"/>
                        <h4>#Politica Exterior</h4>
                    </div>
                </div>
                <!-- email subscription form -->
                <form action="https://kuorum.org/j_spring_security_check" method="post" name="login-header" id="login-modal" role="form" class="login">
                    <h3>Leave us your email if you want to see the secret PSOE video!</h3>
                    <div class="form-group">
                        <input type="email" name="email" class="form-control input-lg center-block" id="email" required="" placeholder="name@example.com" value="" aria-required="true">
                    </div>
                    <div class="form-group">
                        <input type="submit" class="btn btn-sm" value="Submit my choice!">
                    </div>
                    <div class="form-group">
                        You are accepting the <a href="https://kuorum.org/kuorum/politica-privacidad" target="_blank">service conditions</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- fin modal -->