<r:require modules="contactUsForm"/>
<g:set var="commandRequestDemo" value="${new kuorum.web.commands.customRegister.RequestDemoCommand() }"/>
<div class="section-header col-md-10 col-md-offset-1">
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
                            field="enterpriseSector"
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
                <g:render template="/layouts/recaptchaForm"/>
                <button type="submit"
                        data-sitekey="${_googleCaptchaKey}"
                        data-size="invisible"
                        data-callback='onSubmit'
                        class="btn btn-orange btn-lg g-recaptcha"><g:message code="${msgPrefix}.contactUs.submit"/>
                </button>
            </fieldset>
        </g:form>
    </sec:ifNotLoggedIn>
</div>