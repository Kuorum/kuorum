<footer class="row">
    <ul class="col-md-2 info-kak">
        <li itemprop="keywords">
            <span class="fa ${postUtil.cssIconPostType(post:post)} fa-lg"></span><!-- icono -->
            <span class="sr-only"><g:message code="cluck.footer.${post.postType}"/></span><!-- texto que explica el icono y no es visible -->
        </li>
        <li class="sr-only" itemprop="datePublished">
            <kuorumDate:humanDate date="${post.dateCreated}"/>
        </li>
    </ul>
    <ul class="col-md-10 actions-kak">
        <li class="read-later">
            <a href="#">
                <span class="fa fa-bookmark fa-lg"></span>
                <span class="sr-only"><g:message code="cluck.footer.readLater"/></span>
            </a>
        </li>
        <li class="like-number">
            <span class="counter">${post.numVotes}</span>
            <meta itemprop="interactionCount" content="UserLikes:${post.numVotes}"><!-- pasarle el valor que corresponda -->
            <g:link mapping="postVoteIt" class="${postUtil.cssClassIfVoted(post:post)}" params="${post.encodeAsLinkProperties()}" >
                <span class="fa fa-rocket fa-lg"></span>
                <span class="sr-only"><g:message code="cluck.footer.vote"/></span>
            </g:link>
        </li>

        <li class="kakareo-number">
            <span class="counter">${post.numClucks}</span>
            <g:link mapping="postCluckIt" class="${postUtil.cssClassIfClucked(post:post)}" params="${post.encodeAsLinkProperties()}" >
                <span class="fa fa-bullhorn fa-lg"></span><span class="sr-only"><g:message code="cluck.footer.cluckIt"/></span>
            </g:link>
        </li>

        <li class="more-actions">
            <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                <span class="fa fa-plus"></span> <span class="sr-only">Más acciones</span>
            </a>
        </li>
    </ul>
</footer>