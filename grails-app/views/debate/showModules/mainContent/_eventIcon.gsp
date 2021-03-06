
<campaignUtil:ifActiveEvent campaign="${campaign}">
    <g:set var="event" value="${campaign.event}"/>
    <sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
    <sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
    <g:set var="buyTicketActive" value="${event.amountAssistants<event.capacity}"/>
    <div class="comment-counter pull-right">
        <button type="button"
                class="${!event.registered & buyTicketActive?'event-confirm-button':''} ${event.registered?'active disabled':''} ${!buyTicketActive?'disabled':''}"
                data-userLoggedAlias="${userUtil.loggedUserId()}"
                data-eventUserId="${campaign.user.id}"
                data-postUrl="${g.createLink(mapping: 'eventBookTicket', params:campaign.encodeAsLinkProperties())}"

        >
            <span class="fal fa-ticket-alt" aria-hidden="true"></span>
            <span class="number">${event.amountAssistants}</span>
        </button>
    </div>
</campaignUtil:ifActiveEvent>