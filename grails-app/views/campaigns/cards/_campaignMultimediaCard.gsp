<div class="card-header-photo">
    <g:if test="${campaign.videoUrl}">
        <image:showYoutube youtube="${campaign.videoUrl}" campaign="${campaign}"/>
    </g:if>
    <g:elseif test="${campaign.photoUrl}">
        <img itemprop="image" src="${campaign.photoUrl}" alt="${campaign.title}">
    </g:elseif>
    <g:else>
        <div class="imagen-shadowed-main-color-domain">
            <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${campaign.title}"/>
        </div>
    </g:else>
</div>