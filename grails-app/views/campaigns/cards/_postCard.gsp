<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="post-${post.id}" data-datepublished="${post.datePublished.time}">
        <g:set var="campaignLink" value="${g.createLink(mapping: 'postShow', params: post.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: post]"/>
        <g:render template="/campaigns/cards/campaignBodyCard" model="[campaign: post, campaignLink: campaignLink]"/>
        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${post.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>
                <g:if test="${post.event}">
                    <li>
                        <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" role="button" class="${post.event.registered?'active':''}">
                            <span class="fal fa-ticket-alt fa-lg"></span>
                            <span class="number">${post.event.amountAssistants}</span>
                        </g:link>
                    </li>
                </g:if>
                <g:else>
                    <sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
                    <sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
                    <li>
                        <a class="post-like ${post.liked && isLogged?'active':''}"
                           data-postId="${post.id}"
                           data-postUserId="${post.user.id}"
                           data-urlAction="${g.createLink(mapping: "postLike")}"
                           data-loggedUser="${sec.username()}">
                            <span class="${post.liked && isLogged?'fas':'fal'} fa-heart fa-lg"></span>
                            <span class="number">${post.likes}</span>
                        </a>
                    </li>
                </g:else>
            </ul>
        </div>
    </div>
</article>
