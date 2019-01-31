<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[petition.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.authorFollowers" args="[campaignUser.numFollowers]" /></span>
    </li>
    <li>
        <span class="fal fa-microphone" aria-hidden="true"></span>
        <span class="info"><g:message code="petition.stats.numberSigns" args="[petition.signs]" /></span>
    </li>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${petition.lastActivity}"/> </span>
    </li>
</ul>
