<%@ page import="kuorum.core.model.ProjectStatusType; kuorum.core.model.VoteType" %>
<section class="boxes vote signin" id="vote" data-projectId="${project.id}">
    %{--<g:render template="/project/projectVotesModuleIntro" model="[project:project, userVote: userVote, title:title,necessaryVotesForKuorum:necessaryVotesForKuorum ]"/>--}%
    <g:set var="numDays" value="${kuorumDate.differenceDays(initDate:  project.deadline, endDate: new Date())}"/>
    <h1><g:message code="project.vote.headTitle" args="[numDays]"/> </h1>

    <sec:ifLoggedIn>
        <projectUtil:ifUserAvailableForVoting project="${project}">
            <form action="#" method="post" name="sign" role="form" autocomplete="off" id="sign">
                <g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, userVote: userVote, header:Boolean.FALSE]"/>
            </form>
        </projectUtil:ifUserAvailableForVoting>
    </sec:ifLoggedIn>

    <g:if test="${social}">
        <g:render template="/project/projectSocialShare" model="[project:project]"/>
    </g:if>
</section>