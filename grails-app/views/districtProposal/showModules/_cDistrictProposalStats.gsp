<ul class="leader-post-stats">
    <li>
        <span class="fa fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[districtProposal.visits]" /></span>
    </li>
    <li>
        <span class="fa fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.followers.size()]" /></span>
    </li>
    <li>
        <span class="fa fa-rocket" aria-hidden="true"></span>
        <span class="info"><g:message code="districtProposal.stats.numberSupports" args="[districtProposal.numSupports]" /></span>
    </li>
    <li>
        <span class="fa fa-shopping-cart" aria-hidden="true"></span>
        <span class="info"><g:message code="districtProposal.stats.numberVotes" args="[districtProposal.numVotes]" /></span>
    </li>
    <li>
        <span class="fa fa-clock-o" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${districtProposal.lastActivity}"/> </span>
    </li>
</ul>
