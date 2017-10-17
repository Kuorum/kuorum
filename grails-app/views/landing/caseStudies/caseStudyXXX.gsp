<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingLeaders.head.title"/></title>
    <meta name="layout" content="individualCaseStudyLayout">
    <parameter name="extraHeadCss" value="landing"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingLeaders.head.title'),
                      kuorumDescription:g.message(code:'landingLeaders.head.description'),
                      kuorumImage:request.siteUrl +r.resource(dir:'images/landing', file:'leaders-01.jpg')
              ]"/>
</head>

<content tag="breadcrumb">
    <g:link mapping="landingServices" class="breadcrumb">
        <g:message code="landingCaseStudy.page.breadcrumb.home"/>
    </g:link>
    >
    <g:link mapping="landingCaseStudy" class="breadcrumb">
        <g:message code="landingCaseStudy.page.breadcrumb.casesStudy"/>
    </g:link>
</content>

<content tag="main">
    <g:render template="/landing/commonModules/slider" model="[msgPrefix:'caseStudy'+caseStudyId]"/>
    <g:render template="/dashboard/landingOrganizationsModules/modalRequestDemo"/>
</content>

<content tag="caseStudyBody">
    <h1><g:message code="landingCaseStudy.${caseStudyId}.content.title"/> </h1>
    <p><g:message code="landingCaseStudy.${caseStudyId}.content.text"/> </p>

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
                    <div id="recaptcha-case-study-id"></div>
                    <button id="request-case-study-id"
                            class="btn btn-sign-up btn-orange btn-lg col-lg-4 g-recaptcha"><g:message code="landingCaseStudy.page.body.downloadCase.submit"/>
                    </button>
                </div>
            </fieldset>
        </g:form>
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
    </div>
</content>

<content tag="casesStudyGrid">
    <g:render template="/landing/caseStudies/modules/otherCases" model="[otherCases:suggestedCaseStudies, sectionName:'landingCaseStudy']"/>
    <div class="row section-body request-demo">
        <div id="request-demo-btn" class="col-md-4 col-md-offset-4">
            <a href="#" class="btn btn-lg btn-orange btn-sign-up btn-open-modal-request-demo"><g:message code="landingCaseStudy.page.requestDemo"/> </a>
        </div>
    </div>
</content>