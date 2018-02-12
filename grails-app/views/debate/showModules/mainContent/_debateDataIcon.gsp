<g:if test="${debate.campaignStatusRSDTO == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
    <div class="comment-counter pull-right">
        <button type="button">
            <span class="fa fa-lightbulb-o" aria-hidden="true"></span>
            <span class="number">${numberProposals}</span>
        </button>
    </div>
</g:if>