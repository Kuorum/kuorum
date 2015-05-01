<g:if test="${projectImage}">
    <div class="photo">
        <img itemprop="image" alt="${hashtag}" src="${projectImage.url}">
    </div>
</g:if>
<g:elseif test="${youtube}">
    <image:showYoutube youtube="${youtube}"/>
</g:elseif>