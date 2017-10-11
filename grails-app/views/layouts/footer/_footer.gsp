<footer id="footer" class="row" role="contentinfo">
    <div class="container-fluid">
        <section class="links">
            <ul>
                <li><g:link mapping="footerAboutKuorum"><g:message code="layout.footer.technology"/></g:link> </li>
                <li><g:link mapping="footerAboutUs"><g:message code="layout.footer.aboutUs"/></g:link> </li>
                <li><g:link mapping="footerInformation"><g:message code="layout.footer.pressTitle"/></g:link></li>
                <li><g:link mapping="footerContactUs"><g:message code="layout.footer.contact"/></g:link></li>
                <li><g:link mapping="blog"><g:message code="layout.footer.blog"/></g:link> </li>
                %{--<li><g:link mapping="footerGovernment"><g:message code="layout.footer.government"/></g:link></li>--}%
                %{--<li><g:link mapping="footerCitizens"><g:message code="layout.footer.citizens"/></g:link></li>--}%
                %{--<li><g:link mapping="footerAboutUs"><g:message code="layout.footer.about"/></g:link></li>--}%
                %{--<li><g:link mapping="footerWidget"><g:message code="layout.footer.widget"/></g:link></li>--}%
                <li><g:link mapping="footerPrivacyPolicy"><g:message code="layout.footer.privacyPolicy"/></g:link></li>
                <li><g:link mapping="footerTermsUse"><g:message code="layout.footer.termsUse"/></g:link></li>
                <sec:ifNotLoggedIn>
                    <li><nav:generateLangSelector /></li>
                </sec:ifNotLoggedIn>
            </ul>
        </section>
    </div><!-- /.container-fluid - da ancho máximo y centra -->

    <section class="more">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-md-8">
                    <ul class="social">
                        <li class="hidden-xs"><g:message code="layout.footer.followUs"/></li>
                        <li><a href="https://twitter.com/kuorumorg" target="_blank"><span class="sr-only">Twitter</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-twitter fa-stack-1x"></span></span></a></li>
                        <li><a href="https://www.facebook.com/kuorumorg" target="_blank"><span class="sr-only" target="_blank">Facebook</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span></a></li>
                        <li><a href="http://www.linkedin.com/company/kuorumorg" target="_blank"><span class="sr-only" target="_blank">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li>
                        <li><a href="https://plus.google.com/+KuorumOrg" target="_blank"><span class="sr-only" target="_blank">Google+</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-google-plus fa-stack-1x"></span></span></a></li>
                    </ul>
                </div>
                <div class="col-xs-12 col-sm-4 col-md-4">
                    <g:render template="/layouts/footer/licences"/>
                </div>
            </div>
        </div><!-- /.container-fluid - da ancho máximo y centra -->
    </section>
</footer>
