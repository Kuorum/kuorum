<ul class="leader-post-stats">
    <li>
        <span class="fal fa-eye" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.timesSeen" args="[petition.visits]" /></span>
    </li>
    <li>
        <span class="fal fa-microphone" aria-hidden="true"></span>
        <span class="info"><g:message code="petition.stats.numberSigns" args="[petition.signs]" /></span>
    </li>
    <li>
        <span class="fal fa-clock" aria-hidden="true"></span>
        <span class="info"><g:message code="debate.lastActivity"/> <kuorumDate:humanDate date="${petition.lastActivity}"/> </span>
    </li>
    <g:if test="${petition.startDate}">
        <li>
            <span class="fal fa-calendar-check" aria-hidden="true"></span>
            <span class="info"><g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.startDate.label"/>:
            <g:formatDate format="dd/MM/yyyy HH:mm" date="${petition.startDate}" timeZone="${displayTimeZone}"/>
            <kuorumDate:printTimeZoneName date="${petition.startDate}" zoneInfo="${displayTimeZone}"/>
            </span>
        </li>
    </g:if>
    <g:if test="${petition.endDate}">
        <li>
            <span class="fal fa-calendar-times" aria-hidden="true"></span>
            <span class="info"><g:message code="kuorum.web.commands.payment.CampaignSettingsCommand.endDate.label"/>:
            <g:formatDate format="dd/MM/yyyy HH:mm"  date="${petition.endDate}" timeZone="${displayTimeZone}"/>
            <kuorumDate:printTimeZoneName date="${petition.endDate}" zoneInfo="${displayTimeZone}"/>
            </span>
        </li>
    </g:if>
</ul>
