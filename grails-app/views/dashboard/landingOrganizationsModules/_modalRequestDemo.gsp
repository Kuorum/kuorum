

<!-- MODAL CONTACT -->
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
                <g:form mapping="requestADemo" id="request-demo-modal-form" role="form" method="post" name="request-demo-modal-form">
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
                            <input id="request-demo-modal-form-submit-id" type="submit" class="btn btn-lg" value="${g.message(code:'landingCorporationsBrands.carousel.login.submit')}">
                        </div>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </div>
<!-- fin modal -->

<script type="text/javascript">
    $(function(){
//        $("#landing-register button[type=submit]").on("click", function(e){
//            e.preventDefault();
//            openModalRequestDemo()
//        });

        $(".btn-sign-up").on("click", function(e){
            e.preventDefault();
            openModalRequestDemo()
        });

        $("#request-demo-modal-form-submit-id").on("click", function(e){
            e.preventDefault();
            var $form = $(this).parents("form");
            if ($form.valid()){
                var url = $form.attr("action")
                $.ajax({
                    url:url,
                    data:$form.serializeArray(),
                    success:function(data){
                        display.success(data);
                        $("#request-demo-modal").modal("hide");
                    }
                })
            }
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
    })
</script>