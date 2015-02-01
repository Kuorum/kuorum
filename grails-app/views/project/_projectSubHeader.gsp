<%@ page import="kuorum.core.model.VoteType" %>
<div class="col-xs-6 col-sm-3">
    <div class="laley">${project.hashtag}</div>
</div>
<div class="col-sm-5 hidden-xs">
    <ul class="infoVotes">
        <li class="vote-yes">
            <span><g:formatNumber number="${projectStats.percentagePositiveVotes}" type="percent"/> </span>
            <span class="sr-only"><g:message code="project.subHeader.positiveVotes"/></span>
            <span class="icon-smiley fa-lg"></span>
        </li>
        <li class="vote-no">
            <span><g:formatNumber number="${projectStats.percentageNegativeVotes}" type="percent"/> </span>
            <span class="sr-only"><g:message code="project.subHeader.negativeVotes"/></span>
            <span class="icon-sad fa-lg"></span>
        </li>
        <li class="vote-neutral">
            <span><g:formatNumber number="${projectStats.percentageAbsVotes}" type="percent"/> </span>
            <span class="sr-only"><g:message code="project.subHeader.absVotes"/></span>
            <span class="icon-neutral fa-lg"></span>
        </li>
        <li>
            <span>${projectStats.numPublicPosts}</span>
            <span class="sr-only"><g:message code="project.subHeader.numPosts"/></span>
            <span class="fa fa-lightbulb-o fa-lg"></span>
        </li>
    </ul>
</div>
<div class="col-xs-6 col-sm-4">
    <g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, userVote:userVote, header:Boolean.TRUE]"/>
</div>