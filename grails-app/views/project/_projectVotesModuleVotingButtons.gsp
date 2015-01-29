<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.ProjectStatusType" %>
<div class="voting">
    <g:if test="${project.status == kuorum.core.model.ProjectStatusType.OPEN}">
        <sec:ifLoggedIn>
            <projectUtil:ifProjectIsVotable project="${project}">
                <ul style="${userVote?'display: none;':''}">
                    <!-- LOGADO NO VOTADO -->
                    <li>
                        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.POSITIVE]}" class="btn btn-blue yes" data-projectId="${project.id}">
                            <span class="icon-smiley fa-2x"></span>
                            <g:message code="project.vote.yes"/>
                        </g:link>
                    </li>
                    <li>
                        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.NEGATIVE]}" class="btn btn-blue no" data-projectId="${project.id}">
                            <span class="icon-sad fa-2x"></span>
                            <g:message code="project.vote.no"/>
                        </g:link>

                    </li>
                    <li>
                        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.ABSTENTION]}" class="btn btn-blue neutral" data-projectId="${project.id}">
                            <span class="icon-neutral fa-2x"></span>
                            <g:message code="project.vote.abs"/>
                        </g:link>
                    </li>
                </ul>

                <!-- LOGADO VOTADO -->
                <a href="#" class="changeOpinion" style="${userVote?'display: block;':''}"><g:message code="project.vote.changeVote"/></a>
            </projectUtil:ifProjectIsVotable>
            <projectUtil:elseProjectIsVotable>
                <!-- LOGADO PERO NO ES DE LA REGION -->
                <a href="#" class="btn btn-blue btn-block vote disabled"><g:message code="project.vote.notYourRegion" encodeAs="raw"/></a>
            </projectUtil:elseProjectIsVotable>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:link mapping="projectShowSec" params="${project.encodeAsLinkProperties()}" class="btn btn-blue btn-block vote">
                <g:message code="project.vote.voteButton" encodeAs="raw"/>
            </g:link><!-- al hacer click lo deshabilito y cambio el texto -->
        </sec:ifNotLoggedIn>
    </g:if> %{--FIN DE LA LEY ABIERTA--}%
    <g:else> %{-- LEY CERRADA--}%
        <g:set var="statusProject" value="${message(code:"${ProjectStatusType.name}.${project.status}")}"/>
        <a href="#" class="btn btn-blue btn-block vote disabled"><g:message code="project.vote.voteClosed" args="[statusProject.toLowerCase()]" encodeAs="raw"/></a><!-- al hacer click lo deshabilito y cambio el texto -->
    </g:else>
    <g:if test="${project.availableStats}">
        <g:link mapping="projectStats" params="${project.encodeAsLinkProperties()}" class="hidden-xs">
            <g:message code="project.vote.stats"/>
        </g:link>
    </g:if>
</div>