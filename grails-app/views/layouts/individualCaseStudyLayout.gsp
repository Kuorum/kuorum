<g:applyLayout name="main">

    <head>
        <title><g:layoutTitle/></title>
        <g:layoutHead/>
        <parameter name="bodyCss" value="landing-2"/>
    </head>

    <body>
    <g:render template="/layouts/head" model="[extraHeadCss:g.pageProperty(name:'page.extraHeadCss')]"/>

    <div class="row main landing">
        <section id="main" role="main" class="landing clearfix">
            <g:pageProperty name="page.main"/>
        </section>
    </div>
    <div class="row landing-section-light">
        <div class="container">
            <section id="cases-study-body">
                <div class="section-body">
                    <div class="breadcrumbs">
                        <g:link mapping="landingServices" class="breadcrumb">
                            <g:message code="individualCaseStudy.breadcrumb.home"/>
                        </g:link>
                        >
                        <g:link mapping="landingCaseStudy" class="breadcrumb">
                            <g:message code="individualCaseStudy.breadcrumb.casesStudy"/>
                        </g:link>
                    </div>
                    <div class="content col-md-10 col-md-offset-1">
                        <div class="article">
                            <g:pageProperty name="page.caseStudyBody"/>
                        </div>
                        <div class="request-case-study">
                            <g:set var="commandRequestCaseStudy" value="${new kuorum.web.commands.customRegister.RequestCaseStudyCommand()}"/>
                            <formUtil:validateForm bean="${commandRequestCaseStudy}" form="request-case-study"/>
                            <g:form mapping="registerPressKit" class="request-case-study form-inline dark" name="request-case-study">
                                <input type="hidden" name="caseStudyId" value="${g.pageProperty(name:'page.caseStudyId')}"/>
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
                                        <div id="recaptcha-case-study-id"></div>
                                        <button id="request-case-study-id"
                                                class="btn btn-sign-up btn-orange btn-lg col-lg-4 g-recaptcha"><g:message code="individualCaseStudy.body.downloadCase.submit"/>
                                        </button>
                                    </div>
                                </fieldset>
                            </g:form>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
    <div class="row landing-section-dark">
        <div class="container">
            <section id="cases-study-grid">
                <g:pageProperty name="page.casesStudyGrid"/>
            </section>
        </div>
    </div>
    <g:render template="/layouts/footer/footer"/>
    </div><!-- .container-fluid -->

    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script type="text/javascript">
        $(function(){
            $('#request-case-study-id').on('click', function (e) {
                e.preventDefault()
                recaptchaCaseStudyRender()
            });
        });

        var requestCaseStudyRecaptcha;
        function recaptchaCaseStudyRender(){
            if (requestCaseStudyRecaptcha == undefined ){
                requestCaseStudyRecaptcha = grecaptcha.render('recaptcha-case-study-id', {
                    'sitekey' : '${_googleCaptchaKey}',
                    'size' : 'invisible',
                    'callback' : caseStudyCallback
                });
            }
//            grecaptcha.reset(requestCaseStudyRecaptcha);
            grecaptcha.execute(requestCaseStudyRecaptcha);
        }

        function caseStudyCallback(){
            var $form = $('#request-case-study');
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
                grecaptcha.reset(requestCaseStudyRecaptcha);
            }
        }
    </script>
    </body>
</g:applyLayout>