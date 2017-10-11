<div class="section-body">
    <div class="breadcrumbs">
        <g:link mapping="landingServices" class="breadcrumb">
            INICIO
        </g:link>
        <g:link mapping="landingCasesStudy" class="breadcrumb">
            CASOS DE ÉXITO
        </g:link>
    </div>
    <div class="row col-lg-10 col-md-offset-1">
        <h1><g:message code="individualCaseStudy.body.case1.title"/> </h1>
        <p><g:message code="individualCaseStudy.body.case1.content.text"/> </p>
    </div>
    <div class="row col-lg-10 col-md-offset-1">
        <formUtil:validateForm bean="${command}" form="request-case-study"/>
        <form action="#" class="request-case-study form-inline dark" id="request-case-study" name="request-case-study">
            <fieldset>
                <div class="form-group col-lg-4">
                    <formUtil:input
                            command="${command}"
                            field="name"
                            labelCssClass="sr-only"
                            showLabel="true"
                            showCharCounter="false"
                            placeHolder="${g.message(code: 'individualCaseStudy.body.downloadCase.name')}"
                            required="true"/>
                </div>
                <div class="form-group col-lg-4">
                    <formUtil:input
                            command="${command}"
                            field="email"
                            type="email"
                            showLabel="true"
                            labelCssClass="sr-only"
                            placeHolder="${g.message(code: "individualCaseStudy.body.downloadCase.email")}"
                            required="true"/>
                </div>
                <g:render template="/layouts/recaptchaForm"/>
                <button type="submit"
                        data-sitekey="${siteKey}"
                        data-size="invisible"
                        data-callback='onSubmit'
                        class="btn btn-orange btn-lg col-lg-4 g-recaptcha"><g:message code="${msgPrefix}.body.downloadCase.submit"/>
                </button>
            </fieldset>
        </form>
    </div>
</div>