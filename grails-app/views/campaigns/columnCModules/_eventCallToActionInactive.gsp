<div class="hidden-sm comment-box call-to-action call-to-action-confirm-event">
    <div class="comment-header event-unconfirmed">
        <span class="call-title">${message(code: "event.callToAction.title")}</span>
        <campaignUtil:ifInactiveEvent campaign="${campaign}" evaluatesIfIsClose="true" evaluatesIfIsNotOpen="false">
            <span class="call-subTitle">${message(code: "event.callToAction.subTitle.close", args: [eventUser.name])}</span>
        </campaignUtil:ifInactiveEvent>
        <campaignUtil:ifInactiveEvent campaign="${campaign}" evaluatesIfIsClose="false" evaluatesIfIsNotOpen="true">
            <span class="call-subTitle">${message(code: "event.callToAction.subTitle.noOpen", args: [eventUser.name])}</span>
        </campaignUtil:ifInactiveEvent>
    </div>
</div>