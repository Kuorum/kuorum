<h1><g:message code="layout.footer.aboutKuorum"/></h1>
<ul>
    <li class="${activeMapping=='footerWhatIsKuorum'?'active':''}">
        <g:link mapping="footerWhatIsKuorum"><g:message code="layout.footer.whatIsKuorum"/></g:link>
    </li>
    <li class="${activeMapping=='footerVision'?'active':''}">
        <g:link mapping="footerVision"><g:message code="layout.footer.vision"/></g:link>
    </li>
    <li class="${activeMapping=='footerTeam'?'active':''}">
        <g:link mapping="footerTeam"><g:message code="layout.footer.team"/></g:link>
    </li>
    <li>
        <a href="mailto:info@kuorum.org" target="_blank"><g:message code="layout.footer.contact"/></a>
    </li>
</ul>
