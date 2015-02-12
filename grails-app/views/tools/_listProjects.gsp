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
                        <g:if test="${!project.published}">
                            <li>
                                <g:remoteLink role="button" url="[mapping:'publishProject', params:[hashtag: project.hashtag, order: order, sort: sort, published: published, max: max, offset: offset, seeMore: seeMore, urlLoadMore: urlLoadMore]]" update="projectsList">
                                    <span><g:message code="project.list.publish" /></span>
                                </g:remoteLink>
                            </li>
                        </g:if>
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
                            <g:message code="project.list.project.updates" /><kuorumDate:humanDate date="${project.lastUpdate}"/><br/>
                        </g:if>
                        <g:else>
                            <g:message code="project.list.project.no.updates" /> <br/>
                        </g:else>
                        <kuorumDate:humanDate date="${project.deadline}"/>
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

<g:javascript src="loadMore.js" />