%{--<aside class="participAll">--}%
    %{--<h1><g:message code="project.clucks.title"/> </h1>--}%
    %{--<p><g:message code="project.clucks.description"/> </p>--}%
    %{--<!-- COMIENZA LISTA DE KAKAREOS -->--}%
    %{--<g:set var="urlLoadMore" value="${createLink(mapping: 'projectListClucks', params: project.encodeAsLinkProperties())}"/>--}%
    %{--<g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>--}%
%{--</aside>--}%


<h2 class="underline"><g:message code="project.clucks.title"/> </h2>

<div class="btn-group btn-group-justified filters" role="group" aria-label="Justified button group">
    <a href="#" class="btn active" role="button">
        <g:message code="project.clucks.newer"/>
        <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'project.clucks.newer')}" role="log" class="badge">23</span>
    </a>
    <a href="#" class="btn" role="button">
        <g:message code="project.clucks.defended"/>
        <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'project.clucks.defended')}" role="log" class="badge">5</span>
    </a>
    <a href="#" class="btn" role="button">
        <g:message code="project.clucks.vicotries"/>
        <span aria-relevant="additions" aria-live="assertive" aria-labelledby="${message(code:'project.clucks.vicotries')}" role="log" class="badge">3</span>
    </a>
</div>
<!-- COMIENZA LISTA DE KAKAREOS -->
<g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>