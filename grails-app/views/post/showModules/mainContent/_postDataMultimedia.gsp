<g:if test="${post.photoUrl}">
    <img src="${post.photoUrl}" >
</g:if>
<g:elseif test="${post.videoUrl}">
    <image:showYoutube youtube="${post.videoUrl}"/>
</g:elseif>