<%@ page import="kuorum.core.model.VoteType" %>
<div class="col-xs-6 col-sm-3">
    <div class="laley">${project.hashtag}</div>
</div>
<div class="col-sm-5 hidden-xs">
    <ul class="infoVotes">
        <g:render template="/project/projectLiBasicPercentageStats" model="[project:project, projectBasicStats:projectBasicStats, extraCss:'']"/>
    </ul>
</div>

<g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, userVote: userVote, header:Boolean.TRUE]"/>
