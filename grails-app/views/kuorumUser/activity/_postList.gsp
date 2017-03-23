<li class="col-sm-12 col-md-6">
    <article role="article" class="box-ppal clearfix">
        <div class="link-wrapper">
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
                    <div class="card-text">${raw(post.body)}</div>
                </g:if>
            </div>
            <div class="card-footer">
                <ul>
                    <li>
                        <a class="post-like">
                            <span class="fa fa-heart-o fa-lg"></span>
                            <span class="label">${post.likes}</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>