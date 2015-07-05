<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.ProjectStatusType" %>
<g:set var="cssIconSize" value="fa-3x"/>
<g:if test="${iconSmall}">
    <g:set var="cssIconSize" value="fa-lg"/>
</g:if>
<div class="${header?"":"form-group"} voting" data-projectId="${project.id}">
    <g:if test="${project.deadline > new Date()}">
        %{--OPEN PROJECT--}%
        <sec:ifLoggedIn>
            <projectUtil:ifUserAvailableForNormalVoting project="${project}">
                <g:render template="/project/projectVotesModuleVotingButtons_normalUser" model="[project:project, userVote:userVote, cssIconSize:cssIconSize, userVote:userVote, header:header, iconSmall:iconSmall]"/>
            </projectUtil:ifUserAvailableForNormalVoting>
            <projectUtil:elseUserAvailableForNormalVoting>
                <g:if test="${basicPersonalDataCommand}">
                    <projectUtil:ifUserAvailableForVotingWithoutPersonalData project="${project}">
                        <g:render template="/project/projectVotesModuleVotingButtons_PartialLoggedUser" model="[project:project, cssIconSize:cssIconSize, basicPersonalDataCommand:basicPersonalDataCommand, header:header, iconSmall:iconSmall]"/>
                    </projectUtil:ifUserAvailableForVotingWithoutPersonalData>
                    <projectUtil:ifUserAvailableForVotingWithoutConfirmedMail project="${project}">
                        <g:render template="/project/projectVotesModuleVotingButtons_noMailConfirmed" model="[project:project, cssIconSize:cssIconSize, basicPersonalDataCommand:basicPersonalDataCommand, header:header, iconSmall:iconSmall]"/>
                    </projectUtil:ifUserAvailableForVotingWithoutConfirmedMail>
                </g:if>
                <g:else>
                    <g:render template="/project/projectVotesModuleVotingButtons_partialLoggedUserNoForm" model="[project:project, cssIconSize:cssIconSize, header:header, iconSmall:iconSmall]"/>
                </g:else>
            </projectUtil:elseUserAvailableForNormalVoting>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:render template="/project/projectVotesModuleVotingButtons_noLogged" modle="[project:project, cssIconSize:cssIconSize, userVote:userVote, header:header, iconSmall:iconSmall]"/>
        </sec:ifNotLoggedIn>
    </g:if> %{--FIN DE LA LEY ABIERTA--}%
    <g:else> %{-- LEY CERRADA--}%
        <ul>
        <li>
        <span class="closed">
            <g:message code="project.subHeader.closedProject"/>
        </span>
        </li>
        <li>
            <projectUtil:ifAllowedToUpdateProject project="${project}">
                <g:link mapping="projectUpdate" params="${project.encodeAsLinkProperties()}" class="update">
                    <span class="fa icon2-update fa-lg"></span>
                    <span class="${header?'sr-only':''}"><g:message code="project.vote.updateProject"/></span>
                </g:link>
            </projectUtil:ifAllowedToUpdateProject>
        </li>
        </ul>
    </g:else>
    %{--<g:if test="${project.availableStats}">--}%
        %{--<g:link mapping="projectStats" params="${project.encodeAsLinkProperties()}" class="hidden-xs">--}%
            %{--<g:message code="project.vote.stats"/>--}%
        %{--</g:link>--}%
    %{--</g:if>--}%
</div>