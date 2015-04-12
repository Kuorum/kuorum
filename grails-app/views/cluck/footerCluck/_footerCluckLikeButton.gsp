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
                    ${post.defender ?'':'data-toggle="modal" data-target="#modalSponsor"'}
                   params="${post.encodeAsLinkProperties()}"
                   data-postId="${post.id}">
                    <span class="fa fa-trophy fa-lg"></span>
                    <span class="${displayingColumnC?'sr-only':'hidden-xs'} label-cluckAction">
                        <g:message code="cluck.footer.${post.defender ? 'defended' : 'defend'}"/>
                    </span>
                </a>

                <g:set var="postType" value="${message(code: "${PostType.class.name}.${post.postType}")}"/>
                <script>
                    $(function () {
                        modalDefend.data.post_${post.id} = {
                            owner: {
                                name: "${post.owner.name}",
                                imageUrl: "${image.userImgSrc(user:post.owner)}"
                            },
                            defender: {
                                name: "${sec.loggedInUserInfo(field:'name')}",
                                imageUrl: "${image.loggedUserImgSrc()}"
                            },
                            post: {
                                sponsorLabel: '${message(code:'modalDefend.defender.sponsorLabel', args:[postType])}',
                                what: '${message(code:'modalDefend.owner.what', args:[postType])}',
                                title: "${post.title}",
                                options: [
                                    %{-- Guarrerida para generar el js que se necesita para pintar la ventana modal --}%
                                    <g:each in="${kuorum.core.model.CommitmentType.recoverCommitmentTypesByPostType(PostType.PURPOSE)}" var="commitmentType">
                                    {
                                        textButton: "${message(code:"modalDefend.option.${commitmentType}.button")}",
                                        textDescription: "${message(code:"modalDefend.option.${commitmentType}.description")}",
                                        defendLink: "${g.createLink(mapping: 'postAddDefender', params:post.encodeAsLinkProperties() + [commitmentType:commitmentType])}"
                                    },
                                    </g:each>
                                    {
                                        kk:'this js object is for not delete the las comma. Where it is used dont iterate over this array'
                                    }
                                ],
                                description: "${message(code:'modalDefend.description', args:[post.owner.name, postType, post.numVotes])}",
                                numVotes:${post.numVotes}
                            }
                        }
                    });
                </script>
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