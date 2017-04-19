
<li class="col-sm-12 col-md-6">
    <article role="article" class="box-ppal clearfix">
        <div class="link-wrapper" id="debate-${debate.id}" data-datePublished="${debate.datePublished.time}">
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
                    <div class="card-text">${raw(debate.body.encodeAsRemovingHtmlTags())}</div>
                </g:if>
            </div>
            <div class="card-footer">
                <ul>
                    <g:if test="${showAuthor}">
                        <li class="owner">
                            <userUtil:showUserByAlias
                                    alias="${debate.userAlias}"
                                    showName="true"
                                    showRole="true"
                                    showActions="true"
                                    showDeleteRecommendation="true"
                                    htmlWrapper="li"
                            />
                        </li>
                    </g:if>
                    <li>
                        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                            <span class="fa fa fa-lightbulb-o fa-lg"></span>
                            <span class="number">${debate.numProposals}</span>
                        </g:link>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>