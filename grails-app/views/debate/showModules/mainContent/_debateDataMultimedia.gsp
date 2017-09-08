<g:if test="${debate.photoUrl}">
    <img src="${debate.photoUrl}" alt="${debate.title}">
</g:if>
<g:elseif test="${debate.videoUrl}">
    <image:showYoutube youtube="${debate.videoUrl}"/>
</g:elseif>