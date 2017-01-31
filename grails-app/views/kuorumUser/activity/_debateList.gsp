<li class="col-sm-12 col-md-6">
    <article role="article" class="box-ppal clearfix">
        <div class="link-wrapper">
            <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" class="hidden"></g:link>
            <g:if test="${debate.photoUrl || debate.videoUrl}">
                <div class="card-header-photo">
                    <g:set var="debateMultimedia" value="${false}"/>
                    <g:if test="${debate.photoUrl}">
                        <g:set var="debateMultimedia" value="${true}"/>
                        <img src="${debate.photoUrl}" alt="${debate.title}">
                    </g:if>
                    <g:elseif test="${debate.videoUrl}">
                        <g:set var="debateMultimedia" value="${true}"/>
                        <image:showYoutube youtube="${debate.videoUrl}"/>
                    </g:elseif>
                </div>
            </g:if>
            <div class="card-body">
                <h1>${debate.title}</h1>
                <g:if test="${!debateMultimedia}">
                    <div class="card-text">${raw(debate.body)}</div>
                </g:if>
            </div>
            <div class="card-footer">
                <ul class="pull-right">
                    <li>
                        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                            <span class="fa fa fa-lightbulb-o fa-lg"></span>
                            <span class="label">${debate.numProposals}</span>
                        </g:link>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>