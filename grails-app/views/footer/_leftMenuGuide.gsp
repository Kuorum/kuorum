<h1><g:message code="layout.footer.userGuide"/></h1>
<ul>
    <li class="${activeMapping=='footerTechnology'?'active':''}">
        <g:link mapping="footerTechnology"><g:message code="layout.footer.whatIsKuorum"/></g:link>
    </li>
    <li class="${activeMapping=='footerPoliticians'?'active':''}">
        <g:link mapping="footerPoliticians"><g:message code="layout.footer.politicians"/></g:link>
    </li>
    <li class="${activeMapping=='footerCitizens'?'active':''}">
        <g:link mapping="footerCitizens"><g:message code="layout.footer.citizens"/></g:link>
    </li>
    <li class="${activeMapping=='footerDevelopers'?'active':''}">
        <g:link mapping="footerDevelopers"><g:message code="layout.footer.developers"/></g:link>
    </li>
</ul>
