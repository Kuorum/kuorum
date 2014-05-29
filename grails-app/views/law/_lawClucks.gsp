<aside class="participAll">
    <h1><g:message code="law.clucks.title"/> </h1>
    <p><g:message code="law.clucks.description"/> </p>
    <!-- COMIENZA LISTA DE KAKAREOS -->
    <g:set var="urlLoadMore" value="${createLink(mapping: 'lawListClucks', params: law.encodeAsLinkProperties())}"/>
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>
</aside>