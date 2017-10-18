

<!-- MODAL CONTACT -->
<r:require modules="recaptcha"/>
<g:set var="commandRequestDemo" value="${new kuorum.web.commands.customRegister.RequestDemoCommand() }"/>
<div class="modal fade in" id="request-demo-modal" tabindex="-1" role="dialog" aria-labelledby="contactModalTitle" aria-hidden="false">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span
                        class="sr-only"><g:message code="default.close"/></span></button>
                <h4 id="contactModalTitle"><g:message code="landingCorporationsBrands.carousel.login.submit"/></h4>
            </div>

            <div class="modal-body">
            <!-- email subscription form -->
                <formUtil:validateForm form="request-demo-modal-form" bean="${commandRequestDemo}"/>
                <g:form mapping="requestADemo" role="form" method="post" name="request-demo-modal-form">
                    <input type="hidden" name="comment" class="" value="Request a demo"/>
                    <input type="hidden" name="sector" value="${kuorum.core.model.ContactSectorType.NONE}"/>
                    <fieldset>
                        <div class="form-group col-sm-6 col-xs-12">
                            <formUtil:input field="name" command="${commandRequestDemo}" showLabel="true" showCharCounter="false"/>
                        </div>
                        <div class="form-group col-sm-6 col-xs-12">
                            <formUtil:input field="email" command="${commandRequestDemo}" showLabel="true"/>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="form-group col-sm-6 col-xs-12">
                            <formUtil:input field="enterprise" command="${commandRequestDemo}" showLabel="true"/>
                        </div>
                        <div class="form-group col-sm-6 col-xs-12">
                            <formUtil:input field="phone" command="${commandRequestDemo}" showLabel="true"/>
                        </div>
                    </fieldset>
                    <fieldset>
                        <div class="form-group col-xs-12 button">
                            <button id="request-demo-modal-form-id"
                                    data-recaptcha=""
                                    data-callback="requestDemoCallback"
                                    class="btn btn-orange btn-lg g-recaptcha"><g:message code="landingCorporationsBrands.carousel.login.submit"/>
                            </button>
                        </div>
                    </fieldset>
                    <g:render template="/landing/commonModules/requestStatus"/>
                </g:form>
            </div>
        </div>
    </div>
</div>
<!-- fin modal -->
%{--<script src="https://www.google.com/recaptcha/api.js" async defer></script>--}%
<script type="text/javascript">
    $(function(){

        $(".btn-open-modal-request-demo").on("click", function(e){
            e.preventDefault();
            openModalRequestDemo()
        });

        function openModalRequestDemo(){
            var $form = $("#landing-register");
            var name = $form.find("input[name=name]").val()
            var email = $form.find("input[name=email]").val()
            var $modalForm = $("#request-demo-modal-form")
            $modalForm.find("input[name=name]").val(name)
            $modalForm.find("input[name=email]").val(email)
            $("#request-demo-modal").modal("show")
        }

        $('#request-demo-modal-form-id').on('click', function (e) {
            e.preventDefault()
            var dataRecaptcha = $(this).attr('data-recaptcha');
            recaptchaModal(dataRecaptcha)
        });
    });

    function recaptchaModal(dataRecaptcha){
        grecaptcha.execute(dataRecaptcha);
    }

    function requestDemoCallback(){
        var $form = $('#request-demo-modal-form');
        var dataRecaptcha = $('#request-demo-modal-form-id').attr('data-recaptcha');
        if ($form.valid()){
            $('fieldset.email-sent .in-progress').removeClass('hidden');
            var url = $form.attr("action")
            $.ajax({
                url:url,
                data:$form.serializeArray(),
                success:function(data){
                    if(data){
                        display.success(data);
                        $("#request-demo-modal").modal("hide");

                        $('fieldset.email-sent .error').addClass("hidden");
                        $('fieldset.email-sent .in-progress').addClass('hidden');
                        $('fieldset.email-sent .sent').removeClass("hidden");
                        setTimeout(function(){
                            $('fieldset.email-sent .sent').addClass("hidden");
                            $("#request-demo-modal").modal("hide");
                        }, 2500);
                    }
                    else{
                        $('fieldset.email-sent .in-progress').addClass('hidden');
                        $('fieldset.email-sent .error').removeClass("hidden");
                    }
                }
            })
        }
        else {
            grecaptcha.reset(dataRecaptcha);
        }
    }
</script>