<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="showModules/mainContent/postDataMultimedia" model="[post:post]"/>
    <div class="header">
        <h1 class="title">${post.title}</h1>
        <userUtil:showUser user="${postUser}" showRole="true"/>
        <div class="clearfix">
            <span class="time-ago pull-left"><kuorumDate:humanDate date="${post.datePublished}"/> </span>
            <userUtil:ifUserIsTheLoggedOne user="${postUser}">
            %{--campaignList contains the js to open modal when the debate is scheduled --}%
                <r:require modules="campaignList"/>
                <g:set var="modal" value="${post.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
                <g:link class="edit ${modal}" mapping="postEditContent" params="${post.encodeAsLinkProperties()}">
                    <span class="fa fa-pencil-square-o pull-right fa-2x" aria-hidden="true"></span>
                </g:link>
            </userUtil:ifUserIsTheLoggedOne>
        </div>
    </div>

    <div class="body">
        ${raw(post.body)}
    </div>


    <div class="footer clearfix">
        <g:render template="/post/showModules/mainContent/postDataSocial" model="[post:post, postUser:postUser]"/>

        <sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
        <sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
        <g:set var="activeButton" value="${post.liked && isLogged}"/>
        <div class="comment-counter pull-right">
            <button type="button" class="post-like ${activeButton?'active':''}"
                    data-postId="${post.id}"
                    data-userAlias="${post.user.alias}"
                    data-urlAction="${g.createLink(mapping: "postLike")}"
                    data-loggedUser="${sec.username()}"
                >
                <span class="fa ${activeButton?'fa-heart':'fa-heart-o'}" aria-hidden="true"></span>
                <span class="number">${post.likes}</span>
            </button>
        </div>
    </div>

</div> <!-- ^leader-post !-->