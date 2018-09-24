<div class="clearfix">
    <span class="time-ago pull-left"><kuorumDate:humanDate date="${campaign.datePublished}" itemprop="datePublished"/> </span>
    <g:if test="${editable}">
        <userUtil:ifUserIsTheLoggedOne user="${campaignUser}">
        %{--campaignList contains the js to open modal when the debate is scheduled --}%
            <r:require modules="campaignList"/>
            <g:set var="modal" value="${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ?'modalEditScheduled':''}"/>
            <g:link class="edit ${modal}" mapping="${editMappingName}" params="${campaign.encodeAsLinkProperties()}">
                <span class="fal fa-pen-square pull-right fa-2x" aria-hidden="true"></span>
            </g:link>
        </userUtil:ifUserIsTheLoggedOne>
    </g:if>
</div>