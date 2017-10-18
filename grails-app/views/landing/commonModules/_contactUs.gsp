<r:require modules="contactUsForm,recaptcha"/>
<g:set var="commandRequestDemo" value="${new kuorum.web.commands.customRegister.RequestDemoCommand(sector: sectorDefault) }"/>
<div class="section-header">
    <g:if test="${msgPrefix=='footerContactUs'}"></g:if>
    <g:else><h1><g:message code="${msgPrefix}.contactUs.title"/></h1></g:else>
    <h3 class="hidden-xs"><g:message code="${msgPrefix}.contactUs.subtitle"/></h3>
</div>
<div class="section-body col-md-10 col-md-offset-1">
    <sec:ifNotLoggedIn>
        <formUtil:validateForm bean="${commandRequestDemo}" form="request-demo-form"/>
        <g:form mapping="requestADemo" autocomplete="off" method="post" name="request-demo-form" class="" role="form" novalidate="novalidate">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${commandRequestDemo}"
                            field="name"
                            labelCssClass="left"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${commandRequestDemo}"
                            field="surname"
                            labelCssClass="left"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${commandRequestDemo}"
                            field="email"
                            labelCssClass="left"
                            type="email"
                            showLabel="true"
                            required="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${commandRequestDemo}"
                            field="phone"
                            labelCssClass="left"
                            type="phone"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${commandRequestDemo}"
                            field="enterprise"
                            labelCssClass="left"
                            showLabel="true"
                            required="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:selectEnum
                            command="${commandRequestDemo}"
                            field="sector"
                            labelCssClass="left"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>
            </fieldset>
            <fieldset class="row form-group">
                <div class="form-group col-md-12">
                    <formUtil:textArea
                        command="${commandRequestDemo}"
                        field="comment"
                        placeholder=" "
                        labelCssClass="left"
                        showLabel="true"
                        showCharCounter="false"
                        required="true"/>
                    </div>
            </fieldset>
            <fieldset class="form-group text-center">
                <button id="contact-us-form-id"
                        data-recaptcha=""
                        data-callback="contactUsCallback"
                        class="btn btn-orange btn-lg g-recaptcha"><g:message code="${msgPrefix}.contactUs.submit"/>
                </button>
            </fieldset>
            <g:render template="/landing/commonModules/requestStatus"/>
        </g:form>
    </sec:ifNotLoggedIn>
</div>

<script type="text/javascript">
    $(function(){
        $('#contact-us-form-id').on('click', function (e) {
            e.preventDefault()
            $('fieldset.email-sent .in-progress').removeClass('hidden');
            var dataRecaptcha = $(this).attr('data-recaptcha');
            recaptchaContactUs(dataRecaptcha)
        });
    });

    function recaptchaContactUs(dataRecaptcha){
        grecaptcha.execute(dataRecaptcha);
    }

    function contactUsCallback(){
        var $form = $('#request-demo-form');
        var dataRecaptcha = $('#contact-us-form-id').attr('data-recaptcha');
        if ($form.valid()){
            var url = $form.attr("action")
            $.ajax({
                url:url,
                data:$form.serializeArray(),
                success:function(data){
                    if(data){
                        display.success(data);

                        $('fieldset.email-sent .error').addClass("hidden");
                        $('fieldset.email-sent .in-progress').addClass('hidden');
                        $('fieldset.email-sent .sent').removeClass("hidden");
                    }
                    else{
                        $('fieldset.email-sent .in-progress').addClass('hidden');
                        $('fieldset.email-sent .error').removeClass("hidden");
                    }
                }
            })
        }
        else{
            grecaptcha.reset(dataRecaptcha);
            $('fieldset.email-sent .in-progress').addClass('hidden');
        }
    }
</script>