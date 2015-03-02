<!-- COMIENZA LISTA DE KAKAREOS Y SEGUIMIENTOS -->
<ul class="kakareo-list project">
    <g:render template="/project/liProjects2Columns" model="[projects:projects]"/>
</ul>
<!-- ver más -->
<g:if test="${seeMore}">
    <div id="load-more" class="text-center">
        <a href="${urlLoadMore}" class="loadMore" data-parent-id="list-kakareos-id">
            <g:message code="dashboard.clucks.seeMore"/>
        </a>
    </div>
</g:if>