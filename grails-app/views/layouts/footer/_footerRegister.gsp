<%@ page import="kuorum.users.CookieUUIDService" %>
<footer id="footer" class="row" role="contentinfo">
    <div class="container-fluid">
        <section class="links">
            <ul>
                <li><g:link mapping="footerPrivacyPolicy" target="_blank" rel='nofollow noopener noreferrer'><g:message
                        code="layout.footer.privacyPolicy"/></g:link></li>
                <li><g:link mapping="footerTermsUse" target="_blank" rel='nofollow noopener noreferrer'><g:message
                        code="layout.footer.termsUse"/></g:link></li>
                <li><g:link mapping="footerCookiesInfo" target="_blank" rel="nofollow noopener noreferrer"><g:message
                        code="layout.footer.cookiesInfo"/></g:link></li>

                <nav:ifPageProperty pageProperty="showBrowserId" nullValue="false">
                    <li>Browser ID: <g:cookie name="${kuorum.users.CookieUUIDService.COOKIE_BROWSER_ID}"/></li>
                </nav:ifPageProperty>
            </ul>
        </section>
    </div><!-- /.container-fluid - da ancho máximo y centra -->

    <section class="more">
        <div class="container-fluid">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-md-8">

                </div>

                <div class="col-xs-12 col-sm-4 col-md-4">
                    %{--<g:render template="/layouts/footer/licences"/>--}%
                </div>
            </div>
        </div><!-- /.container-fluid - da ancho máximo y centra -->
    </section>
</footer>
