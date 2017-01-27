<g:if test="${debate.photoUrl}">
    <img src="${debate.photoUrl}" >
</g:if>
<g:elseif test="${debate.videoUrl}">
    <image:showYoutube youtube="${debate.videoUrl}"/>
</g:elseif>