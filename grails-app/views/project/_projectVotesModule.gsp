<%@ page import="kuorum.core.model.ProjectStatusType; kuorum.core.model.VoteType" %>
<section class="boxes vote" id="vote" data-projectId="${project.id}">
    <g:render template="/project/projectVotesModuleIntro" model="[project:project, userVote: userVote, title:title,necessaryVotesForKuorum:necessaryVotesForKuorum ]"/>
    <g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, userVote: userVote]"/>
    <g:if test="${social}">
        <g:render template="/project/projectSocialShare" model="[project:project]"/>
    </g:if>
</section>