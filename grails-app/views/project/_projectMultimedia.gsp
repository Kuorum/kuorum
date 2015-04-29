<g:if test="${projectImage}">
    <div class="photo">
        <img itemprop="image" alt="${hashtag}" src="${projectImage.url}">
    </div>
</g:if>
<g:elseif test="${youtube}">
    <div class="video">
        <a href="#" class="front">
            <span class="fa fa-play-circle fa-4x"></span>
            <img src="http://img.youtube.com/vi/${youtube.fileName}/maxresdefault.jpg">
            <!-- servir esta otra si el video no es alta resolucion -->
            <!-- <img src="http://img.youtube.com/vi/rhK0UJEuww4/mqdefault.jpg"> -->
        </a>
        <iframe class="youtube" itemprop="video" src="https://www.youtube.com/embed/${youtube.fileName}?fs=1&rel=0&showinfo=0&autoplay=0&enablejsapi=1&showsearch=0" frameborder="0" allowfullscreen></iframe>
    </div>
</g:elseif>