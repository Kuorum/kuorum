<li class="read-later">
    <g:link mapping="postToggleFavorite" class="${postUtil.cssClassIfFavorite(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
        <span class="fa fa-bookmark fa-lg"></span><span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction"><g:message code="cluck.footer.readLater"/></span>
    </g:link>
</li>