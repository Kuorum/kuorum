
<g:if test="${projectUpdate.image}">
    <div class="photo">
        <img itemprop="image" alt="${projectUpdate.image.originalName}" src="${projectUpdate.image.url}">
    </div>
</g:if>
<g:elseif test="${projectUpdate.urlYoutube}">
   <image:showYoutube youtube="${projectUpdate.urlYoutube}"/>
</g:elseif>