<li class="kakareo-number">
    <userUtil:counterUserClucks post="${post}"/>
    <g:link mapping="postCluckIt" class="action cluck ${postUtil.cssClassIfClucked(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
        <span class="icon-megaphone fa-lg"></span>
        <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction"><g:message code="cluck.footer.cluckIt"/></span>
    </g:link>
</li>