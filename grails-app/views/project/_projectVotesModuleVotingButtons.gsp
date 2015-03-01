<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.ProjectStatusType" %>
<g:set var="cssIconSize" value="fa-3x"/>
<g:if test="${iconSmall}">
    <g:set var="cssIconSize" value="fa-lg"/>
</g:if>
<div class="${header?"":"form-group"} voting" data-projectId="${project.id}">
    <g:if test="${project.deadline > new Date()}">
        %{--OPEN PROJECT--}%
        <sec:ifLoggedIn>
            <projectUtil:ifUserAvailableForVoting project="${project}">
                <g:render template="projectVotesModuleVotingButtons_normalUser" model="[project:project, cssIconSize:cssIconSize, userVote:userVote, header:header, iconSmall:iconSmall]"/>
            </projectUtil:ifUserAvailableForVoting>
            <projectUtil:elseUserAvailableForVoting>
                <g:render template="projectVotesModuleVotingButtons_PartialLoggedUser" model="[project:project, cssIconSize:cssIconSize, basicPersonalDataCommand:basicPersonalDataCommand, header:header, iconSmall:iconSmall]"/>
            </projectUtil:elseUserAvailableForVoting>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:render template="/project/projectVotesModuleVotingButtons_noLogged" modle="[project:project, cssIconSize:cssIconSize, userVote:userVote, header:header, iconSmall:iconSmall]"/>
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