<g:if test="${project.image}">
    <div class="photo">
        <img itemprop="image" alt="${project.hashtag}" src="${project.image.url}">
    </div>
</g:if>
<g:else>
    <div class="photo">
        <span> YOUTUBE INTEGRAR: ${project.urlYoutube.url}</span>
    </div>
</g:else>