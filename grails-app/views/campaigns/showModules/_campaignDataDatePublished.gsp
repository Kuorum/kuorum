<div class="clearfix">
    <span class="time-ago pull-left"><kuorumDate:humanDate date="${campaign.datePublished}" itemprop="datePublished"/> </span>
        <div class="campaign-actions pull-right">
            <g:if test="${campaign.editable}">
            %{--campaignList contains the js to open modal when the debate is scheduled --}%
                <r:require modules="campaignList"/>

                <g:if test="${summoningButton}">
                    <g:link class="edit summoing-call" mapping="surveySummoning"
                            params="${campaign.encodeAsLinkProperties()}"><span class="fal fa-envelope fa-2x"
                                                                                aria-hidden="true"></span><span
                            class="sr-only">Summoning button</span></g:link>
                    <g:render template="/campaigns/showModules/campingModalCreateSummoning" model="[]"/>
                </g:if>

                <g:if test="${campaign.statsEnabled == true}">
                    <g:link mapping="politicianCampaignStatsShow" params="[campaignId: campaign.id]" role="button"
                            class="edit"><span class="fal fa-chart-line fa-2x"></span><span
                            class="sr-only">Stats</span></g:link>
                </g:if>

                <g:set var="modal"
                       value="${campaign.newsletter.status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SCHEDULED ? 'modalEditScheduled' : ''}"/>
                <g:link class="edit ${modal}" mapping="${editMappingName}"
                        params="${campaign.encodeAsLinkProperties()}">
                    <span class="fal fa-pen-square fa-2x" aria-hidden="true"></span>
                    <span class="sr-only">Edit</span>
                </g:link>
            </g:if>
        </div>
</div>