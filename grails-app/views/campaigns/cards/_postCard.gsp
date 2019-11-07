<r:require modules="post"/>
<article role="article" class="box-ppal clearfix">
    <div class="link-wrapper" id="post-${post.id}" data-datepublished="${post.datePublished.time}">
        <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"></g:link>
        %{--<g:if test="${post.photoUrl || post.videoUrl}">--}%
        <div class="card-header-photo">
            <g:if test="${post.photoUrl}">
                <img src="${post.photoUrl}" alt="${post.title}">
            </g:if>
            <g:elseif test="${post.videoUrl}">
                <image:showYoutube youtube="${post.videoUrl}" campaign="${post}"/>
            </g:elseif>
            <g:else>
                <div class="multimedia-campaign-default">
                    <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${post.title}"/>
                </div>
            </g:else>
        </div>
        %{--</g:if>--}%
            <div class="card-body">
                <h1>
                    <g:link mapping="postShow" class="link-wrapper-clickable" params="${post.encodeAsLinkProperties()}">
                        ${post.title}
                    </g:link>
                </h1>
            </div>
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
