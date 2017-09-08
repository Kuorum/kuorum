<g:if test="${debate.photoUrl}">
    <img src="${debate.photoUrl}" alt="${debate.title}">
</g:if>
<g:elseif test="${debate.videoUrl}">
    <image:showYoutube youtube="${debate.videoUrl}"/>
</g:elseif>

<g:if test="${poweredByKuorum}">
    <div class="poweredByKuorum">
        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" target="_blank">Powered by Kuorum</g:link>
    </div>
</g:if>