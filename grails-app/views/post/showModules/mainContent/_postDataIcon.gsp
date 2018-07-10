<sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
<sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
<g:set var="activeButton" value="${post.liked && isLogged}"/>
<div class="comment-counter pull-right">
    <button type="button" class="post-like ${activeButton?'active':''}"
            data-postId="${post.id}"
            data-postUserId="${post.user.id}"
            data-urlAction="${g.createLink(mapping: "postLike")}"
            data-campaignValidationActive="${post.checkValidation}"
            data-loggedUser="${sec.username()}"
    >
        <span class="fa ${activeButton?'fa-heart':'fa-heart-o'}" aria-hidden="true"></span>
        <span class="number">${post.likes}</span>
    </button>
</div>