<sec:ifNotLoggedIn>
    <formUtil:validateForm bean="${command}" form="sign"/>
    <g:form mapping="joinDomain" autocomplete="off" method="post" name="sign" class="form-inline dark"
            role="form">
        <fieldset aria-live="polite">
            <div class="form-group col-sm-offset-2 col-sm-4">
                <formUtil:input
                        command="${command}"
                        field="joinCode"
                        labelCssClass="sr-only"
                        showLabel="true"
                        showCharCounter="true"
                        required="true"/>
            </div>

            <div class="form-group col-sm-offset-1 col-sm-3 submit-button">
                <button type="submit"
                        id="join-code-submit"
                        data-sitekey="${siteKey}"
                        data-size="invisible"
                        data-callback='joinRegisterCallback'
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
</sec:ifNotLoggedIn>