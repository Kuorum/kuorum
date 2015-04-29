
<g:if test="${projectUpdate.image}">
    <div class="photo">
        <img itemprop="image" alt="${projectUpdate.image.originalName}" src="${projectUpdate.image.url}">
    </div>
</g:if>
<g:elseif test="${projectUpdate.urlYoutube}">
    <div class="video">
        <a href="#" class="front">
            <span class="fa fa-play-circle fa-4x"></span>
            <img src="http://img.youtube.com/vi/${projectUpdate.urlYoutube.originalName}/mqdefault.jpg">
            <!-- servir esta otra si el video no es alta resolucion -->
            <!-- <img src="http://img.youtube.com/vi/fQDQO4VRpF8/mqdefault.jpg"> -->
            <!-- servir esta otra si el video es alta resolucion -->
            <!-- <img src="http://img.youtube.com/vi/fQDQO4VRpF8/maxresdefault.jpg"> -->
        </a>
        <iframe class="youtube" itemprop="video" src="https://www.youtube.com/embed/${projectUpdate.urlYoutube.originalName}?fs=1&rel=0&showinfo=0&showsearch=0&autoplay=0&enablejsapi=1" frameborder="0" allowfullscreen></iframe>
    </div>
</g:elseif>