<div class="multimedia-campaign">
    <g:if test="${campaign.photoUrl}">
        <img itemprop="image" src="${campaign.photoUrl}" alt="${campaign.title}">
    </g:if>
    <g:elseif test="${campaign.videoUrl}">
        <image:showYoutube youtube="${campaign.videoUrl}" campaign="${campaign}"/>
    </g:elseif>
    <g:else>
        <div class="imagen-shadowed-main-color-domain">
            <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${campaign.title}"/>
        </div>
    </g:else>

    <g:if test="${poweredByKuorum}">
        <div class="poweredByKuorum">
            <g:link mapping="debateShow" params="${campaign.encodeAsLinkProperties()}" target="_blank" rel='nofollow noopener noreferrer'>Powered by Kuorum</g:link>
        </div>
    </g:if>
    <g:if test="${!campaign.validationType.equals(org.kuorum.rest.model.communication.CampaignValidationTypeRDTO.NONE)}">
        <div class="multimedia-campaign-validationInfo">
            <div class="center-block">
                <abbr title="${g.message(code:'kuorum.web.commands.payment.CampaignSettingsCommand.validationType.label.info')}">
                    <span class="far fa-check"></span>
                </abbr>
            </div>
        </div>
    </g:if>
</div>