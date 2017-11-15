
<li class="col-sm-12 col-md-6 search-article">
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
                            <userUtil:showUser
                                    user="${post.user}"
                                    showName="true"
                                    showActions="false"
                                    showDeleteRecommendation="false"
                                    htmlWrapper="div"
                            />
                        </li>
                    </g:if>
                    <g:set var="logged" value="${false}"/>
                    <sec:ifLoggedIn>
                        <g:set var="logged" value="${true}"/>
                    </sec:ifLoggedIn>
                    <li>
                        <a class="post-like ${post.liked && logged?'active':''}"
                        data-postId="${post.id}"
                        data-userAlias="${post.user.alias}"
                        data-urlAction="${g.createLink(mapping: "postLike")}"
                        data-loggedUser="${sec.username()}">
                        <span class="fa ${post.liked && logged?'fa-heart':'fa-heart-o'} fa-lg"></span>
                        <span class="number">${post.likes}</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>