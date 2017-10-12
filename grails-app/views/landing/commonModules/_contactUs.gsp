<div class="section-header col-md-10 col-md-offset-1">
    <h1><g:message code="${msgPrefix}.contactUs.title"/></h1>
    <h3 class="hidden-xs"><g:message code="${msgPrefix}.contactUs.subtitle"/></h3>
</div>
<div class="section-body col-md-10 col-md-offset-1">
    <sec:ifNotLoggedIn>
        <formUtil:validateForm bean="${command}" form="landing-register"/>
        <g:form mapping="register" autocomplete="off" method="post" name="landing-register" class="" role="form" novalidate="novalidate">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${command}"
                            field="name"
                            labelCssClass="left"
                            showLabel="true"
                            showCharCounter="false"
                            required="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${command}"
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
                            command="${command}"
                            field="email"
                            labelCssClass="left"
                            type="email"
                            showLabel="true"
                            required="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${command}"
                            field="telephone"
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
                            command="${command}"
                            field="organization"
                            labelCssClass="left"
                            showLabel="true"
                            required="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:input
                            command="${command}"
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
                        command="${command}"
                        field="comment"
                        placeholder=" "
                        labelCssClass="left"
                        showLabel="true"
                        showCharCounter="false"
                        required="true"/>
                    </div>
            </fieldset>
            <fieldset class="form-group text-center">
                <g:render template="/layouts/recaptchaForm"/>
                <button type="submit"
                        data-sitekey="${siteKey}"
                        data-size="invisible"
                        data-callback='onSubmit'
                        class="btn btn-orange btn-lg g-recaptcha"><g:message code="${msgPrefix}.contactUs.submit"/>
                </button>
            </fieldset>
        </g:form>
    </sec:ifNotLoggedIn>
</div>