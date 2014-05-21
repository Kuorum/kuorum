
<g:include controller="modules" action="registerFooterRelevantUsers"/>

<footer id="footer" class="row" role="contentinfo">
    <section class="links simple">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-md-8">
                    <ul class="legal">
                        <li><g:link mapping="footerPrivacyPolicy" target="_blank"><g:message code="layout.footer.privacyPolicy"/></g:link></li>
                        <li><g:link mapping="footerTermsUse" target="_blank"><g:message code="layout.footer.termsUse"/></g:link></li>
                        %{--<li><g:link mapping="footerTermsAds"><g:message code="layout.footer.termsAds"/></g:link></li>--}%
                    </ul>
                </div>
                <div class="col-xs-12 col-sm-4 col-md-4">
                    <g:render template="/layouts/footer/licences"/>
                </div>
            </div>
        </div><!-- /.container-fluid - da ancho máximo y centra -->
    </section>
</footer>