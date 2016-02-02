<section id="main" role="main" class="landing mainEditors clearfix">
    <div class="full-video">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1><g:message code="landingEditors.videoAndRegister.title"/></h1>
                    <h2><g:message code="landingEditors.videoAndRegister.subTitle"/> </h2>
                    <a href="#ipdb-description" class="btn btn-white anchor-link"><g:message code="landingEditors.videoAndRegister.whatsIPDB" encodeAs="RAW"/> </a>
                    <formUtil:validateForm bean="${command}" form="joinEditors"/>
                    <g:form mapping="register" autocomplete="off" method="post" name="joinEditors" class="form-inline" role="form" novalidate="novalidate">
                        <input type="hidden" name="editor" value="true"/>
                        <fieldset>
                            <div class="form-group">
                                <formUtil:input
                                        command="${command}"
                                        field="name"
                                        labelCssClass="sr-only"
                                        showLabel="true"
                                        showCharCounter="false"
                                        required="true"/>
                            </div>

                            <div class="form-group">
                                <formUtil:input
                                        command="${command}"
                                        field="email"
                                        type="email"
                                        showLabel="true"
                                        labelCssClass="sr-only"
                                        required="true"/>
                            </div>
                            <!-- para el botón, lo que prefieras, <button> o <input>-->
                            <button type="submit" class="btn"><g:message code="landingEditors.videoAndRegister.form.submit"/> </button>
                            <!--                                <input type="submit" class="btn" value="Start your free trial">-->
                        </fieldset>
                        <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerTermsUse')]"/></p>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</section>