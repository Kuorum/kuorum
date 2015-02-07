<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.ProjectStatusType" %>
<div class="${header?"":"form-group"} voting" data-projectId="${project.id}">
    <g:if test="${project.deadline > new Date()}">
        %{--OPEN PROJECT--}%
        <sec:ifLoggedIn>
            <ul>
                <projectUtil:ifUserAvailableForVoting project="${project}">
                    <li class="${VoteType.POSITIVE}">
                        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.POSITIVE]}" role="button" class="ajaxVote ${userVote?.voteType.equals(VoteType.POSITIVE)?"active":""}" data-projectId="${project.id}">
                            <span class="icon-smiley fa-3x"></span>
                            <span class="${header?'sr-only':''}"><g:message code="project.vote.yes"/></span>
                        </g:link>
                    </li>
                    <li class="${VoteType.NEGATIVE}">
                        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.NEGATIVE]}" role="button" class="ajaxVote ${userVote?.voteType.equals(VoteType.NEGATIVE)?"active":""}" data-projectId="${project.id}">
                            <span class="icon-sad fa-3x"></span>
                            <span class="${header?'sr-only':''}"><g:message code="project.vote.no"/></span>
                        </g:link>

                    </li>
                    <li class="${VoteType.ABSTENTION}">
                        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.ABSTENTION]}" role="button" class="ajaxVote ${userVote?.voteType.equals(VoteType.ABSTENTION)?"active":""}" data-projectId="${project.id}">
                            <span class="icon-neutral fa-3x"></span>
                            <span class="${header?'sr-only':''}"><g:message code="project.vote.abs"/></span>
                        </g:link>
                    </li>
                    <projectUtil:ifAllowedToAddPost project="${project}">
                        <li>
                            <g:link mapping="postCreate" params="${project.encodeAsLinkProperties()}" role="button" class="${userVote?.voteType.equals(VoteType.ABSTENTION)?"active":""}" data-projectId="${project.id}">
                                <span class="fa fa-lightbulb-o fa-3x"></span>
                                <span class="${header?'sr-only':''}"><g:message code="project.vote.newPost"/></span>
                            </g:link>
                        </li>
                    </projectUtil:ifAllowedToAddPost>
                </projectUtil:ifUserAvailableForVoting>
                <projectUtil:elseUserAvailableForVoting>
                    %{--USUARIO NO REGISTRADO COMPLETAMENTE--}%

                </projectUtil:elseUserAvailableForVoting>

                <projectUtil:ifAllowedToUpdateProject project="${project}">
                    <li>
                        <g:link mapping="projectUpdate" params="${project.encodeAsLinkProperties()}">
                            <span class="icon2-update fa-3x"></span>
                            <span class="${header?'sr-only':''}"><g:message code="project.vote.updateProject"/></span>
                        </g:link>
                    </li>
                </projectUtil:ifAllowedToUpdateProject>
            </ul>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:link mapping="projectShowSec" params="${project.encodeAsLinkProperties()}" class="btn btn-blue btn-block vote">
                <g:message code="project.vote.voteButton" encodeAs="raw"/>
            </g:link><!-- al hacer click lo deshabilito y cambio el texto -->
        </sec:ifNotLoggedIn>
    </g:if> %{--FIN DE LA LEY ABIERTA--}%
    <g:else> %{-- LEY CERRADA--}%
        <span class="closed">
            <g:message code="project.subHeader.closedProject"/>
            <projectUtil:ifAllowedToUpdateProject project="${project}">
                <a href="#"><span class="fa icon2-update fa-lg"></span></a>
            </projectUtil:ifAllowedToUpdateProject>
        </span>
    </g:else>
    %{--<g:if test="${project.availableStats}">--}%
        %{--<g:link mapping="projectStats" params="${project.encodeAsLinkProperties()}" class="hidden-xs">--}%
            %{--<g:message code="project.vote.stats"/>--}%
        %{--</g:link>--}%
    %{--</g:if>--}%
</div>