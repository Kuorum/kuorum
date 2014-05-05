<!-- COMIENZA LISTA DE KAKAREOS Y SEGUIMIENTOS -->
<ul id="list-kakareos-id" class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
    <g:render template="/cluck/liClucks" model="[clucks:clucks]"/>
</ul>
<!-- ver más -->
<div id="load-more" class="text-center">
    <a href="${urlLoadMore}" class="loadMore" data-parent-id="list-kakareos-id">
        <g:message code="dashboard.clucks.seeMore"/>
    </a>
</div>