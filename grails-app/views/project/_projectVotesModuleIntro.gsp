<%@ page import="kuorum.core.model.VoteType" %>
<g:if test="${title}">
    <h1><g:message code="project.vote.title"/></h1>
    <p><g:message code="project.vote.description"/></p>
</g:if>
<ul class="activity">
    <li class="POSITIVE ${userVote?.voteType==VoteType.POSITIVE?'active':''}"><span>${project.peopleVotes.yes}</span> <g:message code="project.vote.yes"/></li>
    <li class="NEGATIVE ${userVote?.voteType==VoteType.NEGATIVE?'active':''}"><span>${project.peopleVotes.no}</span> <g:message code="project.vote.no"/></li>
    <li class="ABSTENTION ${userVote?.voteType==VoteType.ABSTENTION?'active':''}"><span>${project.peopleVotes.abs}</span> <g:message code="project.vote.abs"/></li>
</ul>
<div class="kuorum">
    <g:message code="project.vote.kuorum.title" args="[necessaryVotesForKuorum]" encodeAs="raw"/>
    <span class="popover-trigger fa fa-info-circle" data-toggle="popover" rel="popover" role="button"></span>
    <!-- POPOVER KUORUM -->
    <div class="popover">
        <div class="popover-kuorum">

            <p class="text-center"><g:message code="project.vote.kuorum.info.title" encodeAs="raw"/></p>
            <p><g:message code="project.vote.kuorum.info.description"/></p>
        </div><!-- /popover-more-kuorum -->
    </div>
    <!-- FIN POPOVER KUORUM -->
</div>