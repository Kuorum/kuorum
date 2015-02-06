<div aria-label="button group" role="group" class="btn-group btn-group-justified filters">
    <g:remoteLink role="button" class="btn active" mapping="projectListOfUsers" update="projectsList" params="[order: order, sort: sort, published: published, max: max, offset: offset]">
        <g:message code="project.list.all" /><span class="badge" role="log" aria-labelledby="todos los proyectos" aria-live="assertive" aria-relevant="additions">${projects.size()}</span>
    </g:remoteLink>
    <g:remoteLink role="button" class="btn active" mapping="projectListOfUsers" update="projectsList" params="[order: order, sort: sort, published: 'true', max: max, offset: offset]">
        <g:message code="project.list.published" /><span class="badge" role="log" aria-labelledby="proyectos publicados" aria-live="assertive" aria-relevant="additions">${projects.findAll{it.published}.size()}</span>
    </g:remoteLink>
    <g:remoteLink role="button" class="btn active" mapping="projectListOfUsers" update="projectsList" params="[order: order, sort: sort, published: 'false', max: max, offset: offset]">
        <g:message code="project.list.drafts" /><span class="badge" role="log" aria-labelledby="borradores de proyectos" aria-live="assertive" aria-relevant="additions">${projects.findAll{!it.published}.size()}</span>
    </g:remoteLink>
</div>

<div class="box-ppal">
    <ul class="filter-order">
        <li><g:message code="project.list.orderBy" /></li>
        <li class="dropdown">
            <a data-target="#" href="#" class="dropdown-toggle" id="open-order" data-toggle="dropdown" role="button"><g:message code="project.list.orderBy.antiquity" /><span class="fa fa-caret-down fa-lg"></span></a>
            <ul id="ordenar" class="dropdown-menu" aria-labelledby="open-order" role="menu">
                %{--TODO: ¿Qué opciones son?--}%
                <li><a href="#"><g:message code="project.list.orderBy.antiquity.option1" /></a></li>
                <li><a href="#"><g:message code="project.list.orderBy.antiquity.option2" /></a></li>
                <li><a href="#"><g:message code="project.list.orderBy.antiquity.option3" /></a></li>
            </ul>
        </li>
        <li><g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[order: 'peopleVotes', sort: 'asc', published: 'false', max: max, offset: offset]"><g:message code="project.list.orderBy.votes" /></g:remoteLink></li>
        <li><g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[order: 'numPosts', sort: 'asc', published: 'false', max: max, offset: offset]"><g:message code="project.list.orderBy.proposals" /></g:remoteLink></li>
        <li><g:remoteLink role="button" mapping="projectListOfUsers" update="projectsList" params="[order: 'peopleVoteYes', sort: 'asc', published: 'false', max: max, offset: offset]"><g:message code="project.list.orderBy.percent" /></g:remoteLink></li>
    </ul>

    <ul class="list-project">

    <g:each in="${projects}" var="project">
        <li>
            <article itemtype="http://schema.org/Article" role="article" class="kakareo post ley">


                <!-- FLECHITA PARA ABRIR MENÚ -->
                <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
                    <span class="fa fa-chevron-down"></span>
                    <span class="sr-only"><g:message code="project.list.show.options" /></span>
                </span>
                <!-- POPOVER OPCIONES EDICIÓN -->
                <div class="popover">
                    <div class="popover-more-actions edition">
                        <ul>
                            <li>
                                <a href="#">
                                    <span><g:message code="project.list.publish" /></span>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span><g:message code="project.list.edit" /></span>
                                </a>
                            </li>
                            <li>
                                <a href="#" data-toggle="modal" data-target="#eliminar-proyecto"><g:message code="project.list.delete" /></a>
                                <!-- la modal está en al final, antes de cerrar la etiqueta <body> -->
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- FIN POPOVER OPCIONES EDICIÓN -->

                <div class="row">
                    <div class="col-xs-12 col-sm-5 laley">
                        <a href="#" itemprop="keywords">${project.hashtag}</a>
                        <time>
                            <g:if test="${project.updates}">
                                <g:message code="project.list.project.updates" /> ${project.lastUpdate} <br/>
                            </g:if>
                            <g:else>
                                <g:message code="project.list.project.no.updates" /> <br/>
                            </g:else>
                            <span>${project.timeToDeadline} <g:message code="project.list.project.deadline" /></span>
                        </time>
                    </div>
                    <div class="col-xs-12 col-sm-7">
                        <p class="total-info text-right">${project.votesInRegion} <g:message code="project.list.project.region.votes.label" /></p>
                        <ul class="infoVotes text-right">
                            <li class="vote-yes">
                                <span>${project.peopleVotes.yes}</span>
                                <span class="sr-only"><g:message code="project.list.project.votesInFavour" /></span>
                                <span class="icon-smiley fa-lg"></span>
                            </li>
                            <li class="vote-no">
                                <span>${project.peopleVotes.no}</span>
                                <span class="sr-only"><g:message code="project.list.project.votesAgainst" /></span>
                                <span class="icon-sad fa-lg"></span>
                            </li>
                            <li class="vote-neutral">
                                <span>${project.peopleVotes.abs}</span>
                                <span class="sr-only"><g:message code="project.list.project.abstentions" /></span>
                                <span class="icon-neutral fa-lg"></span>
                            </li>
                            <li>
                                <span>${project.peopleVotes.numPosts}</span>
                                <span class="sr-only"><g:message code="project.list.project.proposals" /></span>
                                <span class="fa fa-lightbulb-o fa-lg"></span>
                            </li>
                        </ul>
                    </div>
                </div><!-- /.row -->
            </article>
        </li>
    </g:each>

    </ul>
    <div class="text-center" id="load-more"><a href="#"><g:message code="project.list.showMore" /></a></div>
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
