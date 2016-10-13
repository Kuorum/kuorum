<section id="main" role="main" class="landing politicians clearfix">
    <div class="full-video">
        <video autoplay loop poster="${resource(dir: 'images', file: 'background.png')}" id="bgvid">
            <source src="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landing.webm" type="video/webm">
            <source src="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landing.mp4" type="video/mp4">
        </video>

        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1><g:message code="landingPage.videoAndRegister.title"/></h1>
                    <h2><g:message code="landingPage.videoAndRegister.subtitle"/></h2>

                    <a href="#saveTime" class="btn btn-white smooth"><g:message code="landingPage.videoAndRegister.howItWorks"/></a>
                    <sec:ifLoggedIn>
                        <form action="${createLink(mapping: 'landingPrices')}" method="GET">
                        <fieldset>
                            <!-- para el botón, lo que prefieras, <button> o <input>-->
                            <button type="submit" class="btn btn-primary"><g:message code="landingPage.videoAndRegister.startFreeTrial"/></button>
                            <!--                                <input type="submit" class="btn" value="Start your free trial">-->
                        </fieldset>
                        </form>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        <formUtil:validateForm bean="${command}" form="freeTrial"/>
                        <g:form mapping="register" autocomplete="off" method="post" name="freeTrial" class="form-inline" role="form" novalidate="novalidate">
                            <fieldset>
                                <div class="form-group">
                                    <formUtil:input
                                            command="${command}"
                                            field="name"
                                            labelCssClass="sr-only"
                                            showCharCounter="false"
                                            required="true"/>
                                </div>

                                <div class="form-group">
                                    <formUtil:input
                                            command="${command}"
                                            field="email"
                                            type="email"
                                            labelCssClass="sr-only"
                                            required="true"/>
                                </div>
                                <!-- para el botón, lo que prefieras, <button> o <input>-->
                                <button type="submit" class="btn"><g:message code="landingPage.videoAndRegister.startFreeTrial"/></button>
                                <!--                                <input type="submit" class="btn" value="Start your free trial">-->
                            </fieldset>
                            <p><g:message code="register.conditions" args="[g.createLink(mapping: 'footerTermsUse')]"/></p>
                        </g:form>
                    </sec:ifNotLoggedIn>
                </div>
            </div>
        </div>
    </div>
</section>
