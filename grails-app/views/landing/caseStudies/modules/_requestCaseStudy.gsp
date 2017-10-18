
<r:require modules="recaptcha_caseStudyRequest"/>
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
</div>