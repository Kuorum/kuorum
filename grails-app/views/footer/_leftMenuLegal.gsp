<h1><g:message code="layout.footer.legal"/></h1>
<ul>
    <li class="${activeMapping=='footerPrivacyPolicy'?'active':''}">
        <g:link mapping="footerPrivacyPolicy"><g:message code="layout.footer.privacyPolicy"/></g:link>
    </li>
    <li class="${activeMapping=='footerTermsUse'?'active':''}">
        <g:link mapping="footerTermsUse"><g:message code="layout.footer.termsUse"/></g:link>
    </li>
    <li class="${activeMapping=='footerCookiesInfo'?'active':''}">
        <g:link mapping="footerCookiesInfo"><g:message code="layout.footer.cookiesInfo"/></g:link>
    </li>
</ul>
