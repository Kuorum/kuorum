<h1><g:message code="layout.footer.about"/></h1>
<ul>
    <li class="${activeMapping=='footerAboutUs'?'active':''}">
        <g:link mapping="footerAboutUs"><g:message code="layout.footer.aboutUs"/></g:link>
    </li>
    <li class="${activeMapping=='footerVision'?'active':''}">
        <g:link mapping="footerVision"><g:message code="layout.footer.vision"/></g:link>
    </li>
    <li class="${activeMapping=='footerImpact'?'active':''}">
        <g:link mapping="footerImpact"><g:message code="layout.footer.impact"/></g:link>
    </li>
    <li class="${activeMapping=='footerTeam'?'active':''}">
        <g:link mapping="footerTeam"><g:message code="layout.footer.team"/></g:link>
    </li>
    <li>
        <a href="mailto:info@kuorum.org" target="_blank"><g:message code="layout.footer.contact"/></a>
    </li>
</ul>
