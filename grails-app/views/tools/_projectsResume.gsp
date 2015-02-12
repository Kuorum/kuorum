<g:remoteLink role="button" class="btn ${published==''?'active':''}" mapping="projectListOfUsers" update="projectsList" params="[order: order, sort: sort, published: '', max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
    <g:message code="project.list.all" /><span class="badge" role="log" aria-labelledby="todos los proyectos" aria-live="assertive" aria-relevant="additions">${totalProjects}</span>
</g:remoteLink>
<g:remoteLink role="button" class="btn ${published=='true'?'active':''}" mapping="projectListOfUsers" update="projectsList" params="[order: order, sort: sort, published: 'true', max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
    <g:message code="project.list.published" /><span class="badge" role="log" aria-labelledby="proyectos publicados" aria-live="assertive" aria-relevant="additions">${publishedProjects}</span>
</g:remoteLink>
<g:remoteLink role="button" class="btn ${published=='false'?'active':''}" mapping="projectListOfUsers" update="projectsList" params="[order: order, sort: sort, published: 'false', max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
    <g:message code="project.list.drafts" /><span class="badge" role="log" aria-labelledby="borradores de proyectos" aria-live="assertive" aria-relevant="additions">${draftProjects}</span>
</g:remoteLink>