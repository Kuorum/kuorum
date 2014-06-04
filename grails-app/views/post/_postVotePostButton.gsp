<%@ page import="kuorum.core.model.PostType" %>
<postUtil:userOption post="${post}">
    <postUtil:asNoLogged>
        <form>
            <g:link mapping="login" class="btn  ${important ?: 'btn-blue'} btn-lg btn-block" data-postId="${post.id}">
                <g:message code="post.show.boxes.like.vote.button.noLogged" encodeAs="raw"/>
            </g:link>
        </form>
    </postUtil:asNoLogged>
    <postUtil:asPolitician>
        <form id="driveDefend">
            <a href="#"
                    class="${post.defender ? 'disabled' : ''} btn  ${important ?: 'btn-blue'} btn-lg btn-block openModalDefender"
                    data-toggle="modal" data-target="#modalSponsor" params="${post.encodeAsLinkProperties()}"
                    data-postId="${post.id}">
                <g:message code="post.show.boxes.like.defend.${post.defender ? 'buttonDefended' : 'button'}" encodeAs="raw"/>
            </a>
        </form>
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
                            <g:each in="${kuorum.core.model.CommitmentType.recoverCommitmentTypesByPostType(post.postType)}" var="commitmentType">
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
    <postUtil:asUser>
        <form id="drive">
            <g:link mapping="postVoteIt"
                    class="${userVote ? 'disabled' : ''} btn  ${important ?: 'btn-blue'} btn-lg btn-block"
                    params="${post.encodeAsLinkProperties()}" data-postId="${post.id}">
                <g:message code="post.show.boxes.like.vote.${userVote ? 'buttonVoted' : 'button'}" encodeAs="raw"/>
            </g:link>
            <g:if test="${!userVote}">
                <div class="form-group">
                    <label class="checkbox-inline">
                        <input type="checkbox" name="anonymous"
                               value="private"/>
                        <g:message code="post.show.boxes.like.vote.anonymousCheckBoxLabel"/>
                    </label>
                </div>
            </g:if>
        </form>
    </postUtil:asUser>
</postUtil:userOption>
