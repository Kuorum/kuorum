<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="showModules/mainContent/postDataMultimedia" model="[post:post]"/>
    <div class="header">
        <h1 class="title">${post.title}</h1>
        <userUtil:showUser user="${postUser}"/>
        <div class="clearfix">
            <span class="time-ago pull-left"><kuorumDate:humanDate date="${post.datePublished}"/> </span>
            <userUtil:ifUserIsTheLoggedOne user="${postUser}">
                <g:link class="edit" mapping="postEditContent" params="${post.encodeAsLinkProperties()}">
                    <span class="fa fa-pencil-square-o pull-right fa-2x" aria-hidden="true"></span>
                </g:link>
            </userUtil:ifUserIsTheLoggedOne>
        </div>
    </div>

    <div class="body">
        ${raw(post.body)}
    </div>


    <div class="footer clearfix">
        %{--<g:render template="/post/showModules/mainContent/postDataLabels" model="[post:post]"/>--}%
        <g:render template="/post/showModules/mainContent/postDataSocial" model="[post:post, postUser:postUser]"/>

        <div class="comment-counter pull-right">
            <button type="button" class="post-like ${post.liked?'active':''}"
                    data-postId="${post.id}"
                    data-userAlias="${post.userAlias}"
                    data-urlAction="${g.createLink(mapping: "postLike")}"
                    data-loggedUser="${sec.username()}"
                >
                <span class="fa ${post.liked?'fa-heart':'fa-heart-o'}" aria-hidden="true"></span>
                <span class="number">${post.likes}</span>
            </button>
        </div>
    </div>

</div> <!-- ^leader-post !-->