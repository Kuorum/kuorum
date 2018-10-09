<footer id="footer" class="row" role="contentinfo">
    <div class="container-fluid">
        <section class="links">
            <ul>
                <domain:footerLis/>
                %{--<li><g:link mapping="footerUserGuides"><g:message code="layout.footer.userGuides"/></g:link></li>--}%
                <li><g:link mapping="footerPrivacyPolicy" target="_blank"><g:message code="layout.footer.privacyPolicy"/></g:link></li>
                <li><g:link mapping="footerTermsUse" target="_blank"><g:message code="layout.footer.termsUse"/></g:link></li>
                %{--<sec:ifNotLoggedIn>--}%
                    %{--<li class="lang-selector"><nav:generateLangSelector /></li>--}%
                %{--</sec:ifNotLoggedIn>--}%
            </ul>
        </section>
    </div><!-- /.container-fluid - da ancho máximo y centra -->

    <section class="more">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-md-8">
                    <g:if test="${_social}">
                        <ul class="social">
                            <li class="hidden-xs"><g:message code="layout.footer.followUs"/></li>
                            <g:if test="${_social?.twitter}"><li><a href="${_social.twitter}" target="_blank"><span class="sr-only">Twitter</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-twitter fa-stack-1x"></span></span></a></li></g:if>
                            <g:if test="${_social?.facebook}"><li><a href="${_social.facebook}" target="_blank"><span class="sr-only" target="_blank">Facebook</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-facebook fa-stack-1x"></span></span></a></li></g:if>
                            <g:if test="${_social?.linkedIn}"><li><a href="${_social.linkedIn}" target="_blank"><span class="sr-only" target="_blank">LinkedIn</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-linkedin fa-stack-1x"></span></span></a></li></g:if>
                            <g:if test="${_social?.googlePlus}"><li><a href="${_social.googlePlus}" target="_blank"><span class="sr-only" target="_blank">Google+</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-google-plus fa-stack-1x"></span></span></a></li></g:if>
                            <g:if test="${_social?.instagram}"><li><a href="${_social.instagram}" target="_blank"><span class="sr-only" target="_blank">Instagram</span><span class="fa-stack fa-lg"><span class="fa fa-circle fa-stack-2x"></span><span class="fa fa-instagram fa-stack-1x"></span></span></a></li></g:if>
                        </ul>
                    </g:if>
                </div>
                <div class="col-xs-12 col-sm-4 col-md-4">
                    <g:render template="/layouts/footer/licences"/>
                </div>
            </div>
        </div><!-- /.container-fluid - da ancho máximo y centra -->
    </section>
</footer>
