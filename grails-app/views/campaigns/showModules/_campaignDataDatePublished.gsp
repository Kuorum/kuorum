<div class="clearfix">
    <span class="time-ago pull-left"><kuorumDate:humanDate date="${campaign.datePublished}" itemprop="datePublished"/> </span>
    <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
        <div class="campaign-actions pull-right">
            <g:link mapping="politicianCampaignStatsShow" params="[campaignId: campaign.id]" role="button" class="edit"><span class="fal fa-chart-line fa-2x"></span><span class="sr-only">Stats</span></g:link>

            <g:if test="${editable}">
            %{--campaignList contains the js to open modal when the debate is scheduled --}%
                <r:require modules="campaignList"/>
                <g:set var="modal" value="${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
                <g:link class="edit ${modal}" mapping="${editMappingName}" params="${campaign.encodeAsLinkProperties()}">
                    <span class="fal fa-pen-square fa-2x" aria-hidden="true"></span>
                    <span class="sr-only">Edit</span>
                </g:link>
            </g:if>
        </div>
    </userUtil:ifUserIsTheLoggedOne>
</div>