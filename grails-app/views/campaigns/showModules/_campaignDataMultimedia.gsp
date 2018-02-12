<g:if test="${campaign.photoUrl}">
    <img itemprop="image" src="${campaign.photoUrl}" alt="${campaign.title}">
</g:if>
<g:elseif test="${campaign.videoUrl}">
    <image:showYoutube youtube="${campaign.videoUrl}"/>
</g:elseif>

<g:if test="${poweredByKuorum}">
    <div class="poweredByKuorum">
        <g:link mapping="debateShow" params="${campaign.encodeAsLinkProperties()}" target="_blank">Powered by Kuorum</g:link>
    </div>
</g:if>