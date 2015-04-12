<h1><g:message code="layout.footer.pressTitle"/></h1>
<ul>
    <li class="${activeMapping=='footerInformation'?'active':''}">
        <g:link mapping="footerInformation"><g:message code="layout.footer.information"/></g:link>
    </li>
    <li>
        <a href="${resource(dir: 'resources/pdf', file: '201504 Dossier de Prensa - Kuorum esp.pdf')}" target="_blank"><g:message code="layout.footer.press"/></a>
    </li>
    <li>
        <a href="${resource(dir: 'resources/zip', file: 'logos.zip')}" target="_blank"><g:message code="layout.footer.logo"/></a>
    </li>
    <li>
        <a href="mailto:comunicacion@kuorum.org?subject=Quiero recibir notas de prensa" target="_blank"><g:message code="layout.footer.releases"/></a>
    </li>
</ul>
