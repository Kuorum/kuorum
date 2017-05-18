
<li class="col-sm-12 col-md-6">
    <article role="article" class="box-ppal clearfix">
        <div class="link-wrapper" id="post-${post.id}" data-datepublished="${post.datePublished.time}">
            <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}" class="hidden"></g:link>
            <g:if test="${post.photoUrl || post.videoUrl}">
            <div class="card-header-photo">
                <g:set var="postMultimedia" value="${false}"/>
                <g:if test="${post.photoUrl}">
                    <g:set var="postMultimedia" value="${true}"/>
                    <img src="${post.photoUrl}" alt="${post.title}">
                </g:if>
                <g:elseif test="${post.videoUrl}">
                    <g:set var="postMultimedia" value="${true}"/>
                    <image:showYoutube youtube="${post.videoUrl}"/>
                </g:elseif>
                    </div>
                </g:if>
                <div class="card-body">
                <h1>${post.title}</h1>
                <g:if test="${!postMultimedia}">
                    <div class="card-text"><modulesUtil:shortText text="${post.body}"/></div>
                </g:if>
            </div>
            <div class="card-footer">
                <ul>
                    <g:if test="${showAuthor}">
                        <li class="owner">
                            <userUtil:showUserByAlias
                                    alias="${post.userAlias}"
                                    showName="true"
                                    showRole="true"
                                    showActions="true"
                                    showDeleteRecommendation="true"
                                    htmlWrapper="li"
                            />
                        </li>
                    </g:if>
                    <li>
                        <a class="post-like ${post.liked?'active':''}"
                        data-postId="${post.id}"
                        data-userAlias="${post.userAlias}"
                        data-urlAction="${g.createLink(mapping: "postLike")}"
                        data-loggedUser="${sec.username()}">
                        <span class="fa ${post.liked?'fa-heart':'fa-heart-o'} fa-lg"></span>
                        <span class="number">${post.likes}</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>