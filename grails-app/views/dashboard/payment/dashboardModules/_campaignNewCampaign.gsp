<div class="box-ppal" id="createNewCampaign">
    <g:if test="${!numberCampaigns}">
        <g:render template="/newsletter/chooseCampaign" model="[chooseCampaignTitle:g.message(code:'dashboard.payment.newCampaign.neverSent',encodeAs: 'raw')]"/>
    </g:if>
    <g:else>
        <g:render template="/newsletter/chooseCampaign" model="[chooseCampaignTitle:g.message(code:'dashboard.payment.newCampaign.lastCampaignSent', args:[durationDays], encodeAs: 'raw')]"/>
    </g:else>
</div>