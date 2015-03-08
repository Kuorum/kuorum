<sec:ifLoggedIn>
    <li class="kakareo-number">
        <userUtil:counterUserClucks post="${post}"/>
        <g:link mapping="postCluckIt" class="action cluck ${postUtil.cssClassIfClucked(post:post)}" params="${post.encodeAsLinkProperties()}" rel="nofollow">
            <span class="icon-megaphone fa-lg"></span>
            <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction"><g:message code="cluck.footer.cluckIt"/></span>
        </g:link>
    </li>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <li class="kakareo-number">
        <a role="button" data-toggle="modal" data-target="#registro" href="#">
            <span class="icon-megaphone fa-lg"></span>
            <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction"><g:message code="cluck.footer.cluckIt"/></span>
        </a>
    </li>
</sec:ifNotLoggedIn>