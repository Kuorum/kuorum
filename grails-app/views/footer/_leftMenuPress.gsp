<h1><g:message code="layout.footer.pressTitle"/></h1>
<ul>
    <li class="${activeMapping=='footerInformation'?'active':''}">
        <g:link mapping="footerInformation"><g:message code="layout.footer.information"/></g:link>
    </li>
    <li>
        <a href="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/press/KuorumPressKit_2016_${langPressKit}.pdf" target="_blank"><g:message code="layout.footer.press"/></a>
    </li>
    <li>
        <a href="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/press/logos_kuorum.zip" target="_blank"><g:message code="layout.footer.logo"/></a>
    </li>
    <li>
        <a href="mailto:comunicacion@kuorum.org?subject=Quiero recibir notas de prensa" target="_blank"><g:message code="layout.footer.releases"/></a>
    </li>
</ul>
