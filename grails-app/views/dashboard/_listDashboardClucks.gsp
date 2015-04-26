<!-- COMIENZA LISTA DE KAKAREOS Y SEGUIMIENTOS -->
<ul class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
    <g:render template="/cluck/liClucks" model="[clucks:clucks.clucks_1]"/>
</ul>
<div class="row">
    <ul class="kakareo-list project" role="log" aria-relevant="additions" aria-live="assertive">
        <g:each in="${projectEvents}" var="projectEvent">
            <li class="col-md-6">
                <g:set var="projectUpdate" value=""/>
                <g:if test="${projectEvent.projectAction == kuorum.project.ProjectAction.PROJECT_UPDATE}">
                    <g:set var="projectUpdate" value="${projectEvent.project.updates[projectEvent.projectUpdatePos]}"/>
                </g:if>
                <g:render template="/modules/projects/projectOnList" model="[project: projectEvent.project, projectUpdate:projectUpdate]"/>
            </li>
        </g:each>
    </ul>
</div>

<!-- ver más -->
<div id="trasteando" class="text-center">
    <g:message code="dashboard.projects.seeMore" args="[g.createLink(mapping: 'discoverProjects')]"/>
</div>
<g:if test="${clucks.clucks_2}">
    <ul id="list-kakareos-id" class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
        <g:render template="/cluck/liClucks" model="[clucks:clucks.clucks_2]"/>
    </ul>
</g:if>
<!-- ver más -->
<g:if test="${seeMore}">
    <div id="load-more" class="text-center">
        <a href="${urlLoadMore}" class="loadMore" data-parent-id="list-kakareos-id">
            <g:message code="dashboard.clucks.seeMore"/>
        </a>
    </div>
</g:if>
