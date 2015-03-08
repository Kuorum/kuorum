<sec:ifLoggedIn>
    <li class="read-later logged">
        <g:link mapping="postToggleFavorite" class="${postUtil.cssClassIfFavorite(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
            <span class="fa fa-bookmark fa-lg"></span><span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction"><g:message code="cluck.footer.readLater"/></span>
        </g:link>
    </li>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <li class="read-later">
        <a role="button" data-toggle="modal" data-target="#registro" href="#">
            <span class="fa fa-bookmark fa-lg"></span><span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction"><g:message code="cluck.footer.readLater"/></span>
        </a>
    </li>
</sec:ifNotLoggedIn>