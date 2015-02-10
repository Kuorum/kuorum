<g:if test="${project.image}">
    <div class="photo">
        <img itemprop="image" alt="${project.hashtag}" src="${project.image.url}">
    </div>
</g:if>
<g:else>
    <div class="video hidden-xs hidden-md visible-lg">
        <a href="#" class="front">
            <span class="fa fa-play-circle fa-4x"></span>
            <img src="http://img.youtube.com/vi/${project.urlYoutube.fileName}/hqdefault.jpg">
        </a>
        <iframe class="youtube" itemprop="video" height="360" src="http://www.youtube.com/embed/${project.urlYoutube.fileName}?fs=1&rel=0&showinfo=0&showsearch=0&autoplay=0&enablejsapi=1" frameborder="0" allowfullscreen></iframe>
    </div>

    <!-- ÑAPA RAPIDA PARA QUE SE VEA BIEN EN LOS MOVILES -->
    <div class="video hidden-lg visible-xs visible-md">
        <a href="http://www.youtube.com/embed/${project.urlYoutube.fileName}?fs=1&rel=0&showinfo=0&showsearch=0&autoplay=1" target="_blank">
            <span class="fa fa-play-circle fa-4x"></span>
            <img src="http://img.youtube.com/vi/${project.urlYoutube.fileName}/hqdefault.jpg">
        </a>
    </div>
</g:else>