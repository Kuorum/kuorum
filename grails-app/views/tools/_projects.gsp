<div aria-label="button group" role="group" class="btn-group btn-group-justified filters" id="projectsResume">
    <g:render template="projectsResume" model="[order: order, sort: sort, published: published, max: max, offset: offset, totalProjects: totalProjects, publishedProjects: publishedProjects, draftProjects: draftProjects, seeMore: seeMore, urlLoadMore: urlLoadMore]"/>
</div>

<div class="box-ppal">
    <ul class="filter-order">
        <li><g:message code="project.list.orderBy" /></li>
        <li>
            <g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[sort: 'dateCreated', order: (sort=='dateCreated' && order=='asc'?'desc':'asc'), published: published, max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
                <g:message code="project.list.orderBy.antiquity" /><g:if test="${sort == 'dateCreated'}" ><span class="fa fa-caret-${order=='asc'?'up':'down'} fa-lg"></span></g:if>
            </g:remoteLink>
        </li>
        <li>
            <g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[sort: 'peopleVotes', order: (sort=='peopleVotes' && order=='asc'?'desc':'asc'), published: published, max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
                <g:message code="project.list.orderBy.votes" /><g:if test="${sort == 'peopleVotes'}" ><span class="fa fa-caret-${order=='asc'?'up':'down'} fa-lg"></span></g:if>
            </g:remoteLink>
        </li>
        <li>
            <g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[sort: 'numPosts', order: (sort=='numPosts' && order=='asc'?'desc':'asc'), published: published, max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
                <g:message code="project.list.orderBy.proposals" /><g:if test="${sort == 'numPosts'}" ><span class="fa fa-caret-${order=='asc'?'up':'down'} fa-lg"></span></g:if>
            </g:remoteLink>
        </li>
        <li>
            <g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[sort: 'peopleVoteYes', order: (sort=='peopleVoteYes' && order=='asc'?'desc':'asc'), published: published, max: max, offset: offset, template: 'projects', urlLoadMore: urlLoadMore]">
                <g:message code="project.list.orderBy.percent" /><g:if test="${sort == 'peopleVoteYes'}" ><span class="fa fa-caret-${order=='asc'?'up':'down'} fa-lg"></span></g:if>
            </g:remoteLink>
        </li>
    </ul>

    <ul class="list-project" id="list-projects-id">
        <g:render template="listProjects" model="[projects:projects, order: order, sort: sort, published: published, max: max, offset: offset, totalProjects: totalProjects, publishedProjects: publishedProjects, draftProjects: draftProjects, seeMore: seeMore, urlLoadMore: urlLoadMore]"/>
    </ul>

    <!-- ver más -->
    <g:if test="${seeMore}">
        <div id="load-more" class="text-center">
            %{--<g:hiddenField name="urlLoadMore" value="${urlLoadMore}"/>--}%
            <a href="${urlLoadMore}" class="loadMore" data-parent-id="list-projects-id">
                <g:message code="project.list.showMore" />
            </a>
        </div>
    </g:if>

</div>

<!-- Modal eliminar-proyecto -->
<div class="modal fade" id="eliminar-proyecto" tabindex="-1" role="dialog" aria-labelledby="eliminarProyecto" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="default.close" /></span></button>
                <h4 class="modal-title" id="eliminarProyecto"><g:message code="project.list.project.delete.label" /></h4>
            </div>
            <div class="modal-body clearfix">
                <p><g:message code="project.list.project.delete.question" /></p>
                <a href="#" class="btn btn-grey pull-right"><g:message code="project.list.project.delete" /></a>
            </div>
        </div>
    </div>
</div>
<!-- fin modal eliminar-proyecto -->
