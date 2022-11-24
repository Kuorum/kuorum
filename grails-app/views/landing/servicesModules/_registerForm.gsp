<g:if test="${landingVisibleRoles}">
    <a href="#how-it-works" class="btn btn-lg btn-grey-transparent hidden-xs" data-effect="scroll"><g:message
            code="landingServices.howItWorks.title"/></a>
</g:if>
<sec:ifNotLoggedIn>
    <g:if test="${_domainLoginSettings.showLandingLogin && _domainLoginSettings.providerBasicEmailForm}">
        <formUtil:validateForm bean="${command}" form="sign"/>
        <g:form mapping="register" autocomplete="off" method="post" name="sign" class="form-inline dark" role="form"
                novalidate="novalidate">
            <fieldset aria-live="polite">
                <div class="form-group col-sm-3">
                    <formUtil:input
                            command="${command}"
                            field="name"
                            labelCssClass="sr-only"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>

                <div class="form-group col-sm-3">
                    <formUtil:input
                            command="${command}"
                            field="email"
                            type="email"
                            showLabel="true"
                            labelCssClass="sr-only"
                            required="true"/>
                </div>

                <div class="form-group col-sm-3">
                    <formUtil:input
                            command="${command}"
                            field="password"
                            type="password"
                            showLabel="true"
                            labelCssClass="sr-only"
                            required="true"/>
                </div>

                <div class="form-group col-sm-3 submit-button">
                    <button type="submit"
                            id="register-submit"
                            data-sitekey="${siteKey}"
                            data-size="invisible"
                            data-callback='registerCallback'
                            class="btn btn-lg g-recaptcha"><g:message code="landingPage.register.form.submit"/>
                    </button>
                </div>
            </fieldset>
            <fieldset aria-live="polite">
                <div class="form-group col-xs-12 conditions">
                    <formUtil:checkBox
                            command="${command}"
                            field="conditions"
                            label="${g.message(code: 'register.conditions', args: [g.createLink(mapping: 'footerPrivacyPolicy')], encodeAs: 'raw')}"/>

                </div>
                <r:require modules="recaptcha_register"/>
            </fieldset>
        </g:form>
    </g:if>
</sec:ifNotLoggedIn>