<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[contest.visits]"/></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.numFollowers]"/></span>
    </li>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate
                date="${contest.lastActivity}"/></span>
    </li>
    <li>
        <span class="fal fa-flag" aria-hidden="true"></span>
        <span class="info"><g:message code="contest.numWinnerApplications"
                                      args="[contest.numWinnerApplications ?: '--']"/>
    </li>
    <li>
        <span class="fal fa-scroll" aria-hidden="true"></span>
        <span class="info"><g:message code="contest.numApplications" args="[contest.numApplications ?: '--']"/>
    </li>
</ul>
