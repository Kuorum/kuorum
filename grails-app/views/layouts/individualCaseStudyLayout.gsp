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
                        <g:link mapping="landingCasesStudy" class="breadcrumb">
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
                                        <g:render template="/layouts/recaptchaForm" model="[formName: 'request-case-study']"/>
                                        <button type="submit"
                                                data-sitekey="${_googleCaptchaKey}"
                                                data-size="invisible"
                                                data-callback='onSubmit'
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
    </body>
</g:applyLayout>