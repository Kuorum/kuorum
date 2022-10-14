<sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
<sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
<g:set var="activeButton" value="${post.liked && isLogged}"/>
<div class="comment-counter pull-right">
    <button type="button" class="post-like ${activeButton ? 'active' : ''}"
            data-postId="${post.id}"
            data-postUserId="${post.user.id}"
            data-urlAction="${g.createLink(mapping: "postLike", params: post.encodeAsLinkProperties())}"
            data-campaignValidationActive="${post.checkValidationActive}"
            data-campaignGroupValidationActive="${post.groupValidation ? g.createLink(mapping: "campaignCheckGroupValidation", params: post.encodeAsLinkProperties()) : ''}"
            data-campaignId="${post.id}"
            data-loggedUser="${sec.username()}">
        <span class="${activeButton ? 'fas' : 'fal'} fa-heart" aria-hidden="true"></span>
        <span class="number">${post.likes}</span>
    </button>
</div>