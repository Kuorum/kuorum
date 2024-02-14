<% String[] availableDomains = ["local.kuorum.org", "front.dev.kuorum.org","participacion.autismo.org.es"] %>
<g:if test="${availableDomains.contains(kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain)}">
    <div id='qhld-widget'></div>
    <script async src='https://autismo-widget.quehacenlosdiputados.es/embed.js'></script>
</g:if>