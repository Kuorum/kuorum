<campaignUtil:ifActiveEvent campaign="${campaign}">
    <g:render template="/campaigns/columnCModules/eventCallToActionActive" model="[eventUser:eventUser, campaign:campaign, hideSmallDevices:hideSmallDevices]"/>
</campaignUtil:ifActiveEvent>
<campaignUtil:ifInactiveEvent campaign="${campaign}">
    <g:render template="/campaigns/columnCModules/eventCallToActionInactive" model="[eventUser:eventUser, event:campaign.event,hideSmallDevices:hideSmallDevices,closedMsg:closedMsg]"/>
</campaignUtil:ifInactiveEvent>
