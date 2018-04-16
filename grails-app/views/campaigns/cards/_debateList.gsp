
<article role="article" class="box-ppal clearfix">
    <div class="link-wrapper" id="debate-${debate.id}" data-datePublished="${debate.datePublished.time}">
        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" class="hidden"></g:link>
        <g:if test="${debate.photoUrl || debate.videoUrl}">
            <div class="card-header-photo">
                <g:if test="${debate.photoUrl}">
                    <img src="${debate.photoUrl}" alt="${debate.title}">
                </g:if>
                <g:elseif test="${debate.videoUrl}">
                    <image:showYoutube youtube="${debate.videoUrl}"/>
                </g:elseif>
                <g:else>
                    <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${post.title}">
                </g:else>
            </div>
        </g:if>
        <div class="card-body">
            <h1>
                <g:link mapping="debateShow" class="link-wrapper-clickable" params="${debate.encodeAsLinkProperties()}">
                    ${debate.title}
                </g:link>
            </h1>
            <g:if test="${!debateMultimedia}">
                <div class="card-text"><modulesUtil:shortText text="${debate.body}"/></div>
            </g:if>
        </div>
        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${debate.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>
                <g:if test="${debate.event}">
                    <li>
                        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" role="button" class="${debate.event.registered?'active':''}">
                            <span class="fa fa fa-ticket fa-lg"></span>
                            <span class="number">${debate.event.amountAssistants}</span>
                        </g:link>
                    </li>
                </g:if>
                <g:else>
                    <li>
                        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                            <span class="fa fa fa-lightbulb-o fa-lg"></span>
                            <span class="number">${debate.numProposals}</span>
                        </g:link>
                    </li>
                </g:else>
            </ul>
        </div>
    </div>
</article>
