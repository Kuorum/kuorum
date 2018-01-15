<campaignUtil:ifActiveEvent campaign="${campaign}">
    <g:render template="/campaigns/columnCModules/eventCallToActionActive" model="[eventUser:eventUser, event:campaign.event]"/>
</campaignUtil:ifActiveEvent>
<campaignUtil:ifInactiveEvent campaign="${campaign}" evaluatesIfIsClose="true" evaluatesIfIsNotOpen="true">
    <g:render template="/campaigns/columnCModules/eventCallToActionInactive" model="[eventUser:eventUser, event:campaign.event]"/>
</campaignUtil:ifInactiveEvent>
