<sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
<sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
<g:set var="buyTicketActive" value="${event.amountAssistants<event.capacity}"/>
<div class="comment-counter pull-right">
    <button type="button"
            class="${!event.registered & buyTicketActive?'event-confirm-button':''} ${event.registered?'active disabled':''} ${!buyTicketActive?'disabled':''}"
            data-userLoggedAlias="${userUtil.loggedUserAlias()}"
            data-postUrl="${g.createLink(mapping: 'eventBookTicket', params:event.encodeAsLinkProperties())}"

    >
        <span class="fa fa-ticket" aria-hidden="true"></span>
        <span class="number">${event.amountAssistants}</span>
    </button>
</div>