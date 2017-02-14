<h3><g:message code="landingLeaders.pressKit.title"/> </h3>
<formUtil:validateForm command="springSecurity.ResendVerificationMailCommand" form="pressKit-form"/>
<g:form mapping="registerPressKit" name="pressKit-form" method="POST" class="form-inline dark" role="form">
    <fieldset>
        <div class="form-group col-lg-6">
            <formUtil:input command="${command}" field="email" type="email" cssClass="form-control input-lg"/>
        </div>
        <button type="submit" class="btn btn-blue btn-lg col-lg-6"><g:message code="landingLeaders.pressKit.submit"/></button>
    </fieldset>
</g:form>