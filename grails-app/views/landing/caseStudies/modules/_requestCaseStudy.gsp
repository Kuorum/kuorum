
<r:require modules="recaptcha"/>
<div class="request-case-study">
    <g:set var="commandRequestCaseStudy" value="${new kuorum.web.commands.customRegister.RequestCaseStudyCommand()}"/>
    <formUtil:validateForm bean="${commandRequestCaseStudy}" form="request-case-study"/>
    <g:form mapping="registerPressKit" class="request-case-study form-inline dark" name="request-case-study">
        <input type="hidden" name="caseStudyId" value="${caseStudyId}"/>
        <fieldset>
            <div class="form-group col-lg-4">
                <formUtil:input
                        command="${commandRequestCaseStudy}"
                        field="name"
                        labelCssClass="sr-only"
                        showLabel="true"
                        showCharCounter="false"
                        required="true"/>
            </div>
            <div class="form-group col-lg-4">
                <formUtil:input
                        command="${commandRequestCaseStudy}"
                        field="email"
                        type="email"
                        showLabel="true"
                        labelCssClass="sr-only"
                        required="true"/>
            </div>
            <div class="form-group col-lg-4">
                <button id="request-case-study-id"
                        data-recaptcha=""
                        data-callback="caseStudyCallback"
                        class="btn btn-sign-up btn-orange btn-lg col-lg-4 g-recaptcha"><g:message code="landingCaseStudy.page.body.downloadCase.submit"/>
                </button>
            </div>
        </fieldset>
    </g:form>
    %{--<script src="https://www.google.com/recaptcha/api.js" async defer></script>--}%
    <script type="text/javascript">
        $(function(){
            $('#request-case-study-id').on('click', function (e) {
                e.preventDefault()
                var dataRecaptcha = $(this).attr('data-recaptcha');
                recaptchaCaseStudy(dataRecaptcha)
            });
        });

//        var requestCaseStudyRecaptcha;
        function recaptchaCaseStudy(dataRecaptcha){
            grecaptcha.execute(dataRecaptcha);
        }

        function caseStudyCallback(){
            var $form = $('#request-case-study');
            var dataRecaptcha = $('#request-case-study-id').attr('data-recaptcha');
            if ($form.valid()){
//                var url = $form.attr("action")
//                $.ajax({
//                    url:url,
//                    data:$form.serializeArray(),
//                    success:function(data){
//                        display.success(data);
//                    }
//                })
                $form.submit()
            }else{
                grecaptcha.reset(dataRecaptcha);
            }
        }
    </script>
</div>