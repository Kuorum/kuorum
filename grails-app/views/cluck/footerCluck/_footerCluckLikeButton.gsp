<%@ page import="kuorum.core.model.PostType" %>

<li class="like-number">
    <sec:ifLoggedIn>
        <userUtil:counterUserLikes post="${post}"/>
        %{--<meta itemprop="interactionCount" content="UserLikes:${post.numVotes}"><!-- pasarle el valor que corresponda -->--}%
        <postUtil:userOption post="${post}">
            <postUtil:asUser>
            <g:link mapping="postVoteIt" class="${postUtil.cssClassIfVoted(post:post)} action drive" params="${post.encodeAsLinkProperties()}" rel="nofollow">
                <span class="fa fa-rocket fa-lg"></span>
                <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction">
                    <g:message code="cluck.footer.vote"/>
                </span>
            </g:link>
            </postUtil:asUser>
            <postUtil:asPolitician>

                <a href="#"
                   class="${post.defender ? 'disabled' : 'openModalDefender'}"
                    ${post.defender ?'':'data-toggle="modal" data-target="#apadrina-propuesta"'}
                   data-postId="${post.id}">
                    <span class="fa fa-trophy fa-lg"></span>
                    <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction">
                        <g:message code="cluck.footer.${post.defender ? 'defended' : 'defend'}"/>
                    </span>
                </a>
            </postUtil:asPolitician>
        </postUtil:userOption>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
        <a role="button" data-toggle="modal" data-target="#registro" href="#">
           <span class="fa fa-rocket fa-lg"></span>
           <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction">
                <g:message code="cluck.footer.vote"/>
            </span>
        </a>
    </sec:ifNotLoggedIn>
</li>