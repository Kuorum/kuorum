<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[contestApplication.visits]"/></span>
    </li>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate
                date="${contestApplication.lastActivity}"/></span>
    </li>
    <li>
        <span class="fal fa-globe" aria-hidden="true"></span>
        <span class="info"><g:message
                code="org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.shortLabel"/>: <g:message
                code="org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.${contestApplication.activityType}"/></span>
    </li>
    <li>
        <span class="fal fa-users" aria-hidden="true"></span>
        <span class="info"><g:message
                code="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.shortLabel"/>: <g:message
                code="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.${contestApplication.focusType}"/></span>
    </li>
</ul>
