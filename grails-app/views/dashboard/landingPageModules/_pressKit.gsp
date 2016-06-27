<section role="complementary">
    <div class="row">
        <div class="col-md-5 col-lg-6">
            <h2><g:message code="landingPage.pressKit.title"/> </h2>
        </div>

        <div class="col-md-7 col-lg-6">
            <formUtil:validateForm command="springSecurity.ResendVerificationMailCommand" form="pressKit-form"/>
            <g:form mapping="registerPressKit" name="pressKit-form" method="POST" class="form-inline" role="form">
                <fieldset>
                    <div class="form-group">
                        <formUtil:input command="${command}" field="email" type="email" cssClass="form-control input-lg"/>
                        %{--<label for="email-press" class="sr-only">Email</label>--}%
                        %{--<input type="email" name="email-press" class="form-control input-lg" id="email-press" required placeholder="Email" aria-required="true">--}%
                    </div>
                    <!-- para el botón, lo que prefieras, <button> o <input>-->
                    <button type="submit" class="btn btn-blue btn-lg"><g:message code="landingPage.pressKit.download"/></button>
                    <!--                        <input type="submit" class="btn" value="Download press kit">-->
                </fieldset>
            </g:form>
        </div>
    </div>
</section>