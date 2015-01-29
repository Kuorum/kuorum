<aside class="participAll">
    <h1><g:message code="project.clucks.title"/> </h1>
    <p><g:message code="project.clucks.description"/> </p>
    <!-- COMIENZA LISTA DE KAKAREOS -->
    <g:set var="urlLoadMore" value="${createLink(mapping: 'projectListClucks', params: project.encodeAsLinkProperties())}"/>
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>
</aside>